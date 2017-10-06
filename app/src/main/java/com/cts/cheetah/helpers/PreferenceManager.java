package com.cts.cheetah.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import com.cts.cheetah.service.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fingent on 16/2/17.
 */
public class PreferenceManager {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static PreferenceManager instance;

    public PreferenceManager(Context context){
        this.context=context;

        try {
            sharedPreferences = context.getSharedPreferences(ApplicationRef.LoginTags.USER_PREFERENCE_FILE, 0);
            editor = sharedPreferences.edit();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public static PreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceManager(context);
        }
        return instance;
    }
    public void setBaseUrl(String baseUrl){
        editor.putString(ApplicationRef.AppConfig.BASE_URL, baseUrl);
        editor.commit();
    }

    public void setApiVersion(String version){
        editor.putString(ApplicationRef.AppConfig.API_VERSION, version);
        editor.commit();
    }

    public void setAllowAccess(Boolean value){
        editor.putBoolean(ApplicationRef.AppConfig.ALLOW_APP_ACCESS,value);
        editor.commit();
    }
    public Boolean getAllowAccess(){
        return sharedPreferences.getBoolean(ApplicationRef.AppConfig.ALLOW_APP_ACCESS,true);
    }

    public void setStoreUrl(String url){
        editor.putString(ApplicationRef.AppConfig.STORE_URL,url);
        editor.commit();
    }
    public String getStroreUrl(){
        return sharedPreferences.getString(ApplicationRef.AppConfig.STORE_URL,"");
    }

    public String getApiVersion(){
        return sharedPreferences.getString(ApplicationRef.AppConfig.API_VERSION, "");
    }

    public String getBaseUrl(){
        return sharedPreferences.getString(ApplicationRef.AppConfig.BASE_URL, "");
    }

    //LOGIN DATA START----------------------------------------------
    public  void setFirstName(String name){
        editor.putString(ApplicationRef.LoginTags.DRIVER_FIRST_NAME, name);
        editor.commit();
    }

    public String getFirstName(){
        return sharedPreferences.getString(ApplicationRef.LoginTags.DRIVER_FIRST_NAME, "");
    }

    public  void setLastName(String name){
        editor.putString(ApplicationRef.LoginTags.DRIVER_LAST_NAME, name);
        editor.commit();
    }

    public String getLastName(){
        return sharedPreferences.getString(ApplicationRef.LoginTags.DRIVER_FIRST_NAME, "");
    }


    public  void setDriverID(String driverID){
        editor.putString(ApplicationRef.LoginTags.DRIVER_ID, driverID);
        editor.commit();
    }

    public String getDriverID(){
        return sharedPreferences.getString(ApplicationRef.LoginTags.DRIVER_ID, "");
    }

    public void setDriverEmail(String email){
        editor.putString(ApplicationRef.LoginTags.DRIVER_EMAIL, email);
        editor.commit();
    }

    public String getDriverEmail(){
        return sharedPreferences.getString(ApplicationRef.LoginTags.DRIVER_EMAIL, "");
    }

    public  void setAccessToken(String token){
        editor.putString(ApplicationRef.LoginTags.ACCESS_TOKEN_TAG, token);
        editor.commit();
    }

    public String getAccessToken(){
        return sharedPreferences.getString(ApplicationRef.LoginTags.ACCESS_TOKEN_TAG, "");
    }

    public void setDriverAvailabilityStatus(String availability){
        editor.putString(ApplicationRef.LoginTags.DRIVER_AVAILABILITY_STATUS, availability);
        editor.commit();
    }

    public String getDriverAvailabilityStatus(){
        return sharedPreferences.getString(ApplicationRef.LoginTags.DRIVER_AVAILABILITY_STATUS, "");
    }

    public void setUserPreferences(JSONObject jsonObject){
        try {
            JSONObject data = jsonObject.getJSONObject("account");
            setFirstName(data.getString(ApplicationRef.LoginTags.DRIVER_FIRST_NAME));
            setLastName(data.getString(ApplicationRef.LoginTags.DRIVER_LAST_NAME));
            setDriverEmail(data.getString(ApplicationRef.LoginTags.DRIVER_EMAIL));
           // setDriverID(data.getString(ApplicationRef.LoginTags.DRIVER_ID));
         //   setDriverAvailabilityStatus(data.getString(ApplicationRef.LoginTags.DRIVER_AVAILABILITY_STATUS));
            setAccessToken(data.getString(ApplicationRef.LoginTags.ACCESS_TOKEN_TAG));

        }catch (JSONException e){
            e.printStackTrace();
        }

    }
    //LOGIN DATA END----------------------------------------------

    public void setFirebaseId(String id){
        editor.putString("FirebaseId", id);
        editor.commit();
    }

    public String getFirebaseId(){
        return sharedPreferences.getString("GcmDeviceId","");
    }

    public void clearUserPreferences(){
        editor.clear();
        setBaseUrl(Service.BASE_URL);
        editor.commit();
    }
}
