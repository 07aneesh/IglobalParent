package com.cts.cheetah.view.forgotpassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cts.cheetah.R;
import com.cts.cheetah.components.LoaderDialogFragment;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.PreferenceManager;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.interfaces.IBaseView;
import com.cts.cheetah.view.login.LoginActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ForgotPasswordActivity extends AppCompatActivity implements IBaseView,IForgotPassword {

    ForgotPasswordController forgotPasswordController;
    LoaderDialogFragment loaderFragment;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        forgotPasswordController = new ForgotPasswordController(this,this);
        email = (EditText) findViewById(R.id.emailResetPassword);

        Button passwordResetButton = (Button) findViewById(R.id.passwordResetButton);
        passwordResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUserCredentilas();
            }
        });

        TextView backToLgin = (TextView) findViewById(R.id.backToLgin);
        backToLgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.forgotPasswordLayout).requestFocus();

        findViewById(R.id.forgotPasswordLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utility.hideSoftKeyboard(ForgotPasswordActivity.this);
                return false;
            }
        });
    }

    private void validateUserCredentilas() {
        String emailInput=email.getText().toString().trim();
        boolean result = Utility.validateEmail(this,emailInput);
        if(result){
            forgotPasswordController.startPasswordRecovery(emailInput);
        }else {
            Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onPasswordRecoverySuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        hideLoader();
        finish();
    }

    @Override
    public void onPasswordRecoveryError(String error) {
        onError(error);
    }

    @Override
    public void onError(String error) {
        hideLoader();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInvalidSession() {

    }

    @Override
    public void showLoader() {
        if (loaderFragment == null)
            loaderFragment = new LoaderDialogFragment();
        if(!loaderFragment.isVisible())
            loaderFragment.show(getSupportFragmentManager(), getString(R.string.login_loader_loaderTag));
    }

    @Override
    public void hideLoader() {
        try {
            if (loaderFragment != null){
                loaderFragment.dismiss();
            }
        } catch (IllegalStateException ie) {
            Utility.logger(ie);
        }
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
    }
}
