package com.cts.cheetah.model;

import org.json.JSONObject;

/**
 * Created by manu.palassery on 12-07-2017.
 */

public interface ICommonDataListener {
    void onCommonDataSuccess(String api, JSONObject object);
    void onCommonDataError(String api,String error);
}
