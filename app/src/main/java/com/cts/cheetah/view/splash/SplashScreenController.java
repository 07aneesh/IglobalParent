package com.cts.cheetah.view.splash;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.cts.cheetah.R;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.PreferenceManager;
import com.cts.cheetah.helpers.StatusCodeManager;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.interfaces.IBaseView;
import com.cts.cheetah.service.IBaseService;
import com.cts.cheetah.service.Service;
import com.cts.cheetah.service.ServiceForPhp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by manu.palassery on 09-05-2017.
 */

public class SplashScreenController implements IBaseService {

    Context context;
    IBaseView view;

    public SplashScreenController(Context context, IBaseView view){
        this.context = context;
        this.view = view;
    }

    public void getApplicationStatusCodes(){
        if (Utility.haveNetworkConnectivity(context)) {
            try {
                int method = com.android.volley.Request.Method.POST;
                ServiceForPhp servicePhp = new ServiceForPhp();
                servicePhp.sendJsonObject(ApplicationRef.Service.PHP_GET_APLN_STATUS_CODES,null,this,context,null,method);
            } catch (Exception e) {
                Utility.logger(e);
            }
        }else{
            ((IBaseView)view).onError(context.getString(R.string.service_error_noConnection));
        }
    }

    @Override
    public void onSuccess(JSONObject result) throws JSONException {
        StatusCodeManager.writeStatusCodeFile(context,result.getString("results").toString());
        ((ISpalshScreen)view).onGetStatusCodeSuccess();
    }

    @Override
    public void onError(String error) {

        ((IBaseView)view).onError(error);
    }
}
