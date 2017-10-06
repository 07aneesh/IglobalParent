package com.cts.cheetah.view.forgotpassword;

import android.content.Context;

import com.cts.cheetah.BuildConfig;
import com.cts.cheetah.R;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.PreferenceManager;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.interfaces.IBaseView;
import com.cts.cheetah.service.IBaseService;
import com.cts.cheetah.service.Service;
import com.cts.cheetah.view.login.ILoginView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by manu.palassery on 08-05-2017.
 */

public class ForgotPasswordController implements IBaseService {

    Context context;
    IBaseView view;

    public ForgotPasswordController(Context context, IBaseView view){
        this.context = context;
        this.view = view;
    }

    @Override
    public void onSuccess(JSONObject result) throws JSONException {
        String statusMessge = "Success";
        try{
            statusMessge = result.getString(ApplicationRef.Service.STATUS_MSG);
        }catch (JSONException e){

        }
        ((IForgotPassword)view).onPasswordRecoverySuccess(statusMessge);
    }

    @Override
    public void onError(String error) {
        ((IForgotPassword)view).onPasswordRecoveryError(error);
    }

    public void startPasswordRecovery(String email){
        if (Utility.haveNetworkConnectivity(context)) {
            ((IBaseView)view).showLoader();
            try {
                int method = com.android.volley.Request.Method.POST;
                String recoveryCallData=getDataForRecovery(email);
                Service service = new Service();
                service.sendJsonObject(ApplicationRef.Service.FORGOT_PASSWORD,recoveryCallData,this,context,null,method);
            } catch (Exception e) {
                Utility.logger(e);
            }
        } else {
            ((IBaseView)view).onError(context.getString(R.string.service_error_noConnection));
        }
    }


    private String getDataForRecovery(String email){
        String data = "{" +
                "  \"client\": {" +
                "    \"Id\": \""+ BuildConfig.CLIENT_ID +"\"," +
                "    \"secret\": \""+ BuildConfig.CLIENT_SECRET_ID +"\"" +
                "}," +
                "\"account\": {" +
                "\"email\": \"" + email + "\"" +
                "}" +
                "}";
        return data;
    }
}
