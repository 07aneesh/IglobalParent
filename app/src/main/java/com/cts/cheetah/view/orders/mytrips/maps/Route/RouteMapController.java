package com.cts.cheetah.view.orders.mytrips.maps.Route;

import android.content.Context;
import android.widget.Toast;

import com.cts.cheetah.R;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.interfaces.IBaseView;
import com.cts.cheetah.service.IBaseService;
import com.cts.cheetah.service.Service;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by manu.palassery on 13-07-2017.
 */

public class RouteMapController implements IBaseService {

    Context context;
    IRouteMap iRouteMap;

    public RouteMapController(Context context,IRouteMap iRouteMap) {
        this.context = context;
        this.iRouteMap = iRouteMap;
    }

    public void getMyTripsData(String orderId){
        if (Utility.haveNetworkConnectivity(context)) {
            try {
                Service service = new Service();
                int method = com.android.volley.Request.Method.POST;
                String jsonString = "{ \"orderId\":\""+ orderId +"\" }";
                service.sendJsonObject(ApplicationRef.Service.GET_MY_TRIP_SINGLE,jsonString,this,context,Utility.getAccessToken(context),method);
            } catch (Exception e) {
                Utility.logger(e);
            }
        } else {
            Toast.makeText(context,context.getString(R.string.service_error_noConnection),Toast.LENGTH_SHORT).show();
        }
    }

    public void sendIssueOrStatus(String tripId, String locationId, String locationType, String statusCode, String reason, String reasonId, String state, String latitude, String longitude){
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
                Toast.makeText(context,context.getString(R.string.service_error_noConnection),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getTripAcceptJson(String orderId,String locationId,String locationType,String statusCode,String reason,String reasonId,String state,String latitude,String longitude){
        String jsonString =  "{\"orderId\":"+ orderId +",\"locationId\":\" " + locationId + " \",\"locationType\":\" "+ locationType +" \",\"statusCode\":\""+statusCode+"\",\"reason\":\" " + reason + " \",\"reasonId\":\""+ reasonId +"\",\"state\":\""+ state +"\",\"latitude\":\" "+ latitude +" \",\"longitude\":\" "+longitude+" \"}";
        return jsonString;
    }

    @Override
    public void onSuccess(JSONObject result) throws JSONException {
        if(result != null){
            String api = result.getString(ApplicationRef.Service.API);
            switch(api){
                case ApplicationRef.Service.GET_MY_TRIP_SINGLE:
                    iRouteMap.onTripDataSuccess(result);
                    break;
                case ApplicationRef.Service.UPDATE_MY_TRIP_STATUS:
                    iRouteMap.onIssueOrStatusSuccess(result);
                    break;
            }

        }
    }

    @Override
    public void onError(String error) {
        iRouteMap.onIssueOrStatusError(error);
    }
}
