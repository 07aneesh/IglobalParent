package com.cts.cheetah.model;

/**
 * Created by manu.palassery on 14-06-2017.
 */

public class HazardItem {

    String unIdNumber;
    String hazardClass;
    String pg;
    String description;

    public HazardItem(){

    }

    public String getUnIdNumber() {
        return unIdNumber;
    }

    public void setUnIdNumber(String unIdNumber) {
        this.unIdNumber = unIdNumber;
    }

    public String getHazardClass() {
        return hazardClass;
    }

    public void setHazardClass(String hazardClass) {
        this.hazardClass = hazardClass;
    }

    public String getPg() {
        return pg;
    }

    public void setPg(String pg) {
        this.pg = pg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
