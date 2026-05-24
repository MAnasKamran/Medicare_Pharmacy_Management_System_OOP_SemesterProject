package com.pharmacy.service;

import com.pharmacy.dao.MedicineDAO;
import com.pharmacy.dao.SaleDAO;
import com.pharmacy.generics.Result;
import com.pharmacy.model.*;
import com.pharmacy.util.AuditLogger;
import com.pharmacy.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Point-of-Sale service — orchestrates the checkout workflow.
 * OOP: Service layer, Composition, Generic Result wrapper
 */
public class POSService {

    private static POSService instance;

    private final MedicineDAO    medicineDAO = MedicineDAO.getInstance();
    private final SaleDAO        saleDAO     = SaleDAO.getInstance();
    private final SessionManager session     = SessionManager.getInstance();

    private POSService() {}

    public static synchronized POSService getInstance() {
        if (instance == null) instance = new POSService();
        return instance;
    }

    /**
     * Validate and add a medicine to the cart.
     *
     * @param cart  current cart (mutable)
     * @param medId medicine to add
     * @param qty   quantity requested
     * @return Result.ok(cart) or Result.fail(reason)
     */
    public Result<List<CartItem>> addToCart(List<CartItem> cart, int medId, int qty) {
        session.requireLogin();

        var medOpt = medicineDAO.findById(medId);
        if (medOpt.isEmpty())   return Result.fail("Medicine not found.");

        Medicine med = medOpt.get();
        if (!med.isActive())    return Result.fail(med.getName() + " is not available for sale.");
        if (med.isOutOfStock()) return Result.fail("Out of stock: " + med.getName());

        // Total already in cart
        int inCart = cart.stream()
            .filter(ci -> ci.getMedId() == medId)
            .mapToInt(CartItem::getQuantity)
            .sum();

        if (inCart + qty > med.getStockQty()) {
            return Result.fail("Cannot exceed available stock (" + med.getStockQty() + ") for " + med.getName() + ".");
        }

        // Narcotic: pharmacist or admin only
        if (med.isNarcotic() && !session.isPharmacist() && !session.isAdmin()) {
            return Result.fail("Only a licensed pharmacist can dispense narcotic substances.");
        }

        // Add or increment existing cart line
        boolean found = false;
        for (CartItem ci : cart) {
            if (ci.getMedId() == medId) {
                ci.setQuantity(ci.getQuantity() + qty);
                found = true;
                break;
            }
        }
        if (!found) cart.add(new CartItem(med, qty));

        return Result.ok(cart);
    }

    /** Remove an item from the cart by medicine ID. */
    public Result<List<CartItem>> removeFromCart(List<CartItem> cart, int medId) {
        boolean removed = cart.removeIf(ci -> ci.getMedId() == medId);
        return removed ? Result.ok(cart) : Result.fail("Item not found in cart.");
    }

    /** Compute a sale summary from the cart. */
    public SaleSummary computeSummary(List<CartItem> cart, double discountPct, double taxPct) {
        double subtotal = cart.stream().mapToDouble(CartItem::getSubtotal).sum();
        double discAmt  = subtotal * discountPct / 100.0;
        double taxable  = subtotal - discAmt;
        double taxAmt   = taxable  * taxPct  / 100.0;
        double grand    = taxable  + taxAmt;
        return new SaleSummary(subtotal, discAmt, taxAmt, grand);
    }

    /** Validate a discount percentage against the current user's role limit. */
    public Result<Double> validateDiscount(double pct) {
        User u = session.getCurrentUser();
        double max = u.isAdmin() ? 30 : u.isPharmacist() ? 20 : 10;
        if (pct < 0)   return Result.fail("Discount cannot be negative.");
        if (pct > max) return Result.fail("Your role allows max " + (int) max + "% discount.");
        return Result.ok(pct);
    }

    /**
     * Complete checkout: build Sale, deduct stock, persist.
     *
     * @param cart          items in the cart
     * @param summary       pre-computed totals
     * @param paymentMethod selected payment method
     * @param amountPaid    cash tendered (ignored for non-cash)
     * @param patient       optional patient; null = walk-in
     * @return Result.ok(sale) or Result.fail(reason)
     */
    public Result<Sale> checkout(List<CartItem> cart,
                                 SaleSummary   summary,
                                 Sale.PaymentMethod paymentMethod,
                                 double        amountPaid,
                                 Patient       patient) {
        session.requireLogin();

        if (cart == null || cart.isEmpty()) return Result.fail("Cart is empty.");

        if (paymentMethod == Sale.PaymentMethod.CASH && amountPaid < summary.grandTotal()) {
            return Result.fail(String.format(
                "Amount paid (Rs %.2f) is less than grand total (Rs %.2f).",
                amountPaid, summary.grandTotal()));
        }

        // Re-verify stock before committing
        for (CartItem ci : cart) {
            var medOpt = medicineDAO.findById(ci.getMedId());
            if (medOpt.isEmpty())                             return Result.fail("Medicine not found: " + ci.getMedicineName());
            if (medOpt.get().getStockQty() < ci.getQuantity()) return Result.fail("Insufficient stock for " + ci.getMedicineName());
        }

        // Build Sale
        User user = session.getCurrentUser();
        Sale sale = new Sale();
        sale.setUserId(user.getId());
        sale.setCashierName(user.getFullName());
        sale.setSubtotal(summary.subtotal());
        sale.setDiscountAmount(summary.discountAmount());
        sale.setTaxAmount(summary.taxAmount());
        sale.setGrandTotal(summary.grandTotal());
        sale.setAmountPaid(amountPaid);
        sale.setChangeReturned(Math.max(0, amountPaid - summary.grandTotal()));
        sale.setPaymentMethod(paymentMethod);
        sale.setStatus(Sale.Status.COMPLETED);

        if (patient != null) {
            sale.setPatientId(patient.getId());
            sale.setPatientName(patient.getFullName());
        }

        // Build SaleItems and deduct stock
        List<SaleItem> items = new ArrayList<>();
        for (CartItem ci : cart) {
            items.add(SaleItem.fromCartItem(ci));
            medicineDAO.findById(ci.getMedId()).ifPresent(med -> {
                med.deductStock(ci.getQuantity());
                medicineDAO.update(med);
            });
        }
        sale.setItems(items);

        saleDAO.save(sale);
        AuditLogger.log("SALE", String.format("Sale #%d by %s — Rs %.2f via %s",
            sale.getId(), user.getFullName(), sale.getGrandTotal(), paymentMethod));

        return Result.ok(sale);
    }

    /** Immutable value object for computed totals. */
    public record SaleSummary(double subtotal, double discountAmount, double taxAmount, double grandTotal) {
        public double changeFor(double paid) { return Math.max(0, paid - grandTotal); }
    }
}
