package com.cts.cheetah.view.splash;

import android.content.ContentResolver;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cts.cheetah.R;
import com.cts.cheetah.components.CustomAlertDialog;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.PreferenceManager;
import com.cts.cheetah.helpers.StatusCodeManager;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.interfaces.IBaseView;
import com.cts.cheetah.interfaces.ICustomDialogDismiss;
import com.cts.cheetah.view.login.LoginActivity;
import com.cts.cheetah.view.main.MainDashboardActivity;
import com.google.firebase.iid.FirebaseInstanceId;

public class SplashScreenActivity extends AppCompatActivity implements ICustomDialogDismiss,IBaseView,ISpalshScreen {

    final private static int GPS_REQUEST_CODE = 1;
    SplashScreenController splashScreenController;
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashScreenController = new SplashScreenController(this,this);
        preferenceManager = PreferenceManager.getInstance(this);
        preferenceManager.setAccessToken("");//by an
        StatusCodeManager.writeStatusCodeFile(this,"");
        checkGPSStatus();
    }


    private void checkGPSStatus(){
        try {
            final LocationManager manager = (LocationManager) getSystemService(SplashScreenActivity.this.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                CustomAlertDialog customAlertDialog = new CustomAlertDialog(this, this);
                customAlertDialog.showAlert(getString(R.string.splash_gps_dialog_title), getString(R.string.splash_gps_dialog_message), false, false);
            } else {
                checkAppStatusCodesDownloaded();
            }
        }catch (Exception e){

        }
    }


    /**
     * checkAppStatusCodesDownloaded() checks that status code file and values are there. if not it's API is called,
     * Otherwise next activity is loaded.
     */
    private void checkAppStatusCodesDownloaded(){
        try {

//An
            if (StatusCodeManager.getStatusCode(this, ApplicationRef.StatusCodes.DRIVER_STATUS, ApplicationRef.StatusCodes.DRIVER_AVAILABILITY_ONLINE) == null) {
                splashScreenController.getApplicationStatusCodes();
            } else {
                loadNextActivity();
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInvalidSession() {
        Utility.handleInvalidSession(this);
    }

    @Override
    public void showLoader() {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void onGetStatusCodeSuccess() {
        loadNextActivity();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == GPS_REQUEST_CODE && resultCode == 0){
            ContentResolver cr = getApplicationContext().getContentResolver();
            int locationMode = 0;
            try {
                locationMode = Settings.Secure.getInt(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            if(locationMode == 0){
                try {
                    Snackbar.make(this.getCurrentFocus(), "Enable GPS to continue", Snackbar.LENGTH_SHORT);
                }catch (Exception e){

                }
            }else{
                //loadNextActivity();
                splashScreenController.getApplicationStatusCodes();

            }

        }
    }

    private void loadNextActivity(){
        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            }
        }, 2000);*/

        if(preferenceManager.getAccessToken().equals("")){
            //If there is no access token stored, load Login Activity.
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            //If there is access token stored, load MainDashboard Activity.
            Intent intent = new Intent(SplashScreenActivity.this, MainDashboardActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onDialogDismiss(String result) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, GPS_REQUEST_CODE);
    }
}
