package com.cts.cheetah.helpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cts.cheetah.R;

/**
 * Created by manu.palassery on 05-04-2017.
 */

public class CustomFontHelper {

    public static String OSWALD = "oswald";
    public static String OPEN_SANS = "opensans";
    public static String OSWALD_BOLD = "oswald_bold";
    public static String OPEN_SANS_BOLD = "opensans_bold";

    public CustomFontHelper(){

    }

    public static void findViews(Context context, View v ,String fontName){
        try {
            Typeface typeface=getCustomTypeface(context,fontName);

            if(typeface != null) {
                if (v instanceof ViewGroup) {
                    ViewGroup vg = (ViewGroup) v;
                    for (int i = 0; i < vg.getChildCount(); i++) {
                        View child = vg.getChildAt(i);
                        //you can recursively call this method
                        findViews(context, child, fontName);
                    }
                } else if (v instanceof TextView) {
                    ((TextView) v).setTypeface(typeface);
                    Log.i("TV",""+((TextView) v).getText());
                 /*   Typeface style = ((TextView) v).getTypeface();
                int[] attrs = {android.R.attr.fontFamily};
                TypedArray ta = context.obtainStyledAttributes(style.getStyle(), attrs);
                String font = ta.getString(0);*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Typeface getCustomTypeface(Context context,String fontName){
        Typeface typeface=null;
        if(fontName.equals(OSWALD)) {
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Oswald-Regular.ttf");
        }else if(fontName.equals(OSWALD_BOLD)){
            typeface=Typeface.createFromAsset(context.getAssets(), "fonts/Oswald-Bold.ttf");
        } if(fontName.equals(OPEN_SANS)) {
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Oswald-Regular.ttf");
        } if(fontName.equals(OPEN_SANS_BOLD)) {
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Oswald-Bold.ttf");
        }

        return typeface;
    }
}
