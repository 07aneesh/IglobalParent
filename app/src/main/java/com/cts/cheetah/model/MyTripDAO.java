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

public class MyTripDAO {

    public static final String CONTENT_TYPE_PICKUP="pickup";
    public static final String CONTENT_TYPE_DROP="drop";
    public static final String CONTENT_TYPE_SAFETY="safetyInstructions";
    public static final String CONTENT_TYPE_ITEMS="items";
    public static final String CONTENT_TYPE_ACCESSORIALS="accessorials";
    public static final String CONTENT_TYPE_PAYMNET="payment";

    private String contentType;

    private String tripId;
    private String controlNo;
    private boolean isRoundTrip;
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


    public MyTripDAO() {
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

    public String getControlNo() {
        return controlNo;
    }

    public void setControlNo(String controlNo) {
        this.controlNo = controlNo;
    }

    public boolean isRoundTrip() {
        return isRoundTrip;
    }

    public void setRoundTrip(boolean roundTrip) {
        isRoundTrip = roundTrip;
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
            setControlNo(resultObject.getString("controlNo"));
            setRoundTrip(resultObject.getBoolean("isRoundTrip"));
            setTripDate(resultObject.getString("tripDate"));
            setTripDuration(resultObject.getInt("tripDuration"));
            setTimerDuration(resultObject.getInt("timerDuration"));
            setTripDistance(resultObject.getInt("tripDistance"));
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
                location.setLocationId(locationObject.getString("locationId"));
                location.setLocationOrder(locationObject.getString("locationOrder"));
                location.setLocationType(locationObject.getString("locationType"));
                location.setLocationStatus(locationObject.getString("locationStatus"));
                location.setAllowedDuration(locationObject.getInt("allowedDuration"));
                location.setAddress(locationObject.getString("address"));
                location.setLatitude(locationObject.getString("latitude"));
                location.setLongitude(locationObject.getString("longitude"));
                location.setTime(locationObject.getString("arrivalTime"));
                location.setDate(locationObject.getString("date"));
                location.setIsHazardous(locationObject.getString("isHazardous"));

                JSONObject contacts = new JSONObject(locationObject.getString("contacts"));
                location.setContactPerson(contacts.getString("contactPerson"));
                location.setContactNo(contacts.getString("contactNo"));
                location.setAlternateContactPerson(contacts.getString("alternateContactPerson"));
                location.setAlternateContactNo(contacts.getString("alternateContactNo"));
                //
                JSONArray safetyArray = new JSONArray(locationObject.getString("safetyInstructions"));
                ArrayList<String> safetyInstructionsList = new ArrayList<>();
                for (int j=0;j<safetyArray.length();j++){
                    JSONObject safetyObject = new JSONObject(safetyArray.get(j).toString());
                    safetyInstructionsList.add(safetyObject.getString("instruction"));
                }
                location.setSafetyInstructions(safetyInstructionsList);
                //---------------------------------------------------
                //
                JSONArray itemsJsonArray = new JSONArray(locationObject.getString("items"));
                ArrayList<OrderItem> itemsList = new ArrayList<>();
                for (int k=0;k<itemsJsonArray.length();k++){
                    OrderItem orderItem = new OrderItem();
                    JSONObject itemObject = new JSONObject(itemsJsonArray.get(k).toString());
                    orderItem.setItemName(itemObject.getString("commodity"));
                    orderItem.setItemCommodity(itemObject.getString("commodity"));
                    orderItem.setItemType(itemObject.getString("packageType"));
                    orderItem.setItemQuantity(itemObject.getString("quantity"));
                    orderItem.setItemDimension(itemObject.getString("dimension"));
                    orderItem.setItemWeight(itemObject.getString("weight"));
                    orderItem.setItemHazard(itemObject.getBoolean("isHazardous"));
                    if(itemObject.getBoolean("isHazardous")){
                        JSONObject hazardObject = new JSONObject(itemObject.getString("hazardData"));
                        HazardItem hazardItem = new HazardItem();
                        hazardItem.setUnIdNumber(hazardObject.getString("unIdNumber"));
                        hazardItem.setHazardClass(hazardObject.getString("hazardClass"));
                        hazardItem.setPg(hazardObject.getString("pg"));
                        hazardItem.setDescription(hazardObject.getString("description"));
                        orderItem.setHazardItem(hazardItem);
                    }
                    itemsList.add(orderItem);
                }
                location.setItems(itemsList);
                //
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
              tripPayout.setAccessorialCharge(payObject.getString("accessorial"));
            tripPayout.setTotalPay(payObject.getString("total"));
            setPayout(tripPayout);
        }catch (JSONException e){
            Log.i("",""+e);
        }
    }

    public MyTripDAO getData(int position){
        MyTripDAO myTripDAO = new MyTripDAO();
        ArrayList<MyTripDAO> tripDataArrayList = new ArrayList<>();

        ArrayList<Location> locationArrayList =  new ArrayList<>();
        Location location = new Location();
        location.setAddress("306 Congress St, Boston, MA 02210, USA");
        location.setDate("2017-05-24 08:01:08");
        location.setTime("10.30");
        location.setLocationOrder("1");
        location.setContactPerson("Erica Sanders");
        location.setContactNo("713 465 900");
        location.setLocationType(Location.LOCATION_TYPE_PICKUP);

        //SAFETY INSTRUCTION
        ArrayList<String>safetyInstructionsList=new ArrayList<>();
        safetyInstructionsList.add("Use appropriate foot protection");
        safetyInstructionsList.add("Use appropriate safety glasses");
        safetyInstructionsList.add("Use appropriate hand gloves");
        location.setSafetyInstructions(safetyInstructionsList);
        //ITEM
        ArrayList<OrderItem>itemsList=new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setItemName("Rotor");
        orderItem.setItemDescription("Control Panel used for electrical circuits. Legrand brand. Has to be handled with care.");
        orderItem.setItemDimension("40in x 48in x 36 in");
        orderItem.setItemQuantity("3");
        orderItem.setItemHazard(true);
        orderItem.setItemCommodity("Machinery");
        orderItem.setItemWeight("120 lbs");
        orderItem.setItemType("Pallet");
        itemsList.add(orderItem);
        //
        orderItem = new OrderItem();
        orderItem.setItemHazard(false);
        orderItem.setItemName("Machinery");
        orderItem.setItemDescription("Control Panel used for electrical circuits. Legrand brand. Has to be handled with care.");
        orderItem.setItemDimension("30in x 38in x 26 in");
        orderItem.setItemQuantity("1");
        orderItem.setItemHazard(false);
        orderItem.setItemCommodity("Machinery");
        orderItem.setItemWeight("140 lbs");
        orderItem.setItemType("Pallet");
        itemsList.add(orderItem);
        location.setItems(itemsList);
        //ACCESSORY
        ArrayList<String>accessorialList=new ArrayList<>();
        accessorialList.add("Rotors");
        location.setAccessorials(accessorialList);
        //PAYOUT
        TripPayout payout = new TripPayout();
        payout.setBasePay("60");
        payout.setFuelSurcharge("40");
        payout.setTotalPay("100");
        location.setPayout(payout);

        locationArrayList.add(location);

        if(position == 1){
            itemsList=new ArrayList<>();
            orderItem = new OrderItem();
            orderItem.setItemName("Furniture");
            orderItem.setItemDescription("House hold items");
            orderItem.setItemDimension("30in x 38in x 26 in");
            orderItem.setItemQuantity("1");
            orderItem.setItemHazard(false);
            orderItem.setItemCommodity("Tables");
            orderItem.setItemWeight("140 lbs");
            orderItem.setItemType("Wood");
            itemsList.add(orderItem);
            //

            location = new Location();
            //SAFETY INSTRUCTION
            ArrayList<String>safetyInstructionsList2=new ArrayList<>();
            safetyInstructionsList2.add("Appropriate foot protection");
            safetyInstructionsList2.add("Appropriate safety glasses");
            location.setSafetyInstructions(safetyInstructionsList2);

            location.setAddress("St James Corner, TX 2210, USA");
            location.setDate("2017-05-24 09:01:08");
            location.setTime("11.30");
            location.setLocationOrder("2");
            location.setContactPerson("Jim Sanders");
            location.setContactNo("713 465 900");
            location.setLocationType(Location.LOCATION_TYPE_PICKUP);
            location.setItems(itemsList);
            location.setAccessorials(accessorialList);
            location.setPayout(payout);
            locationArrayList.add(location);
        }
        //

        if(position == 0) {
            location = new Location();
            ArrayList<String>safetyInstructionsList3=new ArrayList<>();
            safetyInstructionsList3.add("Appropriate foot protection");
            location.setSafetyInstructions(safetyInstructionsList3);

            location.setAddress("58 Summer St, Boston, MA 02110, USA");
            location.setDate("2017-05-24 09:01:08");
            location.setTime("12.30");
            location.setLocationOrder("2");
            location.setContactPerson("Garry Gibson");
            location.setContactNo("893 065 900");
            location.setLocationType(Location.LOCATION_TYPE_DROP);
            location.setItems(itemsList);
            locationArrayList.add(location);
        }
        //
        location = new Location();
        ArrayList<String>safetyInstructionsList4=new ArrayList<>();
        safetyInstructionsList4.add("Safety Shoes");
        location.setSafetyInstructions(safetyInstructionsList4);
        location.setAddress("66 Summer St, Boston, MA 02111, USA");
        location.setDate("2017-05-24 10:01:08");
        location.setTime("2.30");
        location.setLocationOrder("1");
        location.setContactPerson("Jim Samson");
        location.setContactNo("870 095 800");
        location.setLocationType(Location.LOCATION_TYPE_DROP);

        itemsList=new ArrayList<>();
        orderItem = new OrderItem();
        orderItem.setItemName("Tarp");
        orderItem.setItemDescription("Water proof polyuretane");
        orderItem.setItemDimension("30in x 38in x 26 in");
        orderItem.setItemQuantity("1");
        orderItem.setItemHazard(false);
        orderItem.setItemCommodity("Machinery");
        orderItem.setItemWeight("140 lbs");
        orderItem.setItemType("Pallet");
        itemsList.add(orderItem);

        location.setItems(itemsList);
        locationArrayList.add(location);

        myTripDAO.setLocations(locationArrayList);

        tripDataArrayList.add(myTripDAO);


        return myTripDAO;
    }
}
