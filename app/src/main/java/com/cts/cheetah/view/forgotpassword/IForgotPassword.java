package com.cts.cheetah.view.forgotpassword;

/**
 * Created by manu.palassery on 08-05-2017.
 */

public interface IForgotPassword {
    void onPasswordRecoverySuccess(String message);
    void onPasswordRecoveryError(String error);
}
