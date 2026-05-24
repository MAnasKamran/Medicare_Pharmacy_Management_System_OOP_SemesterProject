package com.pharmacy.service;

import com.pharmacy.dao.MedicineDAO;
import com.pharmacy.dao.SupplierDAO;
import com.pharmacy.generics.Result;
import com.pharmacy.model.Medicine;
import com.pharmacy.model.Supplier;
import com.pharmacy.util.AuditLogger;

import java.util.List;
import java.util.Optional;

/**
 * Inventory management service — medicines and suppliers.
 * OOP: Service layer, Generics (Result<T>), Singleton
 */
public class InventoryService {

    private static InventoryService instance;

    private final MedicineDAO medDAO      = MedicineDAO.getInstance();
    private final SupplierDAO supplierDAO = SupplierDAO.getInstance();

    private InventoryService() {}

    public static synchronized InventoryService getInstance() {
        if (instance == null) instance = new InventoryService();
        return instance;
    }

    // ── Medicine operations ───────────────────────────────────

    public Result<Medicine> addMedicine(Medicine med) {
        if (!med.isValid()) return Result.fail("Medicine data is invalid.");
        Medicine saved = medDAO.save(med);
        AuditLogger.log("ADD_MEDICINE", "Added: " + saved.getName() + " (id=" + saved.getId() + ")");
        return Result.ok(saved);
    }

    public Result<Medicine> updateMedicine(Medicine med) {
        if (medDAO.findById(med.getId()).isEmpty())
            return Result.fail("Medicine id=" + med.getId() + " not found.");
        medDAO.update(med);
        AuditLogger.log("EDIT_MEDICINE", "Updated: " + med.getName());
        return Result.ok(med);
    }

    public Result<Void> deleteMedicine(int id) {
        Optional<Medicine> opt = medDAO.findById(id);
        if (opt.isEmpty()) return Result.fail("Medicine not found.");
        opt.get().deactivate();
        medDAO.update(opt.get());
        AuditLogger.log("DELETE_MEDICINE", "Deactivated: " + opt.get().getName());
        return Result.ok();
    }

    public Result<Void> addStock(int medId, int qty, String batchNote) {
        Optional<Medicine> opt = medDAO.findById(medId);
        if (opt.isEmpty()) return Result.fail("Medicine not found.");
        if (qty <= 0) return Result.fail("Quantity must be positive.");
        Medicine med = opt.get();
        med.addStock(qty);
        medDAO.update(med);
        AuditLogger.log("ADD_STOCK", "Added " + qty + " units to " + med.getName()
            + (batchNote != null && !batchNote.isBlank() ? " | " + batchNote : ""));
        return Result.ok();
    }

    public List<Medicine> getAllActive()        { return medDAO.findActive(); }
    public List<Medicine> getLowStock()         { return medDAO.findLowStock(); }
    public List<Medicine> getOutOfStock()       { return medDAO.findOutOfStock(); }
    public List<Medicine> search(String kw)     { return medDAO.search(kw); }
    public Optional<Medicine> findById(int id)  { return medDAO.findById(id); }

    // ── Supplier operations ───────────────────────────────────

    public Result<Supplier> addSupplier(Supplier s) {
        if (!s.isValid()) return Result.fail("Supplier name is required.");
        Supplier saved = supplierDAO.save(s);
        AuditLogger.log("ADD_SUPPLIER", "Added: " + saved.getName());
        return Result.ok(saved);
    }

    public Result<Supplier> updateSupplier(Supplier s) {
        supplierDAO.update(s);
        AuditLogger.log("EDIT_SUPPLIER", "Updated: " + s.getName());
        return Result.ok(s);
    }

    public Result<Void> deleteSupplier(int id) {
        Optional<Supplier> opt = supplierDAO.findById(id);
        if (opt.isEmpty()) return Result.fail("Supplier not found.");
        opt.get().deactivate();
        supplierDAO.update(opt.get());
        AuditLogger.log("DELETE_SUPPLIER", "Deactivated supplier id=" + id);
        return Result.ok();
    }

    public List<Supplier> getAllSuppliers()         { return supplierDAO.findActive(); }
    public List<Supplier> searchSuppliers(String kw){ return supplierDAO.search(kw); }
}
