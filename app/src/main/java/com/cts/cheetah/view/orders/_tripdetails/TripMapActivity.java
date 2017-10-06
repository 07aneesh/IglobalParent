package com.cts.cheetah.view.orders._tripdetails;

/**
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Color;
        import android.location.Location;
        import android.media.RingtoneManager;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.v4.app.FragmentActivity;
        import android.support.v4.app.NotificationCompat;
        import android.support.v7.app.AlertDialog;
        import android.text.Html;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.cts.cheetah.R;
        import com.cts.cheetah.helpers.DirectionsJSONParser;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
        import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
        import com.google.android.gms.location.LocationListener;
        import com.google.android.gms.location.LocationRequest;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.CircleOptions;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.maps.model.PolylineOptions;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.text.DateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.List;

/**
 * Getting Location Updates.
 *
 * Demonstrates how to use the Fused Location Provider API to get updates about a device's
 * location. The Fused Location Provider is part of the Google Play services location APIs.
 *
 * For a simpler example that shows the use of Google Play services to fetch the last known location
 * of a device, see
 * https://github.com/googlesamples/android-play-location/tree/master/BasicLocation.
 *
 * This sample uses Google Play services, but it does not require authentication. For a sample that
 * uses Google Play services for authentication, see
 * https://github.com/googlesamples/android-google-accounts/tree/master/QuickStart.
 */
public class TripMapActivity extends FragmentActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener,OnMapReadyCallback {

    protected static final String TAG = "location-updates-sample";

    LatLng proximityPoint=null;
    private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 20; // in Meters
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;

    // UI Widgets.
    protected Button mStartUpdatesButton;
    protected Button mStopUpdatesButton;
    protected TextView mLastUpdateTimeTextView;
    protected TextView mLatitudeTextView;
    protected TextView mLongitudeTextView;

    // Labels.
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected String mLastUpdateTimeLabel;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;

    private GoogleMap mMap;
    private ArrayList<LatLng> wayPoints;
    private ArrayList<Marker> markersArray;
    private LatLng startPoint;
    private LatLng endPoint;
    private int totalPoints=2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trips_map_activity);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gmap);
        mapFragment.getMapAsync(this);

        // Locate the UI widgets.
        mStartUpdatesButton = (Button) findViewById(R.id.start_updates_button);
        mStopUpdatesButton = (Button) findViewById(R.id.stop_updates_button);
        mLatitudeTextView = (TextView) findViewById(R.id.latitude_text);
        mLongitudeTextView = (TextView) findViewById(R.id.longitude_text);
        mLastUpdateTimeTextView = (TextView) findViewById(R.id.last_update_time_text);

        // Set labels.
        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mLastUpdateTimeLabel = getResources().getString(R.string.last_update_time_label);

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        buildGoogleApiClient();

        wayPoints = new ArrayList<LatLng>();
        markersArray = new ArrayList<Marker>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        addWaypoints(null,null);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                /*if (wayPoints.size()<3) {
                    drawMarker(point);
                    drawCircle(point);
                }*/

                if(totalPoints == 0) {
                    startPoint = point;
                    ++totalPoints;
                }else if(totalPoints == 1) {
                    endPoint = point;
                    ++totalPoints;
                    addWaypoints(startPoint,endPoint);
                }
            }

        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
               /*mMap.clear();

                wayPoints = new ArrayList<LatLng>();
                markersArray = new ArrayList<Marker>();*/

                //showClearAlert();
            }
        });
    }

    private void addWaypoints(LatLng origin,LatLng dest){
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin,dest);
        TripMapActivity.DownloadTask downloadTask = new TripMapActivity.DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

    }

    private void drawCircle(LatLng point){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(point);
        circleOptions.radius(MINIMUM_DISTANCECHANGE_FOR_UPDATE);
        circleOptions.strokeColor(Color.BLACK);
        circleOptions.fillColor(0x30ff0000);
        circleOptions.strokeWidth(2);
        mMap.addCircle(circleOptions);
    }

    private void drawMarker(LatLng point,String title){
        wayPoints.add(point);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.title(title);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.point_icon));
        markerOptions.snippet(Double.toString(point.latitude) + "," + Double.toString(point.longitude));
        Marker marker = mMap.addMarker(markerOptions);
        markersArray.add(marker);
    }

    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
                setButtonsEnabledState();
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
            updateUI();
            // checkProximity();
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * updates have already been requested.
     */
    public void startUpdatesButtonHandler(View view) {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            setButtonsEnabledState();
            startLocationUpdates();
        }
    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates. Does nothing if
     * updates were not previously requested.
     */
    public void stopUpdatesButtonHandler(View view) {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            setButtonsEnabledState();
            stopLocationUpdates();
        }
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        try{
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }catch (SecurityException e){

        }

    }

    /**
     * Ensures that only one button is enabled at any time. The Start Updates button is enabled
     * if the user is not requesting location updates. The Stop Updates button is enabled if the
     * user is requesting location updates.
     */
    private void setButtonsEnabledState() {
        if (mRequestingLocationUpdates) {
            mStartUpdatesButton.setEnabled(false);
            mStopUpdatesButton.setEnabled(true);
        } else {
            mStartUpdatesButton.setEnabled(true);
            mStopUpdatesButton.setEnabled(false);
        }
    }

    /**
     * Updates the latitude, the longitude, and the last location time in the UI.
     */
    private void updateUI() {
        mLatitudeTextView.setText(String.format("%s: %f", mLatitudeLabel,
                mCurrentLocation.getLatitude()));
        mLongitudeTextView.setText(String.format("%s: %f", mLongitudeLabel,
                mCurrentLocation.getLongitude()));
        mLastUpdateTimeTextView.setText(String.format("%s: %s", mLastUpdateTimeLabel,
                mLastUpdateTime));
    }

    private boolean enterStatus=false;
    private boolean exitStatus=true;
    String notificationTitle;
    String notificationContent;
    String tickerMessage;

    private void checkProximity0(){
        if(proximityPoint != null) {
            try {
                Location pointLocation = new Location("POINT_LOCATION");
                pointLocation.setLatitude(proximityPoint.latitude);
                pointLocation.setLongitude(proximityPoint.longitude);
                float distance = mCurrentLocation.distanceTo(pointLocation);
                if (distance <= 20) {
                    if (!enterStatus) {
                        Toast.makeText(TripMapActivity.this, "Distance from Point:" + distance, Toast.LENGTH_LONG).show();
                        notificationTitle = "Proximity - Entry";
                        notificationContent = "Entered the region:" + mCurrentLocation.getLatitude();
                        tickerMessage = "Entered the region:" + mCurrentLocation.getLatitude();
                        showNotification();
                        enterStatus = true;
                        exitStatus = false;
                    }

                } else if (distance > 20) {
                    if (!exitStatus) {
                        notificationTitle = "Proximity - Exit";
                        notificationContent = "Exit the region:" + mCurrentLocation.getLatitude();
                        tickerMessage = "Exit the region:" + mCurrentLocation.getLatitude();
                        showNotification();
                        exitStatus = true;
                        enterStatus = false;
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    int lastPointIndex=100;
    private void checkProximityMultiplePoints(){
       if(wayPoints.size()>0){
           try{
               for (int i=0;i<wayPoints.size();i++){
                   Location pointLocation = new Location("POINT_LOCATION");
                   pointLocation.setLatitude(wayPoints.get(i).latitude);
                   pointLocation.setLongitude(wayPoints.get(i).longitude);
                   float distance = mCurrentLocation.distanceTo(pointLocation);
                   if (distance <= 20) {
                       if (!enterStatus && lastPointIndex != i) {
                           Toast.makeText(TripMapActivity.this, "Distance from Point:" + distance, Toast.LENGTH_LONG).show();
                           notificationTitle = "Proximity - Entry";
                           notificationContent = "Entered the region:" + mCurrentLocation.getLatitude();
                           tickerMessage = "Entered the region:" + mCurrentLocation.getLatitude();
                           showNotification();
                           enterStatus = true;
                           exitStatus = false;
                           markersArray.get(i).showInfoWindow();
                           lastPointIndex = i;
                           break;
                       }
                   } else if (distance > 20) {
                       if (!exitStatus && lastPointIndex == i) {
                           notificationTitle = "Proximity - Exit";
                           notificationContent = "Exit the region:" + mCurrentLocation.getLatitude();
                           tickerMessage = "Exit the region:" + mCurrentLocation.getLatitude();
                           showNotification();
                           exitStatus = true;
                           enterStatus = false;
                           markersArray.get(lastPointIndex).hideInfoWindow();
                           lastPointIndex=100;
                           break;
                       }
                   }

               }

           }catch (Exception e){

           }
       }
    }

    private void showNotification( ){
        Intent notificationIntent = new Intent(getApplicationContext(),NotificationActivity.class);
        notificationIntent.putExtra("content", notificationContent );
        notificationIntent.setData(Uri.parse("tel:/"+ (int)System.currentTimeMillis()));
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager nManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setWhen(System.currentTimeMillis())
                .setContentText(notificationContent)
                .setContentTitle("Proximity")
                .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                .setAutoCancel(true)
                .setTicker(tickerMessage)
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        Notification notification = notificationBuilder.build();
        nManager.notify((int)System.currentTimeMillis(), notification);
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();

        super.onStop();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        try{
            if (mCurrentLocation == null) {
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateUI();
            }

        }catch (SecurityException e){

        }


        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        setMyGpsLocation(location);
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
        //checkProximity();
        checkProximityMultiplePoints();
        Toast.makeText(this, getResources().getString(R.string.location_updated_message),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    //-----------------------------------------------------------------------------------------------
    private String getDirectionsUrl(LatLng origin,LatLng dest){
        String url;
        if(origin != null && dest != null) {
            // Origin of route
            String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

            // Destination of route
            String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = str_origin + "&" + str_dest + "&" + sensor;

            // Output format
            String output = "json";

            // Building the url to the web service
            url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyApYcn6UniU2C_-WUrQhEaU6TWL7tInq1M";
            //String url = "https://maps.googleapis.com/maps/api/directions/json?origin=Disneyland&destination=Universal+Studios+Hollywood4&key=AIzaSyApYcn6UniU2C_-WUrQhEaU6TWL7tInq1M";
        }else {
            url = "https://maps.googleapis.com/maps/api/directions/json?origin=Carnival+Infopark,+Kakkanad,+Kerala&destination=Kakkanad,+Kerala&key=AIzaSyApYcn6UniU2C_-WUrQhEaU6TWL7tInq1M";
        }
        return url;
    }



    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Error downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            TripMapActivity.ParserTask parserTask = new TripMapActivity.ParserTask();

            // Invokes the thread for parsing the JSON data
            if(result != null && result != "") {
                parserTask.execute(result);
            }
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        JSONArray steps;
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);

                try {
                    // Starts parsing data
                    JSONArray bounds = new JSONArray( jObject.getString("routes"));
                    JSONObject boundsObject = (JSONObject) bounds.get(0);
                    JSONArray legs = new JSONArray(boundsObject.getString("legs"));
                    JSONObject legsObject = (JSONObject) legs.get(0);
                    steps = new JSONArray(legsObject.getString("steps"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(3);
                lineOptions.color(Color.BLUE);
            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);

            if(steps.length() >0){
                LatLng wp = null;
                for (int i=0;i<steps.length();i++){
                    try {

                        JSONObject step = (JSONObject) steps.get(i);
                        JSONObject startLocation = step.getJSONObject("start_location");
                        double lat = Double.parseDouble(startLocation.getString("lat"));
                        double lng = Double.parseDouble(startLocation.getString("lng"));
                        wp = new LatLng(lat,lng);
                        drawCircle(wp);
                        drawMarker(wp,getTextFromHtmlTag(step.getString("html_instructions")));
                        //mMap.addMarker(new MarkerOptions().position(wp).title(getTextFromHtmlTag(step.getString("html_instructions"))).icon(BitmapDescriptorFactory.fromResource(R.drawable.point_icon)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                if(wp != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(wp));
                }
            }
        }
    }


    private String getTextFromHtmlTag(String tag){
        String string;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            string = String.valueOf(Html.fromHtml(tag, Html.FROM_HTML_MODE_COMPACT));
        }else {
            string = String.valueOf(Html.fromHtml(tag));
        }
        return string;

    }

    Marker gpsMarker = null;
    Location lastGpsLocation;
    private void setMyGpsLocation(Location location){
        if(location != null) {
            lastGpsLocation = location;
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (gpsMarker != null) {
                gpsMarker.remove();
            }
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.point_icon));
            gpsMarker = mMap.addMarker(markerOptions);
        }
    }

    private void showClearAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to clear current path and add new one?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mMap.clear();
                        totalPoints = 0;
                        wayPoints = new ArrayList<LatLng>();
                        markersArray = new ArrayList<Marker>();
                }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }
    //-----------------------------------------------------------------------------------------------
}
