package com.cts.cheetah.components;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.cts.cheetah.R;
import com.cts.cheetah.interfaces.ICheckboxDialogDismiss;

import java.util.ArrayList;


/**
 * Created by manu.palassery on 28-03-2017.
 */

public class CheckBoxAlertDialog {

    Context context;
    public ICheckboxDialogDismiss iCheckboxDialogDismiss;
    ArrayList<Integer> selectedPositionArray = new ArrayList<>();
    ListView listView;
    CheckBoxListAdapter optionsListAdapter;

    public CheckBoxAlertDialog(Context context, ICheckboxDialogDismiss iOptionsDialogDismiss){
        this.context = context;
        this.iCheckboxDialogDismiss = iOptionsDialogDismiss;
    }


    public int mPosition = -1;
    public void showDialog(String title, final String options[], boolean cancelable, ArrayList<Integer> selectedPositions){
        this.selectedPositionArray = selectedPositions;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_checkbox_dialog);
        dialog.setCancelable(cancelable);


        ((TextView) dialog.findViewById(R.id.dialogTitle)).setText(title);
        listView = (ListView) dialog.findViewById(R.id.dialogOptionsList);
        optionsListAdapter = new CheckBoxListAdapter(context,options);
        listView.setAdapter(optionsListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                optionsListAdapter.setSelection(position,view,parent);
                mPosition = position;
            }
        });
        ((Button) dialog.findViewById(R.id.okButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPosition != -1) {
                    iCheckboxDialogDismiss.onCheckboxDialogDismiss(selectedPositionArray);
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


        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int height = (int)(displayMetrics.heightPixels*0.4);
        if(height<200){
            height = 200;
        }
        dialog.getWindow().setLayout((int)(displayMetrics.widthPixels*0.9),height);

        dialog.show();
        Log.i("CB Dialog",(int)(displayMetrics.heightPixels*0.9)+" w/h "+(int)(displayMetrics.heightPixels*0.45));

    }


    public class CheckBoxListAdapter extends BaseAdapter {

        Context context;
        String options[]=null;
        LayoutInflater inflater;
        CheckBox selectedCheckBox;
        int selectedIndex = -1;

        public CheckBoxListAdapter(Context context, String options[]){
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
                rowView = inflater.inflate(R.layout.item_checkbox_option, null);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.optionItem = (CheckBox) rowView.findViewById(R.id.custom_option_checkbox);

                rowView.setTag(viewHolder);
            }

            // fill data
            final ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.optionItem.setText(options[position]);
            if(isPriorSelected(position)){
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
            public CheckBox optionItem;

        }

        public void setSelection(final int position, View convertView, final ViewGroup parent){
            ViewHolder obj = (ViewHolder) getView(position,convertView,parent).getTag();
            if(obj.optionItem.isChecked()) {
                obj.optionItem.setChecked(false);
                removeSelection(position);
            }
            else {
                obj.optionItem.setChecked(true);
                selectedPositionArray.add(position);
            }
        }

    }

    private int getScreenSize(){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        return (int) dpWidth;
    }

    private void removeSelection(int mPosition){
        for(int i=0;i<selectedPositionArray.size();i++){
            if (selectedPositionArray.get(i) == mPosition){
                selectedPositionArray.remove(i);
                break;
            }
        }
    }

    private boolean isPriorSelected(int mPosition){
        boolean isSelected = false;
        for (int i=0;i<selectedPositionArray.size();i++){
            if(selectedPositionArray.get(i) == mPosition){
                isSelected = true;
            }
        }
        return isSelected;
    }

}
