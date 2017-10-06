package com.cts.cheetah.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cts.cheetah.R;
import com.cts.cheetah.model.DocumentImage;
import com.cts.cheetah.view.login.LoginActivity;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by manu.palassery on 21-02-2017.
 */

public class Utility {

    public static final String WIFI_CONNECTION_CHECK = "WIFI";
    public static final String MOBILE_CONNECTION_CHECK = "MOBILE";

    public static String validateLoginDetails(Context context, String user, String password) {
        String s1 = user;
        String s2 = password;
        String result = "";
        if (!("".equals(s1))) {
//            if (s2.length() < 6) {
//                result = context.getString(R.string.login_message_passwordCount);
//            } else if (!"".equals(s2) && (s2.length() >= 6)) {
//                result = "true";
//            } by an
            result = "true";


        }

        if (!("".equals(s1))) {
            if ("".equals(s2)) {
                result = context.getString(R.string.login_message_enterPassword);
            }
        } else if ("".equals(s1)) {

            result = context.getString(R.string.login_message_enterUserName);
        }

        return result;
    }

    public static boolean validateEmail(Context context, String email) {
        if(email == null) {
            return false;
        }else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static void logger(Throwable e) {
        Log.d("Exception", e.toString());
    }

    public static boolean haveNetworkConnectivity(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase(WIFI_CONNECTION_CHECK) && ni.isConnected())
                haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase(MOBILE_CONNECTION_CHECK) && ni.isConnected())
                haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static String getDate(long timeInMilliSeconds,String dateFormat){
        String date="";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            date = formatter.format(timeInMilliSeconds);
        }catch (Exception e){

        }
        return date;
    }

    public static String convertLocalTimeToUTC(long timeInMilliSeconds) {
        Date time = new Date(timeInMilliSeconds);
        SimpleDateFormat outputFmt = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateString = outputFmt.format(time);
        return dateString;


    }

    public static long getMilliSecondsFromDate(String date){
        long milliSeconds=0;
        try{
            //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            formatter.setLenient(false);
            Date _date = formatter.parse(date);
            milliSeconds = _date.getTime();
        }catch (Exception e){
            Log.i("Date error",e+"");
        }

        return milliSeconds;
    }

    public static String getDateItem(long timeInMilliSeconds, String type){
        //import android.icu.text.SimpleDateFormat;
        Date date = new Date(timeInMilliSeconds); //Fri Mar 24 13:35:11 GMT+05:30 2017
        String result = "";
        switch (type) {
            case "DayOfWeek":
                result = (String) DateFormat.format("EEE",date); // Thursday
                break;
            case "Date":
                result = (String) DateFormat.format("dd", date); // 20
                break;
            case "MonthName":
                result = (String) DateFormat.format("MMM", date); // Jun
                break;
            case "MonthValue":
                result = (String) DateFormat.format("MM", date); // 06
                break;
            case "Year":
                result  = (String) DateFormat.format("yyyy", date); // 06
                break;
        }
        return result;
    }

    public static boolean checkPastDate(int year,int month,int day){
        Calendar cal = Calendar.getInstance();
        int today_year =cal.get(Calendar.YEAR);
        int today_month = cal.get(Calendar.MONTH);
        int today_date = cal.get(Calendar.DATE);

        if(year < today_year&&month < today_month&&day < today_date)
            return false;

        return true;

    }

    public static String checkToday(String selectedDate,String currentDate){
        String[] s = selectedDate.split("-");
        String[] d = currentDate.split("-");
        int selectedYear = Integer.parseInt(s[0]);
        int selectedMonth = Integer.parseInt(s[1]);
        int selectedDay = Integer.parseInt(s[2]);
        int currentYear = Integer.parseInt(d[0]);
        int currentMonth = Integer.parseInt(d[1]);
        int currentDay = Integer.parseInt(d[2]);
        if(selectedYear>currentYear){
            return "NEXT";
        }else if(selectedYear<currentYear){
            return "PREVIOUS";
        }
        else {
            if(selectedMonth>currentMonth){
                return "NEXT";
            }
            else if(selectedMonth<currentMonth){
                return "PREVIOUS";
            }
            else {
                if(selectedDay>currentDay){
                    return "NEXT";
                }else if(selectedDay<currentDay){
                    return "PREVIOUS";
                }
                else {
                    return "TODAY";
                }
            }
        }
    }

    public static Bitmap getRoundedShape(Bitmap bitmap,int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

     public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
         //File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES +"/thumb");
         File storageDir = getDocImageTempFolder(context);
         if (!storageDir.exists()) {
             boolean success = storageDir.mkdir();
         }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        //mCurrentPhotoPath = image.getAbsolutePath();
         int c= 0;
        return image;
    }


    public static Bitmap getBitmapFromUrl(int width, int height,String mCurrentPhotoPath) {
        Bitmap bitmap = null;

        try {
            // Get the dimensions of the View
        /*int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();*/
            int targetW = width;
            int targetH = height;

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            //mImageView.setImageBitmap(bitmap);
        }catch (Exception e){
            Log.i("",e+"");
        }
        return bitmap;
    }

    public static void deleteImageFile(String filePath){
        File file = new File(filePath);
        boolean deleted = file.delete();
    }

    public static void deleteImageFile(){

    }

    /**
     * Returns time elapsed since the time given as @param 'milliseconds'
     * @param milliSeconds
     * @return
     */
    public static String calculateTimeAgo(long milliSeconds){

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        String rd = formatter.format(calendar.getTime());
        Date receivedDate = null;
        try {
            receivedDate = formatter.parse(rd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar = Calendar.getInstance();
        String cd = formatter.format(calendar.getTime());
        Date currentDate =  null;
        try {
            currentDate = formatter.parse(cd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long different = currentDate.getTime() - receivedDate.getTime();
        //TimeUnit.MILLISECONDS.toDays(different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        //long elapsedSeconds = different / secondsInMilli;

        String returnTime = "";
        if(elapsedDays > 0) {
            returnTime += elapsedDays + " Days ";
        }

        if(elapsedHours > 0){
            if(elapsedHours > 1)
                returnTime += elapsedHours + " Hrs ";
            else
                returnTime += elapsedHours + " Hr ";
        }

        if(elapsedMinutes > 0){
            returnTime += elapsedMinutes + " mins ";
        }

        return returnTime + " ago";
    }

    public static String getFormattedNumber(double number){
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        return nf.format(number);
    }

    public static String getAccessToken(Context context){
       //return ApplicationRef.Service.BEARER + PreferenceManager.getInstance(context).getAccessToken();
        return PreferenceManager.getInstance(context).getAccessToken();
    }

    public static void handleInvalidSession(Context context){
        try{
            PreferenceManager.getInstance(context).clearUserPreferences();
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }catch (Exception e){

        }
    }

    public static String getThumbUrl(String imageUrl){
        String thumbUrl="";
        try{
            thumbUrl = imageUrl.substring(0,imageUrl.lastIndexOf("/")) + "/thumb/";
            thumbUrl += imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
        }catch (Exception e){

        }
        return thumbUrl;
    }

    public static boolean isFileExist(File file){
        if (!file.isFile()) {
            return false;
        }else{
            return true;
        }
    }

    /**
     * Deletes all temp image files taken to update record documents
     * @param context
     */
    public static void deleteTempDocFiles(Context context){
        try {
            File storageDir = new File(getDocImageTempFolderName(context));
            File[] files = storageDir.listFiles();
            if(files.length > 0) {
                for (File tempFile : files) {
                    tempFile.delete();
                }
            }

            storageDir = new File(getDocImageTempFolderName(context) + "/thumb");
            files = storageDir.listFiles();
            if(files.length > 0) {
                for (File tempFile : files) {
                    tempFile.delete();
                }
            }
        }catch (Exception e){
            Log.i("Temp delete exp", e+"");
        }
    }

    public static Bitmap getCustomMarkerIcon(Context context,String type,String status, String text){
        Bitmap bm = null;
        try {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            FrameLayout view = (FrameLayout) inflater.inflate(R.layout.custom_marker, null);

            TextView customMarkerText = (TextView) view.findViewById(R.id.customMarkerText);
            customMarkerText.setText(text);
            ImageView customMarkerImage = (ImageView) view.findViewById(R.id.customMarkerImage);

            if(status.equals("")) {
                if (type.equalsIgnoreCase(ApplicationRef.TripTags.LOCATION_TYPE_PICK_UP)) {
                    customMarkerImage.setImageDrawable(context.getResources().getDrawable(R.drawable.map_pickup));
                } else if (type.equalsIgnoreCase(ApplicationRef.TripTags.LOCATION_TYPE_DROP_OFF)) {
                    customMarkerImage.setImageDrawable(context.getResources().getDrawable(R.drawable.map_drop));
                }
            }else{
                customMarkerImage.setImageDrawable(context.getResources().getDrawable(R.drawable.map_pickup_grey));
            }

            view.setDrawingCacheEnabled(true);

            // this is the important code :)
            // Without it the view will have a dimension of 0,0 and the bitmap will be null
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

            view.buildDrawingCache(true);
            bm = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false); // clear drawing cache
        }catch (Exception e){
            Log.i("",e+"");
        }
        return bm;
    }


    public static void getDistanceTo(Context context,final String latitude,final String longitude,final TextView tv){
        if(latitude!= null && longitude!= null && !latitude.equals("null") && !longitude.equals("null")) {
            final float[] results = new float[1];
            android.location.LocationListener locationListener = new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("activity", "RLOC: loc by GPS");
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            try {
                Location location = null;
                LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
                // getting GPS status
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                // getting network status
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {

                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                        Log.d("activity", "LOC Network Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                android.location.Location.distanceBetween(latLng.latitude, latLng.longitude, Double.valueOf(latitude), Double.valueOf(longitude), results);
                                int meters = (int) results[0];
                                double inches = (39.370078 * meters);
                                int miles = (int) (inches / 63360);
                                tv.setText(miles + " miles away from you");
                                locationManager.removeUpdates(locationListener);
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                            Log.d("activity", "RLOC: GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    android.location.Location.distanceBetween(latLng.latitude, latLng.longitude, Double.valueOf(latitude), Double.valueOf(longitude), results);
                                    int meters = (int) results[0];
                                    double inches = (39.370078 * meters);
                                    int miles = (int) (inches / 63360);
                                    tv.setText(miles + " miles away from you");
                                    locationManager.removeUpdates(locationListener);
                                }
                            }
                        }
                    }
                }
            } catch (SecurityException e) {

            }
        }
    }

    //TIME FUNCTIONS -------------------------------------------------------------------------------
    public static String convertUTCTimeToLocal(String dateStr){
        String newDate = null;
        String utcDateFormatTemp = "MM-dd-yyy HH:mm";
        String localDateFormatTemp = "MM-dd-yyy HH:mm a";
        try {

            SimpleDateFormat formatter = new SimpleDateFormat(utcDateFormatTemp);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(dateStr);

            if (utcDateFormatTemp == null)
                localDateFormatTemp = "MM-dd-yyyy hh:mm a";

            SimpleDateFormat dateFormatter = new SimpleDateFormat(localDateFormatTemp); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            newDate = dateFormatter.format(value);
            Log.i("LOCAL DATE", newDate);
        } catch (Exception e) {
            logger(e);
        }
        return newDate;
    }
    //TIME FUNCTIONS -------------------------------------------------------------------------------

    // RECORDS REALTED FUNCTIONS -------------------------------------------------------------------

    public static String getRemoteUrl(String imageUrl,Context context){
        //Thumb url of Amazon S3 image.
        String thumbUrl="";
        String driverId = PreferenceManager.getInstance(context).getDriverID();
        try{
           thumbUrl = imageUrl.substring(0,imageUrl.lastIndexOf("/"));
            thumbUrl += imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
        }catch (Exception e){

        }
        return imageUrl;
    }

    public static String getRemoteThumbUrl(String imageUrl,Context context){
        //Thumb url of Amazon S3 image.
        String thumbUrl="";
        String driverId = PreferenceManager.getInstance(context).getDriverID();
        try{
            /*thumbUrl = imageUrl.substring(0,imageUrl.lastIndexOf("/"));
            thumbUrl = thumbUrl.substring(0,thumbUrl.lastIndexOf("/")) + "/" + ApplicationRef.Amazon.THUMB_IMAGE_SUB_FOLDER;
            thumbUrl += imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());*/

            thumbUrl = imageUrl.substring(0,imageUrl.lastIndexOf("/")) + "/Thumbs/";
            thumbUrl += imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
        }catch (Exception e){

        }
        return thumbUrl;
    }

    public static String getDocImageFolderName(Context context){
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + "documents" + File.separator).getPath();
        //return (Environment.getExternalStorageDirectory() + File.separator + ApplicationRef.APP_NAME + File.separator + "documents" + File.separator);
    }

    public static String getDocImageTempFolderName(Context context){
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + "temp" + File.separator).getPath();
        //return (Environment.getExternalStorageDirectory() + File.separator + ApplicationRef.APP_NAME + File.separator + "temp" + File.separator);
    }

    public static File getDocImageTempFolder(Context context){
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + "temp");
    }

    public static boolean checkForDownloadedImage(String url,Context context){
        String fileName = url.substring(url.lastIndexOf("/")+1,url.length());
        if(fileName.contains("?")){
            fileName = fileName.substring(0, fileName.lastIndexOf("?"));
        }
        String pathName = getDocImageFolderName(context) + "/" + fileName;
        File file = new File(pathName);
        if (file.exists()) {
            return true;
        }else{
            return false;
        }
    }

    public static ArrayList<DocumentImage> filterDownLoadedImages(ArrayList<DocumentImage> images,Context context){
        ArrayList<DocumentImage> tempArray = new ArrayList<>();
        for(int i=0;i<images.size();i++){
            if(!Utility.checkForDownloadedImage(images.get(i).getImagePath(),context)){
                tempArray.add(images.get(i));
            }
        }
        return tempArray;
    }

    public static ArrayList<DocumentImage> filterNonDownLoadedImages(ArrayList<DocumentImage> images,Context context){
        ArrayList<DocumentImage> tempArray = new ArrayList<>();
        for(int i=0;i<images.size();i++){
            if(Utility.checkForDownloadedImage(images.get(i).getImagePath(),context)){
                tempArray.add(images.get(i));
            }
        }
        return tempArray;
    }
    // RECORDS REALTED FUNCTIONS END ---------------------------------------------------------------

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

}
