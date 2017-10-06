package com.cts.cheetah.components;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.cts.cheetah.R;

import java.util.Calendar;

/**
 * Created by manu on 6/13/2016.
 */
public class CustomDatePicker extends DialogFragment {
    Context context;
    Calendar cal;
    android.app.DatePickerDialog.OnDateSetListener listener;

    public CustomDatePicker(){

    }

    /**
     *
     * @param context
     * @param c
     * @param listener
     * set context and listener
     */
    public void setContext(Context context, Calendar c, android.app.DatePickerDialog.OnDateSetListener listener){

        this.context = context;
        cal = c;
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the last date as the default date in the picker
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        //Long time = cal.getTimeInMillis();
        android.app.DatePickerDialog dp;

        //Apply style to versions from LOLIPOP
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            dp = new android.app.DatePickerDialog(this.context,listener,year, month, day);
        } else {
            dp = new android.app.DatePickerDialog(this.context, R.style.DialogTheme,listener,year, month, day);
        }

        dp.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);//This will set minimum date.
        return dp;
    }

}
