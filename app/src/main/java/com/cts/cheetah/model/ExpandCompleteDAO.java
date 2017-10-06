package com.cts.cheetah.model;

import java.util.ArrayList;

/**
 * Created by manu.palassery on 23-02-2017.
 * ExpandCompleteDAO is tailored to adopt data required for Complete Trip details, mainly to populate heterogenous type of data
 * in an Expandable List view.
 */

public class ExpandCompleteDAO {
    public static final String CONTENT_TYPE_SAFETY="safetyInstructions";
    public static final String CONTENT_TYPE_ACCESSORIALS="accessorials";
    public static final String CONTENT_TYPE_PAYMNET="payment";
    public static final String CONTENT_TYPE_ITEM="item";
    public static final String CONTENT_TYPE_NOTE="note";

    private String contentType;
    private String tripId;
    private ArrayList<String> safetyInstructions = new ArrayList<>();
    private ArrayList<String> accessorials = new ArrayList<>();
    private TripPayout payout;
    private OrderItem orderItem;
    private String note;


    public ExpandCompleteDAO() {
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

    public ArrayList<String> getSafetyInstructions() {
        return safetyInstructions;
    }

    public void setSafetyInstructions(ArrayList<String> safetyInstructions) {
        this.safetyInstructions = safetyInstructions;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSearchString() {
         //String searchString = tripId + ":" + tripDistance + ":" + tripDuration + ":" + tripDate + ":" + tripStatus + ":";

        int i=0;
       /* for(PickupLocation item:tripPickUpAddress){
            searchString += tripPickUpAddress.get(i).getPickupAddress() + ":";
        }

        i=0;
        for(DropLocation item:tripDropAddress){
            searchString += tripDropAddress.get(i).getDropAddress() + ":";
        }*/

        //searchString = searchString.substring(0,searchString.length()-1);

        //return searchString;
        return "";
    }

}
