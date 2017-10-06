package com.cts.cheetah.model;

import java.util.ArrayList;

/**
 * Created by manu.palassery on 20-02-2017.
 */

public class Property {

    String propertyName;
    String propertyAddress;
    int bedCount;
    int bathCount;
    int squareFeet;

    public void property(){

    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public int getBedCount() {
        return bedCount;
    }

    public void setBedCount(int bedCount) {
        this.bedCount = bedCount;
    }

    public int getBathCount() {
        return bathCount;
    }

    public void setBathCount(int bathCount) {
        this.bathCount = bathCount;
    }

    public int getSquareFeet() {
        return squareFeet;
    }

    public void setSquareFeet(int squareFeet) {
        this.squareFeet = squareFeet;
    }

    public ArrayList<Property> getPropertyData(){
        ArrayList<Property> propertiesList = new ArrayList<>();

        ArrayList<String> properties = new ArrayList();

        properties.add("1344 Shades Crest Road, Birmingham AL");
        properties.add("2625 Highland Avenue S #609, Birmingham AL");
        properties.add("2600 Highland Avenue S #201, Birmingham AL");
        properties.add("1216 4th Avenue S, Birmingham AL");
        properties.add("1833 Fulton Avenue SW, Birmingham AL");
        properties.add("1908 Fulton Avenue SW, Birmingham AL");
        properties.add("2313 Princeton Avenue SW, Birmingham AL");
        properties.add("913 Pratt Highway, Birmingham AL");

        for (int i=0;i<8;i++){
            Property property = new Property();
            property.setPropertyName(properties.get(i).split(",")[0]);
            property.setPropertyAddress(properties.get(i).split(",")[1]);
            property.setBathCount(2);
            property.setBedCount(3);
            property.setSquareFeet(Integer.parseInt(properties.get(i).split(" ")[0]));
            propertiesList.add(property);
        }

        return propertiesList;
    }

    public String getSearchString() {
        return propertyName + ":" + propertyAddress;
    }

}
