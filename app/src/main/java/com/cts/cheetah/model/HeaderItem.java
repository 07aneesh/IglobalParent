package com.cts.cheetah.model;

/**
 * Created by manu.palassery on 10-07-2017.
 */

public class HeaderItem {

    String name;
    boolean isSelected=false;

    public HeaderItem(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
