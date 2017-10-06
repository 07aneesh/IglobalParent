package com.cts.cheetah.view.orders.mytrips.maps.Route;

import org.json.JSONObject;

/**
 * Created by manu.palassery on 13-07-2017.
 */

public interface IRouteMap {
    void onTripDataSuccess(JSONObject object);
    void onTripDataError(String error);
    void onIssueOrStatusSuccess(JSONObject object);
    void onIssueOrStatusError(String error);
}
