package com.cts.cheetah.view.login;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.android.volley.Request;
import com.cts.cheetah.BuildConfig;
import com.cts.cheetah.R;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.PreferenceManager;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.interfaces.IBaseView;
import com.cts.cheetah.service.IBaseService;
import com.cts.cheetah.service.Service;
import com.cts.cheetah.service.ServiceForPhp;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class LoginController implements IBaseService,ILoginController {
    private WeakReference<IBaseView> view;
    private Context context;
    int f = 0;

    public LoginController(IBaseView view, Context context){
        this.view= new WeakReference<IBaseView>(view);
        this.context=context;

    }

    public void login(String userName, String password){
            if (Utility.haveNetworkConnectivity(context)) {
                callLoginService(userName,password,PreferenceManager.getInstance(context).getAccessToken());
            } else {
                ((ILoginView)view.get()).onError(context.getString(R.string.service_error_noConnection));
            }
    }

    private void callLoginService(String userName, String password,String refreshedToken){
        ((ILoginView)view.get()).showLoader();
        try {
            int method = com.android.volley.Request.Method.POST;
           // String clientData=getClientDataForLogin(userName,password,refreshedToken);
            //Service service = new Service();
            //servicePhp.sendJsonObject(ApplicationRef.Service.PHP_LOGIN_ACTION,clientData,this,context,null,method);
            String clientData = null;
            ServiceForPhp servicePhp = new ServiceForPhp();
            HashMap<String, String> loginParams = new HashMap<>();
            loginParams.put("username", userName);
            loginParams.put("password", password);
            //execute(String relativeUrl, int requestMethod , final HashMap<String, String> params , final IBaseService listener, Context context ) throws JSONException {
            servicePhp.execute(ApplicationRef.Service.PHP_LOGIN_ACTION, method, loginParams, this, context);
        } catch (Exception e) {
            Utility.logger(e);
        }
    }

    @Override
    public void duplicateLogin() {
        ((ILoginView)view.get()).onDuplicateLogin();
    }


    public void hideKeyboard(View v) {
        // TODO Auto-generated method stub
        InputMethodManager manager = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null)
            manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    @Override
    public void onSuccess(JSONObject result) throws JSONException {
        try {
            ((ILoginView) view.get()).onLoginSuccess();
            PreferenceManager preferenceManager = new PreferenceManager(context);
            preferenceManager.setUserPreferences(result.getJSONObject("results"));
        }catch (Exception e){

        }
    }

    @Override
    public void onError(String error) {
        try {
            if(error.equals(ApplicationRef.Service.INVALID_CODE)){
                ((ILoginView)view.get()).onInvalidSession();
            }else {
                ((ILoginView)view.get()).onError(error);
            }
        }catch (Exception e){

        }
    }

    public void forceLogOut(String userName, String password){
        try {
            String valid = Utility.validateLoginDetails(context, userName, password);
            if (valid.equals("true")) {
                ((ILoginView) view.get()).showLoader();
                ForceLogOut forcelogOut = new ForceLogOut(view.get());
                forcelogOut.executeForceLogOut(userName, password, context);
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onDestroy() {
        this.context=null;
        this.view=null;
    }

    private String getClientDataForLogin(String userName,String password,String pushConfigId){
        String clientData = null;
        try {
            clientData = "{" +
                    "  \"client\": {" +
                    "    \"Id\": \"" + BuildConfig.CLIENT_ID + "\"," +
                    "    \"secret\": \"" + BuildConfig.CLIENT_SECRET_ID + "\"" +
                    "  }," +
                    "  \"account\": {" +
                    "    \"email\": \"" + userName + "\"," +
                    "    \"password\": \"" + password + "\"" +
                    "  }," +
                    "  \"pushConfig\": {" +
                    "    \"platformDeviceId\": \"" + pushConfigId + "\"," +
                    "    \"platformOS\": \"" + BuildConfig.PLATFORM_OS + "\"" +
                    "  }\n" +
                    "}";
        }catch (Exception e){

        }

        return clientData;
    }
}

