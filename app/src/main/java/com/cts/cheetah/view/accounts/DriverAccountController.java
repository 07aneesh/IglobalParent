package com.cts.cheetah.view.accounts;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.cts.cheetah.R;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.service.IBaseService;
import com.cts.cheetah.service.Service;
import com.cts.cheetah.service.ServiceForPhp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by manu.palassery on 20-04-2017.
 */

public class DriverAccountController implements IBaseService {

    private IDriverAccount iDriverAccount ;
    private Context context;

    public DriverAccountController(IDriverAccount iDriverAccount, Context context){
        this.iDriverAccount= iDriverAccount;
        this.context=context;
    }

    public void getDriverAccountData(){
        if (Utility.haveNetworkConnectivity(context)) {
            try {

                int method = com.android.volley.Request.Method.POST;
                //Service service = new Service();
                //service.sendJsonObject(ApplicationRef.Service.GET_DRIVER_ACCOUNT,null,this,context,Utility.getAccessToken(context),method);
                ServiceForPhp servicePhp = new ServiceForPhp();
                HashMap<String, String> params = new HashMap<>();
                String accessToken = Utility.getAccessToken(context);
                params.put("accessToken", accessToken);
                servicePhp.execute(ApplicationRef.Service.PHP_Student_Account_Details, method, params, this, context);
            } catch (Exception e) {
                Utility.logger(e);
            }
        } else {
            Toast.makeText(context,context.getString(R.string.service_error_noConnection),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess(JSONObject result) throws JSONException {
        try {
            if(result != null) {
                iDriverAccount.onDriverAccountData(result);
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onError(String error) {
        try {
            if(error != null) {
                iDriverAccount.onDriverAccountError(error);
            }else{
                iDriverAccount.onDriverAccountError(context.getResources().getString(R.string.message_oops_something_wrong));
            }
        }catch (Exception e){
            Log.i("",e+"");
        }
    }
}
