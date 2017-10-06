package com.cts.cheetah.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by manu.palassery on 02-06-2017.
 * Location can be set as multiple and single by setting parameter (boolean) loacationAsSingle in setData() function;
 */

public class CompletedTripDAO {

    public static final String CONTENT_TYPE_PICKUP="pickup";
    public static final String CONTENT_TYPE_DROP="drop";
    public static final String CONTENT_TYPE_SAFETY="safetyInstructions";
    public static final String CONTENT_TYPE_ITEMS="items";
    public static final String CONTENT_TYPE_ACCESSORIALS="accessorials";
    public static final String CONTENT_TYPE_PAYMNET="payment";

    private String contentType;

    private String tripId;
    private float tripDistance;
    private int tripDuration;
    private int timerDuration;
    private String tripDate;
    private String tripStatus;
    private ArrayList<Location> locations;
    private ArrayList<Location> pickupLocations;
    private ArrayList<Location> dropLocations;
    private ArrayList<Location> roundTripLocations;
    private ArrayList<String> accessorials = new ArrayList<>();
    private TripPayout payout;
    private ArrayList<String> notes;


    public CompletedTripDAO() {
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

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }


    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.locations = locations;
    }

    public ArrayList<Location> getPickupLocations() {
        return pickupLocations;
    }

    public void setPickupLocations(ArrayList<Location> pickupLocations) {
        this.pickupLocations = pickupLocations;
    }

    public ArrayList<Location> getDropLocations() {
        return dropLocations;
    }

    public void setDropLocations(ArrayList<Location> dropLocations) {
        this.dropLocations = dropLocations;
    }

    public ArrayList<Location> getRoundTripLocations() {
        return roundTripLocations;
    }

    public void setRoundTripLocations(ArrayList<Location> roundTripLocations) {
        this.roundTripLocations = roundTripLocations;
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

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }

    /*public String getSearchString() {
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
    }*/

    /**
     *
     * @param resultObject - result json object
     * @param loacationAsSingle - if true location is single, else locations are set into pickup and drop off.
     */
    public void setData(JSONObject resultObject,boolean loacationAsSingle){
          try {
            setTripId(resultObject.getString("orderId"));
            setTripDate(resultObject.getString("tripDate"));
            //-----------------------------------------------
            //
            JSONArray locations = new JSONArray(resultObject.getString("locations"));
            Location location;
            ArrayList<Location> locationArrayList = new ArrayList<>();
            ArrayList<Location> pickLocationArrayList = new ArrayList<>();
            ArrayList<Location> dropLocationArrayList = new ArrayList<>();
              ArrayList<Location> roundTripLocationArrayList = new ArrayList<>();
            for (int i=0;i<locations.length();i++){
                location = new Location();
                JSONObject locationObject = new JSONObject(locations.get(i).toString());
                location.setLocationOrder(locationObject.getString("locationId"));
                location.setLocationType(locationObject.getString("locationType"));
                location.setAddress(locationObject.getString("address"));
                location.setLatitude(locationObject.getString("latitude"));
                location.setLongitude(locationObject.getString("longitude"));
                location.setTime(locationObject.getString("arrivalTime"));
                location.setDate(locationObject.getString("date"));
                location.setIsHazardous(locationObject.getString("isHazardous"));

                /*JSONObject contacts = new JSONObject(locationObject.getString("contacts"));
                location.setContactPerson(contacts.getString("contactPerson"));
                location.setContactNo(contacts.getString("contactNo"));
                location.setAlternateContactPerson(contacts.getString("alternateContactPerson"));
                location.setAlternateContactNo(contacts.getString("alternateContactNo"));*/
                //
                JSONArray safetyArray = new JSONArray(locationObject.getString("safetyInstructions"));
                ArrayList<String> safetyInstructionsList = new ArrayList<>();
                for (int j=0;j<safetyArray.length();j++){
                    JSONObject safetyObject = new JSONObject(safetyArray.get(j).toString());
                    safetyInstructionsList.add(safetyObject.getString("instruction"));
                }
                location.setSafetyInstructions(safetyInstructionsList);
                //---------------------------------------------------
                locationArrayList.add(location);
                switch(location.getLocationType().toLowerCase()){
                    case("pickup"):
                        pickLocationArrayList.add(location);
                        break;
                    case("dropoff"):
                        dropLocationArrayList.add(location);
                        break;
                    case("roundtrip"):
                        roundTripLocationArrayList.add(location);
                        break;

                }

            }
              if(loacationAsSingle) {
                  setLocations(locationArrayList);
              }else {
                  setPickupLocations(pickLocationArrayList);
                  setDropLocations(dropLocationArrayList);
                  setRoundTripLocations(roundTripLocationArrayList);
              }
            //-----------------------------------------
            //
            JSONArray accessorialsJsonArray = new JSONArray(resultObject.getString("accessorials"));
            ArrayList<String> accessorialsList = new ArrayList<>();
            for (int j=0;j<accessorialsJsonArray.length();j++){
                JSONObject accesorialObject = new JSONObject(accessorialsJsonArray.get(j).toString());
                accessorialsList.add(accesorialObject.getString("name"));
            }
            setAccessorials(accessorialsList);
            //------------------------------------------
            //
            JSONArray notesArray = new JSONArray(resultObject.getString("notes"));
              ArrayList<String> notesList = new ArrayList<>();
              for (int j=0;j<notesArray.length();j++){
                  JSONObject notesObject = new JSONObject(notesArray.get(j).toString());
                  notesList.add(notesObject.getString("note"));
              }
            setNotes(notesList);
            //
            JSONObject payObject = new JSONObject(resultObject.getString("payment"));
            TripPayout tripPayout = new TripPayout();
            tripPayout.setBasePay(payObject.getString("basePay"));
            tripPayout.setFuelSurcharge(payObject.getString("fuelSurcharge"));
            tripPayout.setTotalPay(payObject.getString("total"));
            setPayout(tripPayout);
        }catch (JSONException e){
            Log.i("",""+e);
        }
    }

}
