package com.cts.cheetah.interfaces;

/**
 * Created by fts012 on 31/03/16.
 */
public interface IBaseView {
    void onError(String error);
    void onInvalidSession();
    void showLoader();
    void hideLoader();

}
