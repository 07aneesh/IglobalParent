package com.cts.cheetah.model;

import android.util.Log;

import com.cts.cheetah.helpers.ApplicationRef;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by manu.palassery on 22-03-2017.
 */

public class Document {

    private String documentId;
    private String documentName;
    private String documentType;
    private String documentExpiry;
    private String documentStatus;
    private String statusDescription;
    private ArrayList<DocumentImage> documentImages;
    private String safetyScore;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Document(){
        documentImages = new ArrayList<>();
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentExpiry() {
        return documentExpiry;
    }

    public void setDocumentExpiry(String documentExpiry) {
        this.documentExpiry = documentExpiry;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getDocumentStatusDescription() {
        return statusDescription;
    }

    public void setDocumentStatusDescription(String documentStatusMessage) {
        this.statusDescription = documentStatusMessage;
    }

    public ArrayList<DocumentImage> getDocumentImages() {
        return documentImages;
    }

    public void setDocumentImages(ArrayList<DocumentImage> documentImages) {
        this.documentImages = documentImages;
    }

    public String getSafetyScore() {
        return safetyScore;
    }

    public void setSafetyScore(String safetyScore) {
        this.safetyScore = safetyScore;
    }



    public void setData(JSONObject result,String documentType){
        if(result != null){
            try{
                JSONObject results = result.getJSONObject("results");
                if(results != null) {
                    setDocumentId(results.getString("documentId"));

                    //If type is License-----------------
                    if (documentType.equals("License")) {
                        setDocumentName(results.getString("licenseNo"));
                    } else {
                        setDocumentName(results.getString("documentName"));
                    }
                    //------------------------------------

                    if (documentType.equals(ApplicationRef.Document.SAFETY_SCORE)) {
                        setSafetyScore(results.getString("safetyScore"));
                    }else{
                        setDocumentExpiry(results.getString("expiryDate"));
                        setDocumentStatus(results.getString("status"));// "803" expired "804" rejected
                        setDocumentStatusDescription(results.getString("statusDescription"));
                    }

                   ArrayList<DocumentImage> documentImages = new ArrayList<>();
                    JSONArray documents = results.getJSONArray("documents");

                    if (documents != null) {
                        for (int i = 0; i < documents.length(); i++) {
                            JSONObject documentObject = (JSONObject) documents.get(i);
                            DocumentImage documentImage = new DocumentImage();
                            documentImage.setImageName(documentObject.getString("imageName"));
                            documentImage.setImagePath(documentObject.getString("imageURL"));
                            documentImage.setImageLocation(DocumentImage.REMOTE);
                            documentImages.add(documentImage);
                        }

                        setDocumentImages(documentImages);
                    }

                    /*DocumentImage documentImage = new DocumentImage();
                    documentImage.setImageName("imageName");
                    documentImage.setImagePath(ApplicationRef.Amazon.S3_URL+"Brian_McMeans636298715837981115.JPG");
                    documentImage.setImageLocation(DocumentImage.REMOTE);
                    documentImages.add(documentImage);

                    setDocumentImages(documentImages);*/


                }
            }catch (JSONException e){
                Log.i("DOCUMENT EXPN",e+"");
            }
        }
    }


    public ArrayList<Document> setMedicalData(JSONObject result){
        ArrayList<Document> documentsArrayList = new ArrayList<>();
        if(result!= null) {
            try{
                JSONArray results = result.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    Document document = new Document();
                    JSONObject jo = (JSONObject) results.get(i);
                    JSONObject doc = new JSONObject("{\"results\":" + jo.toString() + "}");
                    document.setData(doc, ApplicationRef.Document.MEDICAL);
                    documentsArrayList.add(document);
                }
            }catch (JSONException e){
                Log.i("DOCUMENT EXPN",e+"");
            }
        }
        return documentsArrayList;
    }


}
