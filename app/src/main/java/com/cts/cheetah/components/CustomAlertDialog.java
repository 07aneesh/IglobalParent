package com.cts.cheetah.components;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cts.cheetah.R;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.interfaces.ICustomDialogDismiss;

/**
 * Created by manu.palassery on 28-03-2017.
 */

public class CustomAlertDialog {

    Context context;
    public ICustomDialogDismiss iCustomDialogDismiss;
    int _identifier=0;
    AlertDialog dialog = null;

    public CustomAlertDialog(Context context, ICustomDialogDismiss iCustomDialogDismiss){
        this.context = context;
        this.iCustomDialogDismiss = iCustomDialogDismiss;
    }

    public void showAlert(String title,String message,boolean cancelable,boolean showCancelButton){

        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context ,null));

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dialogView);

        TextView messageTitle = (TextView) dialogView.findViewById(R.id.messageTitle);
        messageTitle.setText(title);

        TextView messageText = (TextView) dialogView.findViewById(R.id.messageText);
        messageText.setText(message);

        Button okButton = (Button) dialogView.findViewById(R.id.messageOkBtn);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                iCustomDialogDismiss.onDialogDismiss(ApplicationRef.OK);
            }
        });

        Button cancelBtn = (Button) dialogView.findViewById(R.id.messageCancelBtn);
        if(showCancelButton) {
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    iCustomDialogDismiss.onDialogDismiss(ApplicationRef.CANCEL);
                }
            });
        }else{
            cancelBtn.setVisibility(View.INVISIBLE);
        }
        // Set the dialog title
       /* builder.setTitle(title)
                .setMessage(message)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        iCustomDialogDismiss.onDialogDismiss(ApplicationRef.OK);
                    }
                });

        if(showCancelButton){
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    iCustomDialogDismiss.onDialogDismiss(ApplicationRef.CANCEL);
                }
            });
        }*/

        //Cancelable or not
        builder.setCancelable(cancelable);

        dialog = builder.create();
        dialog.show();

    }
}
