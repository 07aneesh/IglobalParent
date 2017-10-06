package com.cts.cheetah.view.notifications.fragment;

import org.json.JSONObject;

/**
 * Created by manu.palassery on 12-05-2017.
 */

public interface INotification {
    void onNotificationSuccess(JSONObject result);
    void onNotificationError(String error);
}
