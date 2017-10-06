package com.cts.cheetah.view.gps;

import org.json.JSONObject;

/**
 * Created by dell on 9/20/2017.
 */

public interface IGPS {
    void onStudentDailyTripData(JSONObject result);
    void onStudentDailyTripError(String error);

    void onTripLatitudeLongitudeData(JSONObject result);
    void onTripLatitudeLongitudeError(String error);
}
