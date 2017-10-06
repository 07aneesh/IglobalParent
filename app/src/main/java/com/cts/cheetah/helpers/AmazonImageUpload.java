package com.cts.cheetah.helpers;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.cts.cheetah.R;
import com.cts.cheetah.interfaces.IAmazonUpload;
import com.cts.cheetah.service.IBaseService;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by fingent on 11/2/16.
 */

public class AmazonImageUpload{
    public static final String IMAGE_FILE="image";
    public static final String IMAGE_THUMB_FILE="thumb_image";

    private TransferUtility transferUtility;
    private int index = 0;
    private ArrayList<String> uploadUrls;
    private static final String SUCCESS = "success";
    private static final String FAILED = "failed";
    TransferObserver observer;
    Context context;
    IAmazonUpload listener;
    String _currentImageType=IMAGE_FILE;
    String driverId=null;


    public AmazonImageUpload(IAmazonUpload iAmazonUpload, Context context) {
        this.context = context;
        this.listener = iAmazonUpload;
        try {
            transferUtility = Util.getTransferUtility(context);
        } catch (Exception e) {
            listener.imageUploadError(context.getResources().getString(R.string.upload_image_error));
        }
    }



    public void initiateUpload( ArrayList<String> uploadUrls,String type) {
        driverId = PreferenceManager.getInstance(context).getDriverID();
        _currentImageType = type;
        this.uploadUrls = uploadUrls;
        if (!uploadUrls.isEmpty())
            beginUpload(uploadUrls.get(index));
        else{
            imageUploadCompleted();
        }
    }

   /**
     * Begins to upload the file specified by the file path.
     */
    private void beginUpload(String url) {
        if (url == null) {
            Toast.makeText(context, context.getResources().getString(R.string.could_not_file_image),Toast.LENGTH_LONG).show();
            return;
        }
        File file = new File(url);
        if(_currentImageType.equals(IMAGE_FILE)) {
            //observer = transferUtility.upload(ApplicationRef.Amazon.BUCKET_NAME, ApplicationRef.Amazon.IMAGE_SUB_FOLDER + file.getName(), file);
            observer = transferUtility.upload(ApplicationRef.Amazon.BUCKET_NAME, ApplicationRef.Amazon.DOC_IMAGE_SUB_FOLDER + driverId + "/" + file.getName(), file);
        }else{
            //observer = transferUtility.upload(ApplicationRef.Amazon.BUCKET_NAME, ApplicationRef.Amazon.THUMB_IMAGE_SUB_FOLDER + file.getName(), file);
            observer = transferUtility.upload(ApplicationRef.Amazon.BUCKET_NAME, ApplicationRef.Amazon.DOC_IMAGE_SUB_FOLDER + driverId + "/Thumbs/" + file.getName(), file);
        }

        /**
         * Note that usually we set the transfer listener after initializing the
         * transfer. However it isn't required in this sample app. The flow is
         * click upload button -> start an activity for image selection
         * startActivityForResult -> onActivityResult -> beginUpload -> onResume
         * -> set listeners to in progress transfers.
         */
        observer.setTransferListener(new UploadListener());
    }

    public void imageUploadCompleted() {

        Log.i("AMAZON S3","Image upload complete");
        if(_currentImageType.equals(IMAGE_FILE)) {
            listener.imageUploadSuccess();
        }else{
            listener.thumbImageUploadSuccess();
        }

    }



    /**
     * A TransferListener class that can listen to a upload task and be notified
     * when the status changes.
     */
    class UploadListener implements TransferListener {

        // Simply updates the UI list when notified.
        @Override
        public void onError(int id, Exception e) {
            Log.e("Photo Upload", "Error during upload: " + id, e);
            transferUtility.cancel(observer.getId());
            listener.imageUploadError(context.getResources().getString(R.string.upload_image_error));
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d("Photo Upload", String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            Log.d("Photo Upload", "onStateChanged: " + id + ", " + newState);
            if (newState == TransferState.COMPLETED) {
                Log.d("Upload Completed",uploadUrls.get(index));

                index++;
                if (index < uploadUrls.size())
                    beginUpload(uploadUrls.get(index));
                else {
                    imageUploadCompleted();
                }
            } else if (newState == TransferState.FAILED) {
                transferUtility.cancel(observer.getId());
                listener.imageUploadError(context.getResources().getString(R.string.upload_image_error));
            } else if (newState == TransferState.FAILED || newState == TransferState.WAITING_FOR_NETWORK || newState == TransferState.PAUSED) {
                transferUtility.cancel(observer.getId());
            } else if (newState == TransferState.IN_PROGRESS) {

            }

        }

    }

}
