package com.cts.cheetah.view.gps;

import android.content.Context;
import android.util.Log;

import com.cts.cheetah.R;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.service.IBaseService;
import com.cts.cheetah.service.ServiceForPhp;
import com.cts.cheetah.view.accounts.IDriverAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by dell on 9/20/2017.
 */

public class GPSController implements IBaseService {

    private Context context;
    private IGPS _igps ;

    public GPSController(IGPS igps, Context context){
        this._igps = igps;
        this.context = context;
    }

    public void GetStudentDailyTrip() {
        try {
        int method = com.android.volley.Request.Method.POST;
        ServiceForPhp servicePhp = new ServiceForPhp();
        HashMap<String, String> params = new HashMap<>();
        String accessToken = Utility.getAccessToken(context);
        params.put("accessToken", accessToken);
        servicePhp.execute(ApplicationRef.Service.PHP_Student_DAILY_TRIP, method, params, this, context);
        } catch (Exception e) {
            Utility.logger(e);
        }
    }

    public void GetLatitudeAndLongitudeBasedOnTrip(int vehicleTripDetailsId)
    {
        try {
            int method = com.android.volley.Request.Method.POST;
            ServiceForPhp servicePhp = new ServiceForPhp();
            HashMap<String, String> params = new HashMap<>();
            String accessToken = Utility.getAccessToken(context);
            params.put("accessToken", accessToken);
            params.put("tripId", Integer.toString(vehicleTripDetailsId));
            servicePhp.execute(ApplicationRef.Service.PHP_Student_TRIP_GPS, method, params, this, context);
        } catch (Exception e) {
            Utility.logger(e);
        }
    }

    @Override
    public void onSuccess(JSONObject result) throws JSONException {
        try {
            if(result != null) {
                String api = result.getString("api");
                if(api.equalsIgnoreCase("getstudentdailytrip"))
                {
                    _igps.onStudentDailyTripData(result);
                }
                else if(api.equalsIgnoreCase("getstudenttriplatitudelongitude")){
                    _igps.onTripLatitudeLongitudeData(result);
                }
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onError(String error) {
        try {
            if(error != null) {
                _igps.onStudentDailyTripError(error);
            }else{
                _igps.onStudentDailyTripError(context.getResources().getString(R.string.message_oops_something_wrong));
            }
        }catch (Exception e){
            Log.i("",e+"");
        }
    }

}
