package com.cts.cheetah.view.orders.mytrips.maps.location;

import org.json.JSONObject;

/**
 * Created by manu.palassery on 13-07-2017.
 */

public interface ILocationMap {
    void onSendIssueSuccess(JSONObject object);
    void onSendIssueError(String error);
    void onLocationDataSuccess(JSONObject object);
}
