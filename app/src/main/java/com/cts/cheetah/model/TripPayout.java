package com.cts.cheetah.model;

/**
 * Created by manu.palassery on 19-05-2017.
 */

public class TripPayout {

    private String basePay;
    private String fuelSurcharge;
    private String accessorialCharge;
    private String totalPay;

    public TripPayout() {
    }

    public String getBasePay() {
        return basePay;
    }

    public void setBasePay(String basePay) {
        this.basePay = basePay;
    }

    public String getFuelSurcharge() {
        return fuelSurcharge;
    }

    public void setFuelSurcharge(String fuelSurcharge) {
        this.fuelSurcharge = fuelSurcharge;
    }

    public String getAccessorialCharge() {
        return accessorialCharge;
    }

    public void setAccessorialCharge(String accessorialCharge) {
        this.accessorialCharge = accessorialCharge;
    }

    public String getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(String totalPay) {
        this.totalPay = totalPay;
    }
}
