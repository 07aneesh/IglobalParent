package com.cts.cheetah.view.login;


import com.cts.cheetah.interfaces.IBaseController;

/**
 * Created by fts012 on 22/11/16.
 */

public interface ILoginController extends IBaseController{
        void login(String userName, String password);
        void duplicateLogin();

}
