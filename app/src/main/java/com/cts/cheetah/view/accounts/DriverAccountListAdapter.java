package com.cts.cheetah.view.accounts;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cts.cheetah.R;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.model.DriverAccount;
//import com.cts.cheetah.view.accounts.records.license.LicenseActivity;
//import com.cts.cheetah.view.accounts.records.medical.MedicalRecordActivity;
//import com.cts.cheetah.view.accounts.records.safety.SafetyScoreActivity;

import java.util.ArrayList;
import java.util.List;


public class DriverAccountListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private DriverAccount _listDataChild;

    final static String RECORD_LICENSE = "License";
    final static String RECORD_MEDICAL = "Medical";
    final static String RECORD_SAFETY_SCORE = "SafetyScore";

    private TextView driverDoB,driverAddress,driverCity,driverState,driverCountry,driverEmail,driverPhone,driverSecondaryPhone;
    private TextView vehicleLicenseNo,vehicleVinNo,vehicleMake,vehicleType,vehicleModel,vehicleRackCapacity,vehicleRackLength,vehicleTare,vehicleCapacity;
    private TextView abaRoutingNo,bankName,bankAccountNo,accountHolderName,accountType,payPercentage;


   /* public NotificationListAdapter(Context context, ArrayList<String> listDataHeader,HashMap<String, DriverAccount> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;


    }*/

    public DriverAccountListAdapter(Context context, ArrayList<String> listDataHeader, DriverAccount listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataHeader.get(groupPosition);//this._listDataChild.get(this._listDataHeader.get(groupPosition));
    }

 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        String item = _listDataHeader.get(groupPosition);

        //if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            try {
                switch (item) {
                    case "Personal Information":
                        convertView = infalInflater.inflate(R.layout.driver_personal_item, null);
                        driverAddress = (TextView) convertView.findViewById(R.id.driverAddress);
                        driverAddress.setText(_listDataChild.getDriverAddress());
                        driverDoB = (TextView) convertView.findViewById(R.id.driverDoB);
                        driverDoB.setText(_listDataChild.getDriverDoB());
                        driverCity = (TextView) convertView.findViewById(R.id.driverCity);
                        driverCity.setText(_listDataChild.getCity());
                        driverState = (TextView) convertView.findViewById(R.id.driverState);
                        driverState.setText(_listDataChild.getState());
                        driverCountry = (TextView) convertView.findViewById(R.id.driverCountry);
                        driverCountry.setText(_listDataChild.getCountry());

                        driverPhone = (TextView) convertView.findViewById(R.id.driverPhoneNo);
                        driverPhone.setText(_listDataChild.getDriverPhone());
                        driverSecondaryPhone = (TextView) convertView.findViewById(R.id.driverSecondaryPhoneNo);
                        driverSecondaryPhone.setText(_listDataChild.getDriverSecondaryPhone());
                        driverEmail = (TextView) convertView.findViewById(R.id.driverEmail);
                        driverEmail.setText(_listDataChild.getDriverEmail());
                        break;
                    case "Vehicle Information":
                        convertView = infalInflater.inflate(R.layout.vehicle_details_item, null);
                        vehicleLicenseNo = (TextView) convertView.findViewById(R.id.vehicleLicenseNo);
                        vehicleLicenseNo.setText(_listDataChild.getVehicleLicenseNo());
                        vehicleVinNo = (TextView) convertView.findViewById(R.id.vehicleVinNo);
                        vehicleVinNo.setText(_listDataChild.getVehicleVinNo());
                        vehicleMake = (TextView) convertView.findViewById(R.id.vehicleMake);
                        vehicleMake.setText(_listDataChild.getVehicleMake());
                        vehicleType = (TextView) convertView.findViewById(R.id.vehicleType);
                        vehicleType.setText(_listDataChild.getVehicleType());
                        vehicleModel = (TextView) convertView.findViewById(R.id.vehicleModel);
                        vehicleModel.setText(_listDataChild.getVehicleModel());
                        vehicleRackCapacity = (TextView) convertView.findViewById(R.id.vehicleRackCapacity);
                        if (_listDataChild.getVehicleRackCapacity().equals("")) {
                            vehicleRackCapacity.setText(_listDataChild.getVehicleRackCapacity());
                        } else {
                            vehicleRackCapacity.setText(_listDataChild.getVehicleRackCapacity() + " lbs");
                        }
                        vehicleRackLength = (TextView) convertView.findViewById(R.id.vehicleRackLength);
                        vehicleRackLength.setText(_listDataChild.getVehicleRackLength());
                        //vehicleTare = (TextView) convertView.findViewById(R.id.vehicleTare);
                        //vehicleTare.setText(_listDataChild.getVehicleTare());
                        vehicleCapacity = (TextView) convertView.findViewById(R.id.vehicleCapacity);
                        vehicleCapacity.setText(_listDataChild.getVehicleCapacity());
                        break;
//                    case "Records":
//                        convertView = infalInflater.inflate(R.layout.driver_record_item, null);
//                        LinearLayout licenseLink = (LinearLayout) convertView.findViewById(R.id.licenseRecord);
//
//                        if (!_listDataChild.getLicenseRecord().getDocumentStatus().equals(ApplicationRef.StatusCodes.DOCUMENT_STATUS_NO_RECORD)) {
//                            licenseLink.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    loadRecordDetails(RECORD_LICENSE);
//                                }
//                            });
//                        }
//
//                        if (!_listDataChild.getLicenseRecord().getDocumentStatus().equals(ApplicationRef.StatusCodes.DOCUMENT_STATUS_APPROVED)) {
//                            TextView licenseStatus = (TextView) convertView.findViewById(R.id.licenseStatus);
//                            licenseStatus.setBackground(getStatusBackground(_listDataChild.getLicenseRecord().getDocumentStatus()));
//                            licenseStatus.setText(_listDataChild.getLicenseRecord().getDocumentStatus());
//                        }
//                        //---------------------------
//
//                        LinearLayout medicalLink = (LinearLayout) convertView.findViewById(R.id.medicalRecord);
//                        medicalLink.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                loadRecordDetails(RECORD_MEDICAL);
//                            }
//                        });
//
//                        if (!_listDataChild.getMedicalRecord().getDocumentStatus().equals(ApplicationRef.StatusCodes.DOCUMENT_STATUS_APPROVED)) {
//                            TextView medicalStatus = (TextView) convertView.findViewById(R.id.medicalStatus);
//                            medicalStatus.setBackground(getStatusBackground(_listDataChild.getMedicalRecord().getDocumentStatus()));
//                            medicalStatus.setText(_listDataChild.getMedicalRecord().getDocumentStatus());
//                        }
//                        //---------------------------
//                        LinearLayout safetyScoreLink = (LinearLayout) convertView.findViewById(R.id.safetyRecord);
//                        if (!_listDataChild.getSafetyRecord().getDocumentStatus().equals(ApplicationRef.StatusCodes.DOCUMENT_STATUS_NO_RECORD)) {
//                            safetyScoreLink.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    loadRecordDetails(RECORD_SAFETY_SCORE);
//                                }
//                            });
//                        }
//
//                        if (!_listDataChild.getSafetyRecord().getDocumentStatus().equals(ApplicationRef.StatusCodes.DOCUMENT_STATUS_APPROVED)) {
//                            TextView safetyStatus = (TextView) convertView.findViewById(R.id.safetyStatus);
//                            safetyStatus.setBackground(getStatusBackground(_listDataChild.getSafetyRecord().getDocumentStatus()));
//                            safetyStatus.setText(_listDataChild.getSafetyRecord().getDocumentStatus());
//                        }
//                        //---------------------------------
//                        break;
//                    case "Payment Information":
//                        convertView = infalInflater.inflate(R.layout.driver_payment_item, null);
//                        abaRoutingNo = (TextView) convertView.findViewById(R.id.driverAbaRoutingNo);
//                        abaRoutingNo.setText(_listDataChild.getAbaRoutingNo());
//                        bankName = (TextView) convertView.findViewById(R.id.nameOfBank);
//                        bankName.setText(_listDataChild.getBankName());
//                        bankAccountNo = (TextView) convertView.findViewById(R.id.accountNo);
//                        bankAccountNo.setText(_listDataChild.getBankAccountNo());
//                        accountHolderName = (TextView) convertView.findViewById(R.id.accountHolderName);
//                        accountHolderName.setText(_listDataChild.getAccountHolderName());
//                        accountType = (TextView) convertView.findViewById(R.id.accountType);
//                        accountType.setText(_listDataChild.getAccountType());
//                        payPercentage = (TextView) convertView.findViewById(R.id.percentage);
//                        payPercentage.setText(_listDataChild.getPayPercentage());
//                        break;
                }
            }catch (Exception e){

            }

        return convertView;
    }

    private Drawable getStatusBackground(String value){
        Drawable drawable =null;
        switch (value){
            case (ApplicationRef.StatusCodes.DOCUMENT_STATUS_NO_RECORD):
                drawable = _context.getResources().getDrawable(R.drawable.pending_status_bg);
                break;
            case (ApplicationRef.StatusCodes.DOCUMENT_STATUS_PENDING_APPROVAL):
                drawable = _context.getResources().getDrawable(R.drawable.pending_status_bg);
                break;
            case (ApplicationRef.StatusCodes.DOCUMENT_STATUS_REJECTED):
                drawable = _context.getResources().getDrawable(R.drawable.expire_status_bg);
                break;
            case (ApplicationRef.StatusCodes.DOCUMENT_STATUS_EXPIRED):
                drawable = _context.getResources().getDrawable(R.drawable.expire_status_bg);
                break;

        }
        return drawable;
    }

    private void loadRecordDetails(String type){
         if (Utility.haveNetworkConnectivity(_context)) {
            Intent intent;

//            switch (type) {
//                case RECORD_MEDICAL:
//                    intent = new Intent(_context, MedicalRecordActivity.class);
//                    _context.startActivity(intent);
//                    break;
//                case RECORD_LICENSE:
//                    intent = new Intent(_context, LicenseActivity.class);
//                    _context.startActivity(intent);
//                    break;
//                case RECORD_SAFETY_SCORE:
//                    intent = new Intent(_context, SafetyScoreActivity.class);
//                    _context.startActivity(intent);
//                    break;
//            }
        }else{
            Toast.makeText(_context, _context.getString(R.string.service_error_noConnection), Toast.LENGTH_SHORT).show();
        }
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        String headerText = this._listDataHeader.get(groupPosition);
        return headerText;
    }
 
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.driver_accounts_header, null);
        }
 
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);
        
        ImageView expandIcon = (ImageView) convertView.findViewById(R.id.plusMinusBullet);
        
        if (isExpanded) {
        	expandIcon.setImageResource(R.drawable.icon_accordion_open);
        } else {
        	expandIcon.setImageResource(R.drawable.icon_accordion_close);
        }
        
        //lblListHeader.setTextColor(Color.BLACK);
        
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
