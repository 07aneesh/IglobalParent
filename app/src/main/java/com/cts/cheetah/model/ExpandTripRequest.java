package com.cts.cheetah.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by manu.palassery on 23-02-2017.
 * TripRequest 'DAO' is tailored to adopt data, required for TripRequest and its detail, mainly to populate heterogenous type of data
 * in an Expandable List view.
 */

public class ExpandTripRequest {
    public static final String CONTENT_TYPE_HEADER="header";
    public static final String CONTENT_TYPE_PICKUP="pickup";
    public static final String CONTENT_TYPE_DROP="drop";
    public static final String CONTENT_TYPE_SAFETY="safetyInstructions";
    public static final String CONTENT_TYPE_ACCESSORIALS="accessorials";
    public static final String CONTENT_TYPE_PAYMNET="payment";

    private String contentType;
    private String tripId;
    private String tripControlNo;
    private float tripDistance;
    private int tripDuration;
    private String tripDate;
    private String tripStatus;
    /*private ArrayList<PickupLocation> tripPickUpAddress = new ArrayList<>();
    private ArrayList<DropLocation> tripDropAddress = new ArrayList<>();*/
    private ArrayList<String> accessorials = new ArrayList<>();
    private TripPayout payout;
    private boolean isHazardousItem = false;
    private Location location;


    public ExpandTripRequest() {
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTripControlNo() {
        return tripControlNo;
    }

    public void setTripControlNo(String tripControlNo) {
        this.tripControlNo = tripControlNo;
    }

    public float getTripDistance() {
        return tripDistance;
    }

    public void setTripDistance(float tripDistance) {
        this.tripDistance = tripDistance;
    }

    public int getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

   /* public ArrayList<PickupLocation> getTripPickUpAddress() {
        return tripPickUpAddress;
    }

    public void setTripPickUpAddress(ArrayList<PickupLocation> tripPickUpAddress) {
        this.tripPickUpAddress = tripPickUpAddress;
    }

    public ArrayList<DropLocation> getTripDropAddress() {
        return tripDropAddress;
    }

    public void setTripDropAddress(ArrayList<DropLocation> tripDropAddress) {
        this.tripDropAddress = tripDropAddress;
    }*/

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
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

    public boolean isHazardousItem() {
        return isHazardousItem;
    }

    public void setHazardousItem(boolean hazardousItem) {
        isHazardousItem = hazardousItem;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getSearchString() {
         String searchString = tripId + ":" + tripDistance + ":" + tripDuration + ":" + tripDate + ":" + tripStatus + ":";

        int i=0;
       /* for(PickupLocation item:tripPickUpAddress){
            searchString += tripPickUpAddress.get(i).getPickupAddress() + ":";
        }

        i=0;
        for(DropLocation item:tripDropAddress){
            searchString += tripDropAddress.get(i).getDropAddress() + ":";
        }*/

        searchString = searchString.substring(0,searchString.length()-1);

        return searchString;
    }

}
