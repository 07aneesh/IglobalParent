package com.cts.cheetah.helpers;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.cts.cheetah.R;
import com.cts.cheetah.model.Document;

import io.fabric.sdk.android.Fabric;
import java.lang.reflect.Field;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

/**
 * Created by manu.palassery on 05-04-2017.
 */

public class ApplicationClass extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
