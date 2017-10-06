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

public class TripRequest {
    public static final String CONTENT_TYPE_HEADER="header";
    public static final String CONTENT_TYPE_PICKUP="pickup";
    public static final String CONTENT_TYPE_DROP="drop";
    public static final String CONTENT_TYPE_DROP_OFF="DropOff";
    public static final String CONTENT_TYPE_SAFETY="safetyInstructions";
    public static final String CONTENT_TYPE_ACCESSORIALS="accessorials";
    public static final String CONTENT_TYPE_PAYMNET="payment";

    private String contentType;
    private String tripId;
    private String controlNo;
    private float tripDistance;
    private int tripDuration;
    private int timerDuration;
    private String tripDate;
    private String tripStatus;
    private ArrayList<PickupLocation> tripPickUpAddress = new ArrayList<>();//-----Check that this could be removed
    private ArrayList<DropLocation> tripDropAddress = new ArrayList<>();
    private ArrayList<Location> locations;
    private ArrayList<String> accessorials = new ArrayList<>();
    private TripPayout payout;
    private boolean isHazardousItem = false;


    public TripRequest() {
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

    public String getControlNo() {        return controlNo;
    }

    public void setControlNo(String controlNo) {
        this.controlNo = controlNo;
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

    public int getTimerDuration() {
        return timerDuration;
    }

    public void setTimerDuration(int timerDuration) {
        this.timerDuration = timerDuration;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public ArrayList<PickupLocation> getTripPickUpAddress() {
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
    }

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

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

    public void setData(JSONObject data){
        try{

            JSONObject results = data.getJSONObject("results");

            if(results != null){
                setTripId(results.getString("orderId"));
                setControlNo(results.getString("controlNo"));
                setTripDate(results.getString("tripDate"));
                setTripDuration(results.getInt("tripDuration"));
                setTripDistance(results.getInt("tripDistance"));

                setTimerDuration(results.getInt("timerDuration"));
                //setTripStatus(results.getString("tripStatus"));

                //Set Accessorials
                JSONArray accessorials = results.getJSONArray("accessorials");
                if(accessorials.length() > 0){
                    ArrayList<String> arrayList = new ArrayList<>();
                    for (int i=0;i<accessorials.length();i++){
                        JSONObject jo = new JSONObject(accessorials.get(i).toString());
                        arrayList.add(jo.getString("name"));
                    }
                    setAccessorials(arrayList);
                }

                //Set payout
                JSONObject payOut = results.getJSONObject("payment");
                if(payOut != null){
                    TripPayout tripPayout = new TripPayout();
                    tripPayout.setBasePay(payOut.getString("basePay"));
                    tripPayout.setFuelSurcharge(payOut.getString("fuelSurcharge"));
                    tripPayout.setTotalPay(payOut.getString("total"));
                    setPayout(tripPayout);
                }

                //Set Locations start--------------------------------------------------------------------
                JSONArray locations = results.getJSONArray("locations");
                if(locations.length() > 0){
                    ArrayList<Location> locationsArray = new ArrayList<>();
                    ArrayList<Location> pickLocationsArray = new ArrayList<>();
                    ArrayList<Location> dropLocationsArray = new ArrayList<>();

                    for (int i=0;i<locations.length();i++){
                        Location location = new Location();
                        //
                        JSONObject locationObject = new JSONObject(locations.get(i).toString());
                        location.setLocationOrder(locationObject.getString("locationOrder"));
                        location.setLocationOrderNo(locationObject.getString("locationOrderNo"));
                        location.setLocationType(locationObject.getString("locationType"));
                        location.setAddress(locationObject.getString("address"));
                        location.setTime(locationObject.getString("arrivalTime"));
                        location.setDate(locationObject.getString("date"));
                        location.setLatitude(locationObject.getString("latitude"));
                        location.setLongitude(locationObject.getString("longitude"));

                        JSONArray safetyInstructions = locationObject.getJSONArray("safetyInstructions");
                        if(safetyInstructions.length() >0){
                            ArrayList<String> safetyInstructionsArray = new ArrayList<>();
                            for (int j=0;j<safetyInstructions.length();j++){
                                JSONObject jo = new JSONObject(safetyInstructions.get(j).toString());
                                safetyInstructionsArray.add(jo.getString("instruction"));
                            }
                            location.setSafetyInstructions(safetyInstructionsArray);
                        }

                        JSONArray items = locationObject.getJSONArray("items");
                        if(items.length() >0){
                            ArrayList<OrderItem> itemsArray = new ArrayList<>();
                            OrderItem orderItem;
                            for (int j=0;j<items.length();j++){
                                orderItem = new OrderItem();
                                JSONObject jo = new JSONObject(items.get(j).toString());
                                orderItem.setItemName(jo.getString("commodity"));
                                orderItem.setItemType(jo.getString("packageType"));
                                orderItem.setItemQuantity(jo.getString("quantity"));
                                orderItem.setItemDimension(jo.getString("dimension"));
                                orderItem.setItemWeight(jo.getString("weight"));
                                orderItem.setItemHazard(jo.getBoolean("isHazardous"));

                                JSONObject hazardObject = jo.getJSONObject("hazardData");
                                HazardItem hazardItem = new HazardItem();
                                hazardItem.setUnIdNumber(hazardObject.getString("unIdNumber"));
                                hazardItem.setHazardClass(hazardObject.getString("hazardClass"));
                                hazardItem.setPg(hazardObject.getString("pg"));
                                hazardItem.setDescription(hazardObject.getString("description"));
                                orderItem.setHazardItem(hazardItem);

                                itemsArray.add(orderItem);
                            }

                            location.setItems(itemsArray);
                        }
                       locationsArray.add(location);
                    }
                    setLocations(locationsArray);
                }
                //Set Locations start--------------------------------------------------------------------

            }
        }catch (JSONException e){
            Log.i("",e+"");
        }
    }

    public String getSearchString() {
         String searchString = tripId + ":" + tripDistance + ":" + tripDuration + ":" + tripDate + ":" + tripStatus + ":";

        int i=0;
        for(PickupLocation item:tripPickUpAddress){
            searchString += tripPickUpAddress.get(i).getPickupAddress() + ":";
        }

        i=0;
        for(DropLocation item:tripDropAddress){
            searchString += tripDropAddress.get(i).getDropAddress() + ":";
        }

        searchString = searchString.substring(0,searchString.length()-1);

        return searchString;
    }

}
