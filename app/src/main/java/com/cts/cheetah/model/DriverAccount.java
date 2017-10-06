package com.cts.cheetah.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by manu.palassery on 21-03-2017.
 */

public class DriverAccount {
    private String driverFirstName;
    private String driverLastName;
    private String driverName;
    private String driverId;
    private String driverThumbImage;
    private String driverDoB;
    private String driverAddress;
    private String city;
    private String state;
    private String country;
    private String driverPhone;
    private String driverSecondaryPhone;
    private String driverEmail;

    private Document licenseRecord;
    private Document medicalRecord;
    private Document safetyRecord;

    private String vehicleThumbImage;
    private String vehicleLicenseNo;
    private String vehicleType;
    //
    private String vehicleVinNo;
    private String vehicleMake;
    private String vehicleModel;
    private String vehicleRackCapacity;
    private String vehicleRackLength;
    private String vehicleTare;
    private String vehicleCapacity;

    private String abaRoutingNo;
    private String bankName;
    private String bankAccountNo;
    private String accountHolderName;
    private String accountType;
    private String payPercentage;

    private boolean personalInfoPending;
    private boolean vehicleInfoPending;

    private ArrayList<String> headerList;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverFirstName() {
        return driverFirstName;
    }

    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    public String getDriverLastName() {
        return driverLastName;
    }

    public void setDriverLastName(String driverLastName) {
        this.driverLastName = driverLastName;
    }

    public String getDriverDoB() {
        return driverDoB;
    }

    public void setDriverDoB(String driverDoB) {
        this.driverDoB = driverDoB;
    }

    public String getDriverThumbImage() {
        return driverThumbImage;
    }

    public void setDriverThumbImage(String driverThumbImage) {
        this.driverThumbImage = driverThumbImage;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverAddress() {
        return driverAddress;
    }

    public void setDriverAddress(String driverAddress) {
        this.driverAddress = driverAddress;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDriverSecondaryPhone() {
        return driverSecondaryPhone;
    }

    public void setDriverSecondaryPhone(String driverSecondaryPhone) {
        this.driverSecondaryPhone = driverSecondaryPhone;
    }

    public String getVehicleThumbImage() {
        return vehicleThumbImage;
    }

    public void setVehicleThumbImage(String vehicleThumbImage) {
        this.vehicleThumbImage = vehicleThumbImage;
    }
    //------------------------


    public Document getLicenseRecord() {
        return licenseRecord;
    }

    public void setLicenseRecord(Document licenseRecord) {
        this.licenseRecord = licenseRecord;
    }

    public Document getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(Document medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public Document getSafetyRecord() {
        return safetyRecord;
    }

    public void setSafetyRecord(Document safetyRecord) {
        this.safetyRecord = safetyRecord;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleLicenseNo() {
        return vehicleLicenseNo;
    }

    public void setVehicleLicenseNo(String vehicleRegNo) {
        this.vehicleLicenseNo = vehicleRegNo;
    }

    public String getVehicleVinNo() {
        return vehicleVinNo;
    }

    public void setVehicleVinNo(String vehicleVinNo) {
        this.vehicleVinNo = vehicleVinNo;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleRackCapacity() {
        return vehicleRackCapacity;
    }

    public void setVehicleRackCapacity(String vehicleRackCapacity) {
        this.vehicleRackCapacity = vehicleRackCapacity;
    }

    public String getVehicleRackLength() {
        return vehicleRackLength;
    }

    public void setVehicleRackLength(String vehicleRackLength) {
        this.vehicleRackLength = vehicleRackLength;
    }

    public String getVehicleTare() {
        return vehicleTare;
    }

    public void setVehicleTare(String vehicleTare) {
        this.vehicleTare = vehicleTare;
    }

    public String getVehicleCapacity() {
        return vehicleCapacity;
    }

    public void setVehicleCapacity(String vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
    }

    public boolean isPersonalInfoPending() {
        return personalInfoPending;
    }

    public void setPersonalInfoPending(boolean personalInfoPending) {
        this.personalInfoPending = personalInfoPending;
    }

    public boolean isVehicleInfoPending() {
        return vehicleInfoPending;
    }

    public void setVehicleInfoPending(boolean vehicleInfoPending) {
        this.vehicleInfoPending = vehicleInfoPending;
    }

    public String getAbaRoutingNo() {
        return abaRoutingNo;
    }

    public void setAbaRoutingNo(String abaRoutingNo) {
        this.abaRoutingNo = abaRoutingNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getPayPercentage() {
        return payPercentage;
    }

    public void setPayPercentage(String payRate) {
        this.payPercentage = payRate;
    }

    public ArrayList<String> getHeaderList() {
        return headerList;
    }

    public void setHeaderList(ArrayList<String> headerList) {
        this.headerList = headerList;
    }

    public void setData(JSONObject result){
        try {
            headerList = new ArrayList<>();

            JSONObject resultObject = result.getJSONObject("results");

            JSONObject accountSummary = resultObject.getJSONObject("accountSummary");
            if(accountSummary != null) {
                setDriverFirstName(accountSummary.getString("firstName"));
                setDriverLastName(accountSummary.getString("lastName"));
                setDriverName(accountSummary.getString("firstName") + " "+ accountSummary.getString("lastName"));
                //setDriverId(accountSummary.getString("driverId")); by an
                setDriverId(accountSummary.getString("studentId"));
               // setDriverThumbImage(accountSummary.getString("driverProfileThumb"));
                setDriverThumbImage(accountSummary.getString("profileThumb"));
                //setVehicleLicenseNo(accountSummary.getString("vehicleLicenseNo"));
               // setVehicleType(accountSummary.getString("vehicleType"));
                //setVehicleThumbImage(accountSummary.getString("vehicleThumb"));
            }

            JSONObject personalInformation = resultObject.getJSONObject("personalInformation");
            if(personalInformation != null) {
               setDriverAddress(personalInformation.getString("address"));
               setDriverDoB(personalInformation.getString("dob"));
               setCity(personalInformation.getString("city"));
               setState(personalInformation.getString("state"));
               setCountry(personalInformation.getString("country"));
               setDriverPhone(personalInformation.getString("phoneNo"));
               setDriverSecondaryPhone(personalInformation.getString("mobile"));
               setDriverEmail(personalInformation.getString("email"));
                headerList.add("Personal Information");
            }

//            JSONArray records = resultObject.getJSONArray("records");
//            if(records.length() == 0){
//
//            }else {
//                if (records != null) {
//                    JSONObject recordObject;
//                    for(int i=0;i<records.length();i++){
//                        recordObject = new JSONObject(records.get(i).toString());
//                        Document doc = new Document();
//                        doc.setDocumentType(recordObject.getString("type"));
//                        doc.setDocumentName(recordObject.getString("name"));
//                        doc.setDocumentStatus(recordObject.getString("status"));
//                        switch(recordObject.getString("type")){
//                            case "license":
//                                setLicenseRecord(doc);
//                                break;
//                            case "medical":
//                                setMedicalRecord(doc);
//                                break;
//                            case "safetyscore":
//                                setSafetyRecord(doc);
//                                break;
//
//                        }
//
//                    }
//                    headerList.add("Records");
//                }
//            }
//
//            JSONObject vehicleInformation = resultObject.getJSONObject("vehicleInformation");
//            if(vehicleInformation != null) {
//                setVehicleVinNo(vehicleInformation.getString("vinNumber"));
//                setVehicleMake(vehicleInformation.getString("make"));
//                setVehicleModel(vehicleInformation.getString("model"));
//                setVehicleRackCapacity(vehicleInformation.getString("rackCapacity"));
//                setVehicleRackLength(vehicleInformation.getString("rackLength"));
//                setVehicleTare(vehicleInformation.getString("tare"));
//                setVehicleCapacity(vehicleInformation.getString("capacity"));
//                headerList.add("Vehicle Information");
//            }
//
//            JSONObject paymentInformation = resultObject.getJSONObject("paymentInformation");
//            if(paymentInformation != null) {
//                setAbaRoutingNo(paymentInformation.getString("abaRouting"));
//                setBankName(paymentInformation.getString("bankName"));
//                setBankAccountNo(paymentInformation.getString("accountNo"));
//                setAccountHolderName(paymentInformation.getString("accountHolderName"));
//                setAccountType(paymentInformation.getString("accountType"));
//                setPayPercentage(paymentInformation.getString("percentage"));
//                headerList.add("Payment Information");
//            }

        }catch (JSONException e){
            Log.i("Driver Account data exp",e+"");
        }

    }
}
