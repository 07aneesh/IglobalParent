package com.cts.cheetah.view.login;


import com.cts.cheetah.interfaces.IBaseView;

/**
 * Created by fts012 on 22/11/16.
 */

public interface ILoginView extends IBaseView {
    void onLoginSuccess();
    void onDuplicateLogin();
    void onForceLogOutSuccess();
    void showLoader();
    void hideLoader();
}