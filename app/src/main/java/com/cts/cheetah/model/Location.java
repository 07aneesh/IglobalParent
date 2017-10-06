package com.cts.cheetah.model;

import com.cts.cheetah.helpers.ApplicationRef;

import java.util.ArrayList;

/**
 * Created by manu.palassery on 16-03-2017.
 */

public class Location {

    public static final String LOCATION_TYPE_PICKUP= ApplicationRef.TripTags.LOCATION_TYPE_PICK_UP;
    public static final String LOCATION_TYPE_DROP=ApplicationRef.TripTags.LOCATION_TYPE_DROP_OFF;
    public static final String LOCATION_TYPE_ROUND_TRIP=ApplicationRef.TripTags.LOCATION_TYPE_ROUND_TRIP;

    String date;
    String time;
    String address;
    String locationOrder;
    String locationOrderNo;
    String locationId;
    String locationStatus;
    String locationType;
    int allowedDuration;
    String contactNo;
    String contactPerson;
    String alternateContactNo;
    String alternateContactPerson;
    String latitude;
    String longitude;
    String isHazardous;
    private ArrayList<String> safetyInstructions = new ArrayList<>();
    private ArrayList<OrderItem> items = new ArrayList<>();
    private ArrayList<String> accessorials = new ArrayList<>();
    private TripPayout payout;

    public Location(){

    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocationOrder() {
        return locationOrder;
    }

    public void setLocationOrder(String locationOrder) {
        this.locationOrder = locationOrder;
    }

    public String getLocationOrderNo() {
        return locationOrderNo;
    }

    public void setLocationOrderNo(String locationOrderNo) {
        this.locationOrderNo = locationOrderNo;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(String locationStatus) {
        this.locationStatus = locationStatus;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public int getAllowedDuration() {
        return allowedDuration;
    }

    public void setAllowedDuration(int allowedDuration) {
        this.allowedDuration = allowedDuration;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getAlternateContactNo() {
        return alternateContactNo;
    }

    public void setAlternateContactNo(String alternateContactNo) {
        this.alternateContactNo = alternateContactNo;
    }

    public String getAlternateContactPerson() {
        return alternateContactPerson;
    }

    public void setAlternateContactPerson(String alternateContactPerson) {
        this.alternateContactPerson = alternateContactPerson;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public ArrayList<String> getSafetyInstructions() {
        return safetyInstructions;
    }

    public void setSafetyInstructions(ArrayList<String> safetyInstructions) {
        this.safetyInstructions = safetyInstructions;
    }

    public ArrayList<String> getAccessorials() {
        return accessorials;
    }

    public void setAccessorials(ArrayList<String> accessorials) {
        this.accessorials = accessorials;
    }

    public TripPayout getPayout() {
        return payout;
    }

    public void setPayout(TripPayout payout) {
        this.payout = payout;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderItem> items) {
        this.items = items;
    }

    public String getIsHazardous() {
        return isHazardous;
    }

    public void setIsHazardous(String isHazardous) {
        this.isHazardous = isHazardous;
    }
}
