package com.cts.cheetah.view.login;

import android.content.Context;

import com.android.volley.Request;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.interfaces.IBaseView;
import com.cts.cheetah.service.IBaseService;
import com.cts.cheetah.service.Service;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by Manu on 21/02/17.
 */

public class ForceLogOut implements IBaseService {
    private WeakReference<IBaseView> view;
    private Context context;

    public ForceLogOut(IBaseView view){
        this.view = new WeakReference<IBaseView>(view);
    }
    public void executeForceLogOut(String userName, String password, Context context){
        this.context = context;
        try {
            Service service = new Service();
            HashMap<String, String> params = new HashMap<>();
            params.put(ApplicationRef.Service.EMAIL_KEY, userName);
            params.put(ApplicationRef.Service.P_KEY, password);
            service.execute(ApplicationRef.Service.FORCE_LOGOUT_ACTION, Request.Method.POST,params,this,context);
        } catch (Exception e) {
            Utility.logger(e);
        }

    }
    @Override
    public void onSuccess(JSONObject result) throws JSONException {
        ( (ILoginView)view.get()).onForceLogOutSuccess();
    }

    @Override
        public void onError(String error) {
        ( (ILoginView)view.get()).onError(error);
        }
}
