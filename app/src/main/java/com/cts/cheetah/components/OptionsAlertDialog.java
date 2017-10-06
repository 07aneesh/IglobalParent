package com.cts.cheetah.components;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import com.cts.cheetah.R;
import com.cts.cheetah.interfaces.IOptionsDialogDismiss;

/**
 * Created by manu.palassery on 28-03-2017.
 */

public class OptionsAlertDialog {

    Context context;
    public IOptionsDialogDismiss iOptionsDialogDismiss;
    int _identifier=0;

    public OptionsAlertDialog(Context context, IOptionsDialogDismiss iOptionsDialogDismiss){
        this.context = context;
        this.iOptionsDialogDismiss = iOptionsDialogDismiss;
    }

    public void showOptionsAlert(String title,String options[],boolean cancelable,int identifier){
        this._identifier = identifier;
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context ,null));
        // Set the dialog title
        builder.setTitle(title)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        iOptionsDialogDismiss.onOptionsDismiss(_identifier,which,"");
                    }
                })
                // Set the action buttons
                .setNegativeButton(R.string.trip_label_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        //Cancelable or not
        builder.setCancelable(cancelable);

        //builder.show();

        final AlertDialog inputAlertDialog = builder.create();
        inputAlertDialog.show();

        Button okBtn = inputAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        okBtn.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
    }

    public int mPosition = -1;
    public void showRadioOptionsDialog(String title, final String options[], boolean cancelable, int identifier){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_options_dialog);
        dialog.setCancelable(cancelable);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int height = (int)(displayMetrics.heightPixels*0.5);
        if(height>700){
            height = 700;
        }
        dialog.getWindow().setLayout((int)(displayMetrics.widthPixels*0.9),height);

        final EditText optionReason = (EditText) dialog.findViewById(R.id.optionReason);

        final ListView listView = (ListView) dialog.findViewById(R.id.dialogOptionsList);
        final OptionsListAdapter optionsListAdapter = new OptionsListAdapter(context,options);
        listView.setAdapter(optionsListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == options.length-1){
                    optionReason.setVisibility(View.VISIBLE);
                }else{
                    optionReason.setVisibility(View.GONE);
                }

                optionsListAdapter.setSelection(position,view,parent);
                mPosition = position;
            }
        });
        ((Button) dialog.findViewById(R.id.okButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reasonText="";
                if(mPosition != -1) {
                    if (mPosition == options.length - 1) {
                        reasonText = optionReason.getText().toString();
                    } else {
                        reasonText = options[mPosition];
                    }

                    iOptionsDialogDismiss.onOptionsDismiss(_identifier, mPosition, reasonText);
                    dialog.cancel();
                }
            }
        });

        ((Button) dialog.findViewById(R.id.cancelButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });


        /*WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        dialog.getWindow().setLayout((int)(displayMetrics.heightPixels*0.6),lp.height );*/



        dialog.show();
        Log.i("CB Dialog",(int)(displayMetrics.widthPixels*0.9)+" w/h "+(int)(displayMetrics.heightPixels*0.45));
        Log.i("CB Dialog xy",(int)(displayMetrics.xdpi*0.9)+" w/h "+(int)(displayMetrics.ydpi*0.45));
        getScreenSize();
    }


    public class OptionsListAdapter extends BaseAdapter {

        Context context;
        String options[]=null;
        LayoutInflater inflater;
        RadioButton selectedCheckBox;
        int selectedIndex = -1;

        public OptionsListAdapter(Context context, String options[]){
            this.context = context;
            this.options = options;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return options.length;
        }

        @Override
        public Object getItem(int position) {
            return options[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            View rowView = convertView;
            // reuse views
            if (rowView == null) {
                rowView = inflater.inflate(R.layout.custom_option_item, null);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.optionItem = (RadioButton) rowView.findViewById(R.id.custom_option_radio);

                rowView.setTag(viewHolder);
            }

            // fill data
            final ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.optionItem.setText(options[position]);
            if(position == selectedIndex){
                holder.optionItem.setChecked(true);
            }else{
                holder.optionItem.setChecked(false);
            }

            holder.optionItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedCheckBox != null) {
                        selectedCheckBox.setChecked(false);
                    }
                    selectedCheckBox = holder.optionItem;
                    selectedIndex = position;
                }
            });

            holder.optionItem.setFocusable(false);
            holder.optionItem.setClickable(false);
            return rowView;
        }

        class ViewHolder {
            public RadioButton optionItem;

        }

        public void setSelection(final int position, View convertView, final ViewGroup parent){
            if(selectedCheckBox != null) {
                selectedCheckBox.setChecked(false);
            }
            ViewHolder obj = (ViewHolder) getView(position,convertView,parent).getTag();
            obj.optionItem.setChecked(true);
            selectedCheckBox = obj.optionItem;
            selectedIndex = position;
        }

    }

    private void getScreenSize(){

        final DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels; //320 dip
        int height = dm.heightPixels; //533 dip
        Log.i("WMangr",width + "w/h" + height);

        int widthPix = (int) Math.ceil(dm.widthPixels * (dm.density));
        int heightPix = (int) Math.ceil(dm.heightPixels * (dm.density));
        Log.i("WMangr widthPix",widthPix + " wp/hp " + heightPix);

    }

}
