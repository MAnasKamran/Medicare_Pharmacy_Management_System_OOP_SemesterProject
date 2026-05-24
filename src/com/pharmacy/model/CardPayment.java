package com.pharmacy.model;

public class CardPayment implements Payable {
    private static final long serialVersionUID = 1L;

    private String cardNumber;
    private String cardHolder;
    private double amountCharged;

    public CardPayment() {}

    public CardPayment(String cardNumber, String cardHolder) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
    }

    @Override
    public void processPayment(double amount) {
        if (cardNumber == null || cardNumber.isBlank())
            throw new IllegalArgumentException("Card number is required.");
        if (cardHolder == null || cardHolder.isBlank())
            throw new IllegalArgumentException("Card holder name is required.");
        if (cardNumber.replaceAll("\\s", "").length() < 12)
            throw new IllegalArgumentException("Invalid card number.");
        this.amountCharged = amount;
    }

    @Override
    public String generateReceipt() {
        String masked = "****-****-****-" + cardNumber.replaceAll("\\s", "")
            .substring(Math.max(0, cardNumber.replaceAll("\\s","").length() - 4));
        return String.format("Card | %s | Holder: %s | Rs %.2f", masked, cardHolder, amountCharged);
    }

    @Override
    public String getPaymentType() { return "Card"; }

    public String getCardNumber()            { return cardNumber; }
    public void   setCardNumber(String v)    { this.cardNumber = v; }
    public String getCardHolder()            { return cardHolder; }
    public void   setCardHolder(String v)    { this.cardHolder = v; }
    public double getAmountCharged()         { return amountCharged; }
    public void   setAmountCharged(double v) { this.amountCharged = v; }
}
