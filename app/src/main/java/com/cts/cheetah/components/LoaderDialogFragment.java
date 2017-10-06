package com.cts.cheetah.components;

/**
 * Created by vijay.thomas on 2/9/2016.
 */

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.cts.cheetah.R;


public class LoaderDialogFragment extends DialogFragment {



    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View v=inflater.inflate(R.layout.loader_dialog_fragmet_layout, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        return v;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }

}
