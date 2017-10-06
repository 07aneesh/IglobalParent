package com.cts.cheetah.service;

/**
 * Created by fts012 on 24/10/16.
 */

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ExecutorDelivery;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cts.cheetah.R;
import com.cts.cheetah.helpers.AppSingleton;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.view.login.ILoginController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Service {
    public static String BASE_URL = "http://as.gvssolutions.org/api/MobileAPI/";
    public static String REQUEST_BODY_CONTENT_TYPE = "application/json; charset=utf-8";
    private static final String RESULT = "result";
    private static final String SUCCESS = "success";
    private static final String STATUS = "status";
    private HashMap<String,String> params;
    private JSONObject mBodyParams;
    private Context mContext;
    private int mRequestMethod = Request.Method.POST;
    private String url;
    private IBaseService mlistener;
    private String relativeUrl;

    public void execute(String relativeUrl, int requestMethod , final HashMap<String, String> params , final IBaseService listener, Context context ) throws JSONException {
        this.mContext= context;
        this.url = getBaseUrl() + relativeUrl;
        this.mlistener = listener;
        this.relativeUrl = relativeUrl;
        this.mRequestMethod=requestMethod;
        this.params= params;
        executeRequest();
    }
    public void execute(String url, int requestMethod , final IBaseService listener ) throws JSONException {
        this.url = url;
        this.mlistener = listener;
        this.mRequestMethod=requestMethod;
        executeRequest();
    }
    private void executeRequest() {

        StringRequest stringRequest=new StringRequest(mRequestMethod, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                jsonParse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Error jsonError = new Error();
                NetworkResponse response = error.networkResponse;
                if (error instanceof NoConnectionError) {
                    mlistener.onError("No connection error");
                }
                else if (error.networkResponse != null) {
                    int statusCode = error.networkResponse.statusCode;
                    String json = new String(response.data);
                    jsonError = new Error(json);
                    //jsonError.setCode(statusCode);
                    mlistener.onError(statusCode+"");
                }else {
                    //jsonError.setMessage("OOps something went wrong"); // add to strings.xml
                    if (error.getClass().equals(TimeoutError.class)){
                        mlistener.onError("Timeout Error");
                    }else{
                        mlistener.onError("Error");
                    }


                }
            }
        }){
                @Override
                protected Map<String, String> getParams() {
                return params;
            }

        };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(mContext.getApplicationContext()).addToRequestQueue(stringRequest, relativeUrl);//----------------------------------------------------
    }



    public void jsonParse(String response){
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONObject jresult;
            //jresult = mainObject.getJSONObject("results");
            jresult =  new JSONObject(response);
            String action=null;
            String status=null;
            String statusMsg=null;
            try {
                status=jresult.getString(ApplicationRef.Service.STATUS);
                statusMsg=jresult.getString(ApplicationRef.Service.STATUS_MSG);

                if(status.equalsIgnoreCase(ApplicationRef.Service.STATUS_SUCCESS)) {
                    mlistener.onSuccess(jresult);
                    Log.i("JSON", jresult.get(ApplicationRef.Service.DATA).toString());
                }else if(status.equalsIgnoreCase(ApplicationRef.Service.STATUS_FAILED)){
                    Log.i("Controller", "dsdsew");
                    mlistener.onError(statusMsg);
                }else if(status.equalsIgnoreCase(ApplicationRef.Service.STATUS_DUPLICATE_LOGIN)){
                    ((ILoginController)mlistener).duplicateLogin();
                }
            }catch (JSONException e){
                Utility.logger(e);
            }
        } catch (JSONException e) {
            Utility.logger(e);
        }
    }

    private String getBaseUrl(){
        /*if(BuildConfig.DEBUG == false){
            return BASE_URL;
        }
        return  BASE_URL = PreferenceManager.getInstance(mContext).getBaseUrl();*/
        return BASE_URL;
    }

    public void sendJsonObject(String relativeUrl, String jsonString, final IBaseService listener, final Context context, String token,int method){
        this.url = getBaseUrl() + relativeUrl;
        this.mlistener = listener;
        this.mContext= context;
        this.mRequestMethod = method;
        final String mToken = token;
        JSONObject jsonObject = null;

        if(jsonString != null) {
            try {
                jsonObject = new JSONObject(jsonString);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                this.mRequestMethod, url,
                jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        jsonParse(response.toString());
                    }

                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Error jsonError = new Error();
                 NetworkResponse response = error.networkResponse;
                 if (error instanceof NoConnectionError) {
                    mlistener.onError("No connection error");
                }else if (error.networkResponse != null) {
                    String json = new String(response.data);
                    int statusCode = error.networkResponse.statusCode;
                    //jsonError = new Error(json);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if(response.statusCode == 401){
                           mlistener.onError(ApplicationRef.Service.INVALID_CODE);
                        }else {
                            mlistener.onError(jsonObject.getString(ApplicationRef.Service.STATUS_MSG));
                        }
                    }catch (JSONException e){
                        mlistener.onError(context.getResources().getString(R.string.message_oops_something_wrong));
                    }
                }else {
                    if (error.getClass().equals(TimeoutError.class)){
                        mlistener.onError(context.getResources().getString(R.string.service_timout_error));
                    }else{
                        mlistener.onError(context.getResources().getString(R.string.message_oops_something_wrong));
                    }
                }
            }
        })
        {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", mToken);
                return headers;
            }}
                ;

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);

    }


}
