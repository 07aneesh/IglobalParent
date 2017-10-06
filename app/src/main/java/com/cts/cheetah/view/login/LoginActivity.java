package com.cts.cheetah.view.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cts.cheetah.BuildConfig;
import com.cts.cheetah.R;
import com.cts.cheetah.components.LoaderDialogFragment;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.PreferenceManager;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.view.forgotpassword.ForgotPasswordActivity;
import com.cts.cheetah.view.main.MainDashboardActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    LoaderDialogFragment loaderFragment;
    PreferenceManager preferenceManager;
    int f=0;
    private LoginController mLoginController;
    Button loginButton;
    TextView textEmailAddress,textPassword;
    String fromIntent="";
    String refreshedToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        preferenceManager = PreferenceManager.getInstance(this);
        preferenceManager.setFirebaseId(refreshedToken);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            fromIntent = bundle.getString(ApplicationRef.FROM_INTENT);
        }

        if(fromIntent.equals("Dashboard")){
            overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
        }else{
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }

        preferenceManager = new PreferenceManager(LoginActivity.this);
        Context getBaseContext = getBaseContext();
        mLoginController = new LoginController(this, getBaseContext());

        textEmailAddress = (TextView) findViewById(R.id.email);
        textPassword = (TextView) findViewById(R.id.password);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent  = new Intent(LoginActivity.this, MainDashboardActivity.class);
                startActivity(intent);*/
               mLoginController.hideKeyboard(v);
               validateUserCredentilas();
            }
        });

        textPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    mLoginController.hideKeyboard(v);
                    validateUserCredentilas();
                    return true;
                }
                return false;
            }
        });


        TextView forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.activity_login).requestFocus();

        TextView versionNo = (TextView) findViewById(R.id.versionNo);
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        String version_No = BuildConfig.BUILD_NO;

        versionNo.setText(version_No);

        findViewById(R.id.activity_login).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utility.hideSoftKeyboard(LoginActivity.this);
                return false;
            }
        });
    }

    private void validateUserCredentilas() {
        try {
            String userName = textEmailAddress.getText().toString().trim();
            String password = textPassword.getText().toString().trim();
            String result = Utility.validateLoginDetails(this, userName, password);
            if (result.equalsIgnoreCase(ApplicationRef.TRUE)) {
                if ((loginButton.getText().toString()).equalsIgnoreCase(getString(R.string.login_label_forceLogOut))) {
                    mLoginController.forceLogOut(userName, password);
                } else {
                    mLoginController.login(userName, password);
                }
            } else {
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }


    }

    /*@Override
    public void onBackPressed() {
    }*/

    @Override
    public void hideLoader() {
        try {
            if (loaderFragment != null){
                loaderFragment.dismiss();
            }
        } catch (IllegalStateException ie) {
            Utility.logger(ie);
        }
    }


    @Override
    public void showLoader() {
        try {
            if (loaderFragment == null)
                loaderFragment = new LoaderDialogFragment();
            if (!loaderFragment.isVisible())
                loaderFragment.show(getSupportFragmentManager(), getString(R.string.login_loader_loaderTag));
        }catch (IllegalStateException e){
            Log.i("Loader Frag ill excpn",e+"");
        }catch (Exception e){
            Log.i("Loader Frag excpn",e+"");
        }
    }

    @Override
    public void onLoginSuccess() {
        try {
            loginButton.setEnabled(false);
            Intent intent = new Intent(LoginActivity.this,MainDashboardActivity.class);
            startActivity(intent);
            finish();

        } catch (Exception e) {
            Utility.logger(e);
        }
    }

    @Override
    public void onDuplicateLogin() {
        Toast.makeText(this,"Duplicate Login",Toast.LENGTH_LONG).show();
        hideLoader();
        loginButton.setText(R.string.login_label_forceLogOut);
    }

    @Override
    public void onForceLogOutSuccess() {
        hideLoader();
        loginButton.setText(R.string.login_label_signIn);
    }

    @Override
    public void onError(String error) {
        hideLoader();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInvalidSession() {
        hideLoader();
        Toast.makeText(this, R.string.login_wrong_credentails, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginController.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
