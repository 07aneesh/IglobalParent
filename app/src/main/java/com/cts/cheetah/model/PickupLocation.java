package com.cts.cheetah.model;

/**
 * Created by manu.palassery on 16-03-2017.
 */

public class PickupLocation {

    String PickupDate;
    String PickupTime;
    String PickupAddress;
    String LocationNo;

    public PickupLocation(){

    }

    public String getLocationNo() {
        return LocationNo;
    }

    public void setLocationNo(String locationNumber) {
        LocationNo = locationNumber;
    }

    public String getPickupDate() {
        return PickupDate;
    }

    public void setPickupDate(String pickupDate) {
        PickupDate = pickupDate;
    }

    public String getPickupTime() {
        return PickupTime;
    }

    public void setPickupTime(String pickupTime) {
        PickupTime = pickupTime;
    }

    public String getPickupAddress() {
        return PickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        PickupAddress = pickupAddress;
    }
}
