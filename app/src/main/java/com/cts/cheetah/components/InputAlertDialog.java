package com.cts.cheetah.components;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cts.cheetah.R;
import com.cts.cheetah.interfaces.IInputDialogDismiss;

/**
 * Created by manu.palassery on 28-03-2017.
 */

public class InputAlertDialog {
    Context context;
    public IInputDialogDismiss iInputDialogDismiss;

    public InputAlertDialog(Context context, IInputDialogDismiss iInputDialogDismiss){
        this.context = context;
        this.iInputDialogDismiss = iInputDialogDismiss;
    }


    public void showInputAlert(String title,String message){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setCancelable(false);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.input_alert_dialog, null);
        alertDialog.setView(dialogView);

        final TextView titleTv = (TextView) dialogView.findViewById(R.id.titleText);
        if(title != null) {
            titleTv.setText(title);
        }else{
            titleTv.setVisibility(View.INVISIBLE);
        }

        final TextView messageTv = (TextView) dialogView.findViewById(R.id.messageText);
        if(message != null) {
            messageTv.setText(message);
        }else{
            //messageTv.setVisibility(View.INVISIBLE);
        }



        final EditText input = (EditText) dialogView.findViewById(R.id.textInput);
        final Button okButton = (Button) dialogView.findViewById(R.id.okButton);
        final TextView cancelButton = (TextView) dialogView.findViewById(R.id.cancelButton);

        final AlertDialog inputAlertDialog = alertDialog.create();
        inputAlertDialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = (input.getText().toString().trim().isEmpty());
                // if EditText is empty disable closing on positive button
                if (!wantToCloseDialog) {
                    inputAlertDialog.dismiss();
                    iInputDialogDismiss.onInputDialogDismiss(input.getText().toString());
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputAlertDialog.dismiss();
            }
        });

    }


}
