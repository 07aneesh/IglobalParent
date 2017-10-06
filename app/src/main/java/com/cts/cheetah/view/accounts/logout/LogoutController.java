package com.cts.cheetah.view.accounts.logout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.cts.cheetah.R;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.PreferenceManager;
import com.cts.cheetah.helpers.StatusCodeManager;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.interfaces.IBaseView;
import com.cts.cheetah.service.IBaseService;
import com.cts.cheetah.service.Service;
import com.cts.cheetah.view.accounts.IDriverAccount;
import com.cts.cheetah.view.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by manu.palassery on 31-03-2017.
 */

public class LogoutController implements IBaseService {

    Context context;
    IDriverAccount iDriverAccount;
    String type;

    public LogoutController()
    {
    }

    public LogoutController(Context context,IDriverAccount iDriverAccount){
        this.iDriverAccount = iDriverAccount;
        this.context = context;
    }

    public void logout(){

        try {
            Service service = new Service();
            int method = com.android.volley.Request.Method.POST;
            PreferenceManager preferenceManager = new PreferenceManager(context);
            String token = ApplicationRef.Service.BEARER + preferenceManager.getAccessToken();
                    service.sendJsonObject(ApplicationRef.Service.LOGOUT_ACTION,null,this,context,token,method);
        } catch (Exception e) {
            Utility.logger(e);
        }
    }

    public void setAvailibilityAsOffline(){
        type="availability";
        if (Utility.haveNetworkConnectivity(context)) {
            try {
                int method = com.android.volley.Request.Method.POST;
                Service service = new Service();
                service.sendJsonObject(ApplicationRef.Service.SET_DRIVER_AVAILABILITY,getStatusJson(),this,context,Utility.getAccessToken(context),method);
            } catch (Exception e) {
                Utility.logger(e);
            }
        } else {

        }
    }

    private String getStatusJson(){
        String jsonString = null;
        String availabilityStatusCode = StatusCodeManager.getStatusCode(context,ApplicationRef.StatusCodes.DRIVER_STATUS,ApplicationRef.StatusCodes.DRIVER_AVAILABILITY_OFFLINE);
        try {
            jsonString = "{\"availabilityCode\": \"" + availabilityStatusCode + "\" }";
        }catch (Exception e){

        }
        return jsonString;
    }

    @Override
    public void onSuccess(JSONObject result) throws JSONException {
        String api = result.getString(ApplicationRef.Service.API);
        switch(api){
            case ApplicationRef.Service.SET_DRIVER_AVAILABILITY:
                logout();
                break;
            case ApplicationRef.Service.LOGOUT_ACTION:
                Utility.handleInvalidSession(context);
                Toast.makeText(context,context.getResources().getString(R.string.logout_success),Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onError(String error) {
        if(error.equals(ApplicationRef.Service.INVALID_CODE)){
            Toast.makeText(context,context.getResources().getString(R.string.logout_success),Toast.LENGTH_SHORT).show();
            Utility.handleInvalidSession(context);
        }else{
            //Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
            iDriverAccount.onDriverAccountError(error);
        }
    }

}
