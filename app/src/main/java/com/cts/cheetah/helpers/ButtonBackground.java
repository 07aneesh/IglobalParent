package com.cts.cheetah.helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.view.View;

/**
 * Created by manu.palassery on 28-04-2017.
 */

public class ButtonBackground {
    public static void setBackground(View v, int cornerRadius, int backgroundColor){
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[] { cornerRadius, cornerRadius, cornerRadius, cornerRadius, 0, 0, 0, 0 });
        shape.setColor(backgroundColor);
        //shape.setStroke(borderRdius, borderColor);
        v.setBackground(shape);
    }
}
