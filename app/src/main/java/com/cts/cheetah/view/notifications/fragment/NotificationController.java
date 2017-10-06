package com.cts.cheetah.view.notifications.fragment;

import android.content.Context;

import com.cts.cheetah.R;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.interfaces.IBaseView;
import com.cts.cheetah.model.Notification;
import com.cts.cheetah.service.IBaseService;
import com.cts.cheetah.service.Service;
import com.cts.cheetah.view.accounts.IDriverAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by manu.palassery on 12-05-2017.
 */

public class NotificationController implements IBaseService {

    Context context;
    private INotification view;

    public NotificationController(INotification view, Context context){
        this.context = context;
        this.view= view;
    }

    public void getNotifications(){
        if (Utility.haveNetworkConnectivity(context)) {
            try {
                int method = com.android.volley.Request.Method.POST;
                Service service = new Service();
                service.sendJsonObject(ApplicationRef.Service.GET_PUSH_NOTIFICATIONS,null,this,context,Utility.getAccessToken(context),method);
            } catch (Exception e) {
                Utility.logger(e);
            }
        } else {
            view.onNotificationError(context.getString(R.string.service_error_noConnection));
        }
    }


    @Override
    public void onSuccess(JSONObject result) throws JSONException {
        view.onNotificationSuccess(result);
    }

    @Override
    public void onError(String error) {
        view.onNotificationError(error);
    }

}
