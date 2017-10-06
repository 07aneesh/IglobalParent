package com.cts.cheetah.service;


import org.json.JSONException;
import org.json.JSONObject;

public interface IBaseService {
    void onSuccess(JSONObject result) throws JSONException;
    void onError(String error);
//    void onTripLatitudeLongitudeSuccess(JSONObject result) throws JSONException;
//    void onTripLatitudeLongitudeError(String error);
}
