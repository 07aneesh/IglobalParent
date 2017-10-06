package com.cts.cheetah.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cts.cheetah.R;
import com.cts.cheetah.interfaces.IBaseView;
import com.cts.cheetah.model.ICommonDataListener;
import com.cts.cheetah.model.IssueItem;
import com.cts.cheetah.service.IBaseService;
import com.cts.cheetah.service.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by manu.palassery on 12-07-2017.
 */

public class RejectReasonHelper implements IBaseService {

    Context context;
    ICommonDataListener iCommonDataListener;
    String api;

    public RejectReasonHelper(Context context) {
        this.context = context;
    }

    public void getRejectReason(String type,ICommonDataListener iCommonDataListener){
        this.iCommonDataListener = iCommonDataListener;
        this.api = ApplicationRef.Service.GET_ISSUE_REASON_LIST;
        if (Utility.haveNetworkConnectivity(context)) {
            try {
                int method = com.android.volley.Request.Method.POST;
                String jsonData=getIssueJson(type);
                Service service = new Service();
                service.sendJsonObject(ApplicationRef.Service.GET_ISSUE_REASON_LIST,jsonData,this,context,Utility.getAccessToken(context),method);
            } catch (Exception e) {
                Utility.logger(e);
            }
        } else {
            Toast.makeText(context,context.getString(R.string.service_error_noConnection),Toast.LENGTH_SHORT);
        }
    }

    private String getIssueJson(String type){
        String jsonString =  "{\"type\":\"" + type +  "\"}";
        return jsonString;
    }

    static public ArrayList<IssueItem> getRejectionListArray(JSONObject object){
        ArrayList<IssueItem> rejectionReasonsList = new ArrayList<>();
        try {
            if (object != null) {

                IssueItem issueItem;
                JSONArray reasonArray = object.getJSONArray("results");

                for (int i=0;i<reasonArray.length();i++){
                    JSONObject ro = new JSONObject(reasonArray.get(i).toString());
                    issueItem = new IssueItem();
                    issueItem.setId(ro.getInt("id"));
                    issueItem.setIssue(ro.getString("issue"));
                    rejectionReasonsList.add(issueItem);
                }
            }
        }catch (JSONException e){
            Log.i("",""+e);
        }
        return rejectionReasonsList;
    }

    static public ArrayList<String> getTempRejectionListArray(ArrayList<IssueItem> rejectionReasonsList){
        ArrayList<String> tempRejectionReasonsList = new ArrayList<>();
        for (int i=0;i<rejectionReasonsList.size();i++){
            tempRejectionReasonsList.add(rejectionReasonsList.get(i).getIssue());
        }
        return tempRejectionReasonsList;
    }

    static public ArrayList<String> getTempRejectionListArray(JSONObject object){
        ArrayList<IssueItem> rejectionReasonsList = getRejectionListArray(object);
        ArrayList<String> tempRejectionReasonsList = new ArrayList<>();
        for (int i=0;i<rejectionReasonsList.size();i++){
            tempRejectionReasonsList.add(rejectionReasonsList.get(i).getIssue());
        }
        return tempRejectionReasonsList;
    }

    @Override
    public void onSuccess(JSONObject result) throws JSONException {
        iCommonDataListener.onCommonDataSuccess(api,result);
    }

    @Override
    public void onError(String error) {
        iCommonDataListener.onCommonDataError(api,error);
    }

    public void sendRejectReason(ICommonDataListener iCommonDataListener,String tripId,String locationId,String locationType,String statusCode,String reason,String reasonId,String state,String latitude,String longitude){
        this.iCommonDataListener = iCommonDataListener;
        this.api = ApplicationRef.Service.UPDATE_MY_TRIP_STATUS;
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
}
