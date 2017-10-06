package com.cts.cheetah.view.accounts;

import org.json.JSONObject;

/**
 * Created by manu.palassery on 20-04-2017.
 */

public interface IDriverAccount {
    void onDriverAccountData(JSONObject result);
    void onDriverAccountError(String error);
}
