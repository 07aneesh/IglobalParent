package com.cts.cheetah.model;

/**
 * Created by manu.palassery on 27-03-2017.
 */

public class OrderItem {
    String ItemName;
    String ItemType;
    String ItemDescription;
    String ItemWeight;
    String ItemQuantity;
    String ItemDimension;
    String ItemCommodity;
    boolean isItemHazard=false;
    HazardItem hazardItem;

    public OrderItem(){

    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemType() {
        return ItemType;
    }

    public void setItemType(String itemType) {
        ItemType = itemType;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public String getItemWeight() {
        return ItemWeight;
    }

    public void setItemWeight(String itemWeight) {
        ItemWeight = itemWeight;
    }

    public String getItemQuantity() {
        return ItemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        ItemQuantity = itemQuantity;
    }

    public String getItemDimension() {
        return ItemDimension;
    }

    public void setItemDimension(String itemDimension) {
        ItemDimension = itemDimension;
    }

    public String getItemCommodity() {
        return ItemCommodity;
    }

    public void setItemCommodity(String itemCommodity) {
        ItemCommodity = itemCommodity;
    }

    public boolean isItemHazard() {
        return isItemHazard;
    }

    public void setItemHazard(boolean itemHazard) {
        isItemHazard = itemHazard;
    }

    public HazardItem getHazardItem() {
        return hazardItem;
    }

    public void setHazardItem(HazardItem hazardItem) {
        this.hazardItem = hazardItem;
    }
}
