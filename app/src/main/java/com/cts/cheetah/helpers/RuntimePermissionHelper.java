package com.cts.cheetah.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.cts.cheetah.R;
import com.cts.cheetah.interfaces.IPermissionCheck;

/**
 * Created by manu.palassery on 08-06-2017.
 */

public class RuntimePermissionHelper extends FragmentActivity {

    Activity activity;
    View mLayout;
    public int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;
    public int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION=2;
    IPermissionCheck iPermissionCheck;

    public RuntimePermissionHelper(Activity activity, View mLayout, IPermissionCheck iPermissionCheck){
        this.activity = activity;
        this.mLayout = mLayout;
        this.iPermissionCheck = iPermissionCheck;
    }


    //LOCATION PERMISSION
    public void checkLocationPermission(){

        if (ContextCompat.checkSelfPermission( activity.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( activity.getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Register the listener with the Location Manager to receive location updates
            Toast.makeText(activity.getApplicationContext(),"Permission Required",Toast.LENGTH_SHORT).show();
            askLocationPermission();
        }else{
            iPermissionCheck.onPermissionResult(true);
        }
    }

    private void askLocationPermission(){
        try {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Provide an additional rationale to the user if the permission was not granted
                // and the user would benefit from additional context for the use of the permission.
                // For example if the user has previously denied the permission.

                Snackbar.make(mLayout, activity.getString(R.string.request_fineLocation_message),
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                            }
                        }).show();
            }else {
                ActivityCompat.requestPermissions(activity,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION },
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } catch (Exception e) {
            Utility.logger(e);
        }

    }

    @Override
    public void onRequestPermissionsResult (int requestCode,String[] permissions,int[] grantResults){
        if(permissions[0].equals("android.permission.ACCESS_FINE_LOCATION") && grantResults[0] == 0){
            try {
                iPermissionCheck.onPermissionResult(true);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }else{
            iPermissionCheck.onPermissionResult(false);
        }
    }
    //----------------------------------------------------------------------------------------------
}
