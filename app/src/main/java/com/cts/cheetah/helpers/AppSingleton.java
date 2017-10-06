package com.cts.cheetah.helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.cts.cheetah.model.Document;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by manu.palassery on 21-02-2017.
 */

public class AppSingleton {
    private static AppSingleton mAppSingletonInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mContext;

    private Document document;

    Intent mServiceIntent=null;
    boolean isPositionServiceRunning;

    private AppSingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized AppSingleton getInstance(Context context) {
        if (mAppSingletonInstance == null) {
            mAppSingletonInstance = new AppSingleton(context);
        }
        return mAppSingletonInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * getter and setter for document data passed between Medical record view and edit Acitivties
     * @return
     */
    public Document getDocument(){
        return document;
    }

    public void setDocument(Document document){
        this.document = document;
    }

    /**
     * startPositionUploadService() method start position upload background service to update driver position
     * in every pre-set interval. stopPositionUploadService() stops the service.
     * @param tripId
     */
    public void startPositionUploadService(String tripId, LatLng locationLatLong){
        String statusCode= StatusCodeManager.getStatusCode(mContext,ApplicationRef.StatusCodes.ORDER_STATUS,ApplicationRef.StatusCodes.ORDER_STATUS_IN_TRANSIT);
        mServiceIntent = new Intent(mContext, PositionUploadService.class);
        mServiceIntent.putExtra(ApplicationRef.TripTags.TRIP_ID,tripId);
        mServiceIntent.putExtra(ApplicationRef.StatusCodes.STATUS_CODE,statusCode);
        if(locationLatLong == null) {
            mServiceIntent.putExtra(ApplicationRef.TripTags.LATITUDE, "");
            mServiceIntent.putExtra(ApplicationRef.TripTags.LONGITUDE, "");
        }else{
            mServiceIntent.putExtra(ApplicationRef.TripTags.LATITUDE, locationLatLong.latitude);
            mServiceIntent.putExtra(ApplicationRef.TripTags.LONGITUDE, locationLatLong.longitude);
        }
        //
        mContext.startService(mServiceIntent);
        isPositionServiceRunning = true;

    }

    public void stopPositionUploadService(){
        if (isPositionServiceRunning){
            if(mServiceIntent != null){
                mContext.stopService(mServiceIntent);
                mServiceIntent=null;
            }
            isPositionServiceRunning = false;
        }
    }

}
