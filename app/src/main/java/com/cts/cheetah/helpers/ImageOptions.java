package com.cts.cheetah.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cts.cheetah.R;
import com.cts.cheetah.components.InputAlertDialog;
import com.cts.cheetah.components.OptionsAlertDialog;
import com.cts.cheetah.interfaces.IDocumentImage;
import com.cts.cheetah.interfaces.IInputDialogDismiss;
import com.cts.cheetah.interfaces.IOptionsDialogDismiss;

import java.io.File;
import java.io.IOException;

/**
 * Created by manu.palassery on 03-04-2017.
 */

public class ImageOptions extends Activity implements IInputDialogDismiss,IOptionsDialogDismiss {
    Context context;
    Activity activity;
    IDocumentImage iImageOptions;
    static final int REQUEST_TAKE_PHOTO = 1;
    private int PICK_IMAGE_REQUEST = 2;
    static final int IMAGE_OPTIONS = 1;
    String mCurrentPhotoPath;
    RelativeLayout rLayout;
    LinearLayout lLayout;

    public ImageOptions(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        iImageOptions = (IDocumentImage) activity;
    }

    public void showImageOptions(LinearLayout lLayout,RelativeLayout rLayout){
        String options[] = {ApplicationRef.AppConstants.CAMERA,ApplicationRef.AppConstants.GALLERY};
        OptionsAlertDialog optionsAlertDialog = new OptionsAlertDialog(this.context,this);
        optionsAlertDialog.showOptionsAlert(context.getResources().getString(R.string.document_select_options),options,true,IMAGE_OPTIONS);
    }

    @Override
    public String onInputDialogDismiss(String value) {
        saveDocumentFile(value);
        return null;
    }

    private void saveDocumentFile(String fileName){
        iImageOptions.onImageSave(fileName,mCurrentPhotoPath);
        mCurrentPhotoPath = null;
    }

    @Override
    public void onOptionsDismiss(int identifier, int position,String value) {
        if(identifier == IMAGE_OPTIONS) {
            switch (position){
                case 0:
                    showCamera();
                    break;
                case 1:
                    showGallery();
                    break;
            }

        }
    }


    public void showCamera(){
       /* if(checkCameraPermission(activity,rLayout,lLayout)){

        }*/

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = Utility.createImageFile(this.context);
                //Utility.createImageFile(this.context);
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                try {
                    mCurrentPhotoPath = photoFile.getAbsolutePath();
                    Uri photoURI = FileProvider.getUriForFile(this.context, "com.cts.cheetah.fileprovider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }catch (Exception e){
                    Log.i("",e+"");
                }
            }
        }
    }

    private void showGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //Intent intent = new Intent();
        intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    public void pickImageResultHandler(Intent data){
        try {
            File photoFile = Utility.createImageFile(this.context);
            if(photoFile != null) {
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                Uri selectedImageUri = data.getData();
                ImageProcessor imageProcessor = new ImageProcessor();
                imageProcessor.copyGalleryImage(selectedImageUri, this.context,photoFile);
                requestDocumentName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requestDocumentName(){
        InputAlertDialog alertDialog = new InputAlertDialog(this.context,this);
        alertDialog.showInputAlert("Please enter a file name"," ");
    }

    //CHECK CAMERA PERMISSION--------------------------------------------------------------------------------
    private static final int REQUEST_CAMERA = 0;
    private boolean checkCameraPermission(Activity activty, RelativeLayout rLayout,LinearLayout lLayout) {
        try {
            // Check if the Camera permission is already available.
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // Camera permission has not been granted.
                requestCameraPermission(activty,rLayout,lLayout);
            } else {
                // Camera permissions is already available, show the camera preview.
                return true;
            }
        } catch (Exception e) {
            Utility.logger(e);
        }
        return false;
    }


    private void requestCameraPermission(final Activity activty, RelativeLayout rLayout,LinearLayout lLayout) {
        try {
            Log.d("", "CAMERA permission has NOT been granted. Requesting permission.");

            // BEGIN_INCLUDE(camera_permission_request)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                // Provide an additional rationale to the user if the permission was not granted
                // and the user would benefit from additional context for the use of the permission.
                // For example if the user has previously denied the permission.
                Log.i("", "Displaying camera permission rationale to provide additional context.");
                if(rLayout != null) {
                    Snackbar.make(rLayout, getString(R.string.question_message_cameraPermissionEnquiry),
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(activty,
                                            new String[]{Manifest.permission.CAMERA},
                                            REQUEST_CAMERA);
                                }
                            })
                            .show();
                }else if(lLayout != null){
                    Snackbar.make(lLayout, getString(R.string.question_message_cameraPermissionEnquiry),
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(activty,
                                            new String[]{Manifest.permission.CAMERA},
                                            REQUEST_CAMERA);
                                }
                            })
                            .show();
                }
            } else {

                // Camera permission has not been granted yet. Request it directly.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            }
            // END_INCLUDE(camera_permission_request)
        } catch (Exception e) {
            Utility.logger(e);
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode,String[] permissions,int[] grantResults){
        if(permissions[0].equals("android.permission.CAMERA") && grantResults[0] == 0){

        }
    }

}
