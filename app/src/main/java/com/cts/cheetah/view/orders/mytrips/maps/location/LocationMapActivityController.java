package com.cts.cheetah.view.orders.mytrips.maps.location;

import android.content.Context;
import android.widget.Toast;

import com.cts.cheetah.R;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.service.IBaseService;
import com.cts.cheetah.service.Service;
import com.cts.cheetah.view.orders.mytrips.maps.Route.IRouteMap;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by manu.palassery on 13-07-2017.
 */

public class LocationMapActivityController implements IBaseService {

    Context context;
    ILocationMap iLocationMap;

    public LocationMapActivityController(Context context, ILocationMap iLocationMap) {
        this.context = context;
        this.iLocationMap = iLocationMap;
    }


    public void sendRejectReason(String tripId, String locationId, String locationType, String statusCode, String reason, String reasonId, String state, String latitude, String longitude){
        if(statusCode != null) {
            if (Utility.haveNetworkConnectivity(context)) {
                try {
                    int method = com.android.volley.Request.Method.POST;
                    String jsonData=getTripAcceptJson(tripId,locationId, locationType,statusCode,reason,reasonId,state,latitude,longitude);
                    Service service = new Service();
                    service.sendJsonObject(ApplicationRef.Service.UPDATE_MY_TRIP_STATUS,jsonData,this,context,Utility.getAccessToken(context),method);
                } catch (Exception e) {
                    Utility.logger(e);
                }
            } else {
                Toast.makeText(context,context.getString(R.string.service_error_noConnection),Toast.LENGTH_SHORT);
            }
        }
    }

    private String getTripAcceptJson(String orderId,String locationId,String locationType,String statusCode,String reason,String reasonId,String state,String latitude,String longitude){
        String jsonString =  "{\"orderId\":"+ orderId +",\"locationId\":\" " + locationId + " \",\"locationType\":\" "+ locationType +" \",\"statusCode\":\""+statusCode+"\",\"reason\":\" " + reason + " \",\"reasonId\":\""+ reasonId +"\",\"state\":\""+ state +"\",\"latitude\":\" "+ latitude +" \",\"longitude\":\" "+longitude+" \"}";
        return jsonString;
    }

    @Override
    public void onSuccess(JSONObject result) throws JSONException {
        iLocationMap.onSendIssueSuccess(result);
    }

    @Override
    public void onError(String error) {
        iLocationMap.onSendIssueError(error);
    }
}
