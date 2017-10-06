package com.cts.cheetah.model;

import org.json.JSONObject;

/**
 * Created by manu.palassery on 24-03-2017.
 */

public class Notification {

    private String objectId;
    private String objectTypeId;
    private String notificationTypeId;
    private String notificationType;
    private String notificationMessage;
    private String receivedTime;

    public Notification(){

    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public String getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(String notificationId) {
        this.notificationTypeId = notificationId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(String receivedTime) {
        this.receivedTime = receivedTime;
    }

    public void setData(JSONObject jsonObject){
        try {
            setObjectId("objectId");
            setObjectTypeId("objectTypeId");
            setNotificationTypeId(jsonObject.getString("typeId"));
            setNotificationType(jsonObject.getString(""));
            setNotificationMessage(jsonObject.getString("description"));
            setReceivedTime(jsonObject.getString("sentTime"));
        }catch (Exception e){

        }
    }
}
