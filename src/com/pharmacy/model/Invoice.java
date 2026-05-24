package com.pharmacy.model;

import java.time.format.DateTimeFormatter;

public class Invoice {

    private static final DateTimeFormatter DT_FMT =
        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private final String invoiceNumber;
    private final Sale   sale;
    private final String issuedBy;

    public Invoice(String invoiceNumber, Sale sale, String issuedBy) {
        this.invoiceNumber = invoiceNumber;
        this.sale          = sale;
        this.issuedBy      = issuedBy;
    }

    public String format() {
        return String.format(
            "Invoice No : %s%n" +
            "Issued By  : %s%n" +
            "Date/Time  : %s%n" +
            "Payment    : %s",
            invoiceNumber,
            issuedBy,
            sale.getSaleDate().format(DT_FMT),
            sale.getPaymentMethod()
        );
    }

    public String getInvoiceNumber() { return invoiceNumber; }
    public Sale   getSale()          { return sale; }
    public String getIssuedBy()      { return issuedBy; }
}
