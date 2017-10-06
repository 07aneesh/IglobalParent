package com.cts.cheetah.model;

/**
 * Created by manu.palassery on 16-03-2017.
 */

public class DropLocation {
    String DropDate;
    String DropTime;
    String DropAddress;
    String LocationNo;

    public DropLocation(){

    }

    public String getLocationNo() {
        return LocationNo;
    }

    public void setLocationNo(String locationNo) {
        LocationNo = locationNo;
    }

    public String getDropTime() {
        return DropTime;
    }

    public void setDropTime(String DropTime) {
        this.DropTime = DropTime;
    }

    public String getDropDate() {
        return DropDate;
    }

    public void setDropDate(String DropDate) {
        this.DropDate = DropDate;
    }

    public String getDropAddress() {
        return DropAddress;
    }

    public void setDropAddress(String DropAddress) {
        this.DropAddress = DropAddress;
    }
    
}
