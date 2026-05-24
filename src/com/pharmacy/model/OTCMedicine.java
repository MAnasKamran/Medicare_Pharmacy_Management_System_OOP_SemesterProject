package com.pharmacy.model;

public class OTCMedicine extends Medicine {
    private static final long serialVersionUID = 1L;

    private boolean discountEligible = true;
    private double  discountRate;

    @Override
    public String getDetails() {
        return super.getDetails() + " | OTC";
    }

    public double getDiscountedPrice() {
        return getUnitPrice() * (1.0 - discountRate / 100.0);
    }

    public boolean isDiscountEligible()        { return discountEligible; }
    public void    setDiscountEligible(boolean v) { this.discountEligible = v; }
    public double  getDiscountRate()           { return discountRate; }
    public void    setDiscountRate(double v)   { this.discountRate = Math.max(0, v); }
}
