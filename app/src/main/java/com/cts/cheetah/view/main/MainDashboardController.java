package com.cts.cheetah.view.main;

import android.content.Context;

import com.cts.cheetah.R;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.PreferenceManager;
import com.cts.cheetah.helpers.StatusCodeManager;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.interfaces.IBaseView;
import com.cts.cheetah.service.IBaseService;
import com.cts.cheetah.service.Service;
import com.cts.cheetah.view.login.ILoginView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by manu.palassery on 10-05-2017.
 */

public class MainDashboardController implements IBaseService {

    Context context;
    IBaseView view;

    public MainDashboardController(Context context,IBaseView view){
        this.context = context;
        this.view = view;
    }

    public void setAvailibilityStatus(){
        if (Utility.haveNetworkConnectivity(context)) {
            ((IBaseView)view).showLoader();
            try {
                int method = com.android.volley.Request.Method.POST;
                Service service = new Service();
                service.sendJsonObject(ApplicationRef.Service.SET_DRIVER_AVAILABILITY,getStatusJson(),this,context,Utility.getAccessToken(context),method);
            } catch (Exception e) {
                Utility.logger(e);
            }
        } else {
            ((IBaseView)view).onError(context.getString(R.string.service_error_noConnection));
        }
    }

    @Override
    public void onSuccess(JSONObject result) throws JSONException {

        ((IMainDashboard)view).onAvailabilityStatusSuccess(result);
    }

    @Override
    public void onError(String error) {
        ((IMainDashboard)view).onAvailabilityStatusError(error);
    }

    private String getStatusJson(){
        String jsonString = null;
        String availabilityStatus = "";
        String availabilityStatusCode = PreferenceManager.getInstance(context).getDriverAvailabilityStatus();
        availabilityStatus = StatusCodeManager.getStatusName(context,ApplicationRef.StatusCodes.DRIVER_STATUS,availabilityStatusCode);

        if(availabilityStatus.equals(ApplicationRef.StatusCodes.DRIVER_AVAILABILITY_ONLINE)){
            availabilityStatusCode = StatusCodeManager.getStatusCode(context,ApplicationRef.StatusCodes.DRIVER_STATUS,ApplicationRef.StatusCodes.DRIVER_AVAILABILITY_OFFLINE);
        }else if(availabilityStatus.equals(ApplicationRef.StatusCodes.DRIVER_AVAILABILITY_OFFLINE)){
            availabilityStatusCode = StatusCodeManager.getStatusCode(context,ApplicationRef.StatusCodes.DRIVER_STATUS,ApplicationRef.StatusCodes.DRIVER_AVAILABILITY_ONLINE);
        }

        try {
            jsonString = "{\"availabilityCode\": \"" + availabilityStatusCode + "\" }";
        }catch (Exception e){

        }
        return jsonString;
    }

    public boolean isDriverOnline(){
        boolean isOnline = true;
        String availabilityStatusCode = PreferenceManager.getInstance(context).getDriverAvailabilityStatus();
        String availabilityStatus = StatusCodeManager.getStatusName(context,ApplicationRef.StatusCodes.DRIVER_STATUS,availabilityStatusCode);

        if(availabilityStatus.equals(ApplicationRef.StatusCodes.DRIVER_AVAILABILITY_ONLINE)){
            isOnline = true;
        }else if(availabilityStatus.equals(ApplicationRef.StatusCodes.DRIVER_AVAILABILITY_OFFLINE)){
            isOnline = false;
        }

        return isOnline;
    }
}
