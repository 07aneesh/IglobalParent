package com.cts.cheetah.view.main;

import org.json.JSONObject;

/**
 * Created by manu.palassery on 10-05-2017.
 */

public interface IMainDashboard {
    void onAvailabilityStatusSuccess(JSONObject result);
    void onAvailabilityStatusError(String error);
}
