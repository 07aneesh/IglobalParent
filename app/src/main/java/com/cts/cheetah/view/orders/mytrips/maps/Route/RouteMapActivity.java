package com.cts.cheetah.view.orders.mytrips.maps.Route;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cts.cheetah.R;
import com.cts.cheetah.components.LoaderDialogFragment;
import com.cts.cheetah.components.OptionsAlertDialog;
import com.cts.cheetah.helpers.AppSingleton;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.DirectionsJSONParser;
import com.cts.cheetah.helpers.RejectReasonHelper;
import com.cts.cheetah.helpers.StatusCodeManager;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.interfaces.IBaseView;
import com.cts.cheetah.interfaces.IOptionsDialogDismiss;
import com.cts.cheetah.model.ICommonDataListener;
import com.cts.cheetah.model.IssueItem;
import com.cts.cheetah.model.MyTripDAO;
import com.cts.cheetah.view.orders.mytrips.maps.location.LocationMapActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
//


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class RouteMapActivity extends FragmentActivity implements OnMapReadyCallback,IBaseView,IRouteMap {
    //ConnectionCallbacks, OnConnectionFailedListener, LocationListener,
    LoaderDialogFragment loaderFragment;
    private GoogleMap mMap;
    LatLng origin;
    LatLng dest;
    private TextView toolBarTitle;
    TextView locationTypeTv,tripDate,tripAddress,contactPerson,contactNo;
    ImageView focusToRouteBtn,mapReportBtn;
    ArrayList<IssueItem> rejectionReasonsList;
    static final int REJECT_REASON_OPTIONS = 2;
    String tripId;
    boolean isRoundTrip=false;
    String controlNo;
    String currentLocationId;
    String currentLocationOrder;
    String currentLocationType;
    String currentLocationLatitude;
    String currentLocationLongitude;
    String pendingStatusCode;
    String processedStatusCode;
    //
    String currentStatus;
    String currentStatusCode;
    RouteMapController routeMapController;
    ArrayList<com.cts.cheetah.model.Location> myLocationsArrayList;
    BroadcastReceiver mProximityBroadcastReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        locationTypeTv = (TextView) findViewById(R.id.locationType);
        tripDate = (TextView) findViewById(R.id.tripDate);
        tripAddress = (TextView) findViewById(R.id.tripAddress);
        contactPerson = (TextView) findViewById(R.id.contactPerson);
        contactNo = (TextView) findViewById(R.id.contactNo);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolBarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.findViewById(R.id.common_appbar_icons).setVisibility(View.INVISIBLE);

        mapReportBtn = (ImageView) findViewById(R.id.mapReportBtn);
        mapReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsAlert();
            }
        });

        Button arrivedButton = (Button) findViewById(R.id.arrivedBtn);
        arrivedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocationActivity();
            }
        });

        FloatingActionButton navigationBtn = (FloatingActionButton) findViewById(R.id.fab_call);
        navigationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "google.navigation:q="+ origin.latitude + "," + origin.longitude + "&mode=d";
                Uri gmmIntentUri = Uri.parse(url);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        focusToRouteBtn = (ImageView) findViewById(R.id.focusToRouteBtn);
        ImageView homeButton = (ImageView) toolbar.findViewById(R.id.ic_home_btn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            tripId = bundle.getString(ApplicationRef.TripTags.TRIP_ID);
            controlNo = bundle.getString(ApplicationRef.TripTags.TRIP_CONTROL_NO);
            toolBarTitle.setText("ORDER " + controlNo);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.gmap);
        mapFragment.getMapAsync(this);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);

        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight( (int) getResources().getDimension(R.dimen.peek_height));

        //buildGoogleApiClient();
        currentStatus=ApplicationRef.StatusCodes.ORDER_STATUS_IN_TRANSIT;
        currentStatusCode= StatusCodeManager.getStatusCode(this,ApplicationRef.StatusCodes.ORDER_STATUS,currentStatus);
        pendingStatusCode = StatusCodeManager.getStatusCode(this,ApplicationRef.StatusCodes.ORDER_STATUS,ApplicationRef.StatusCodes.LOCATION_STATUS_PENDING);
        processedStatusCode = StatusCodeManager.getStatusCode(this,ApplicationRef.StatusCodes.ORDER_STATUS,ApplicationRef.StatusCodes.LOCATION_STATUS_PROCESSED);

        routeMapController = new RouteMapController(this,this);
        rejectionReasonsList = new ArrayList<>();

        //SEND TRIP STATUS - ie. inTransit
        routeMapController.sendIssueOrStatus(tripId,"","",currentStatusCode,"","","","","");
    }

    private void getData(){
        showLoader();
        final RejectReasonHelper rejectReasonHelper = new RejectReasonHelper(this);
        ICommonDataListener iCommonDataListener = new ICommonDataListener() {
            @Override
            public void onCommonDataSuccess(String api, JSONObject object) {
                rejectionReasonsList = rejectReasonHelper.getRejectionListArray(object);
                hideLoader();
                routeMapController.getMyTripsData(tripId);
            }

            @Override
            public void onCommonDataError(String api, String error) {
                onError(error);
            }
        };
        rejectReasonHelper.getRejectReason(ApplicationRef.Service.ISSUE,iCommonDataListener);

    }

    private void showOptionsAlert(){
        //
        IOptionsDialogDismiss iOptionsDialogDismiss = new IOptionsDialogDismiss() {
            @Override
            public void onOptionsDismiss(int identifier, int position, String reason) {
                String statusCode = ApplicationRef.Service.ISSUE;
                String reasonId = String.valueOf(rejectionReasonsList.get(position).getId());
                String state = currentStatus;
                routeMapController.sendIssueOrStatus(tripId,"","",statusCode,reason,reasonId,state,"","");
            }
        };
        ArrayList<String> tempRejectionReasonsList = RejectReasonHelper.getTempRejectionListArray(rejectionReasonsList);
        String reasons[] = tempRejectionReasonsList.toArray(new String[tempRejectionReasonsList.size()]);
        OptionsAlertDialog optionsAlertDialog = new OptionsAlertDialog(RouteMapActivity.this,iOptionsDialogDismiss);
        optionsAlertDialog.showRadioOptionsDialog(getResources().getString(R.string.trip_reject),reasons,true,REJECT_REASON_OPTIONS);
    }


    public void showLocationActivity() {
        Intent intent = new Intent(RouteMapActivity.this,LocationMapActivity.class);
        intent.putExtra(ApplicationRef.TripTags.TRIP_ID,tripId);
        intent.putExtra(ApplicationRef.TripTags.TRIP_CONTROL_NO,controlNo);
        intent.putExtra(ApplicationRef.TripTags.LOCATION_ORDER,currentLocationOrder);
        intent.putExtra(ApplicationRef.TripTags.LOCATION_ID,currentLocationId);
        intent.putExtra(ApplicationRef.TripTags.LOCATION_TYPE,currentLocationType);
        intent.putExtra(ApplicationRef.TripTags.LATITUDE,currentLocationLatitude);
        intent.putExtra(ApplicationRef.TripTags.LONGITUDE,currentLocationLongitude);
        startActivity(intent);
        finish();
    }

    @Override
    public void onIssueOrStatusSuccess(JSONObject object) {

    }

    @Override
    public void onIssueOrStatusError(String error) {
        onError(error);
    }

    @Override
    public void onTripDataSuccess(JSONObject object) {
        try {
            if (object != null) {
                JSONObject result = new JSONObject(object.getString("results"));

                myLocationsArrayList = new ArrayList<>();
                MyTripDAO myTripDAO;
                myTripDAO = new MyTripDAO();
                myTripDAO.setData(result, true);
                myLocationsArrayList = myTripDAO.getLocations();
                tripId = myTripDAO.getTripId();
                isRoundTrip = myTripDAO.isRoundTrip();
                setLocationValues();
                getRoute();
            }
        }catch (JSONException e){

        }
    }

    private void setLocationValues(){
        try {
            for (int i = 0; i < myLocationsArrayList.size(); i++) {
                if (myLocationsArrayList.get(i).getLocationStatus().equals("")) {
                    currentLocationId = myLocationsArrayList.get(i).getLocationId();
                    currentLocationType = myLocationsArrayList.get(i).getLocationType();
                    currentLocationLatitude = myLocationsArrayList.get(i).getLatitude();
                    currentLocationLongitude = myLocationsArrayList.get(i).getLongitude();
                    currentLocationOrder = myLocationsArrayList.get(i).getLocationOrder();
                    break;
                }
            }

            int count;
            if(isRoundTrip){
               count =1;
            }else{
                count = myLocationsArrayList.size() - 1;
            }

            origin = new LatLng(Double.valueOf(myLocationsArrayList.get(0).getLatitude()), Double.valueOf(myLocationsArrayList.get(0).getLongitude()));
            dest = new LatLng(Double.valueOf(myLocationsArrayList.get(count).getLatitude()), Double.valueOf(myLocationsArrayList.get(count).getLongitude()));
            startLocationUpdate();
        }catch (Exception e){

        }
    }

    @Override
    public void onTripDataError(String error) {
        onError(error);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            mMap.setMyLocationEnabled(true);
            //setMyGpsLocation();
            getData();
        } catch (SecurityException e) {
            e.printStackTrace();
        }


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String locationId = marker.getTitle().split(" ")[1];
                for(int i=0;i<myLocationsArrayList.size();i++){
                    if(myLocationsArrayList.get(i).getLocationOrder().equals(locationId)){
                        locationTypeTv.setText(myLocationsArrayList.get(i).getLocationType());
                        tripDate.setText(myLocationsArrayList.get(i).getLocationType());
                        tripAddress.setText(myLocationsArrayList.get(i).getAddress());
                        contactPerson.setText(myLocationsArrayList.get(i).getContactPerson());
                        contactNo.setText(myLocationsArrayList.get(i).getContactNo());
                    }
                }
                return false;
            }
        });

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                focusToRouteBtn.setVisibility(View.VISIBLE);
            }
        });


        focusToRouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomMap();
                focusToRouteBtn.setVisibility(View.INVISIBLE);
            }
        });

    }

    /*private void setMyGpsLocation(){
        try {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f));
            }
        }catch (SecurityException e){

        }
    }*/

    private void startLocationUpdate(){
        if(currentLocationLongitude != null && currentLocationLongitude != null) {

            mProximityBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    LocalBroadcastManager.getInstance(RouteMapActivity.this).unregisterReceiver(mProximityBroadcastReceiver);
                    showLocationActivity();
                }
            };
            LocalBroadcastManager.getInstance(this).registerReceiver(mProximityBroadcastReceiver,
                    new IntentFilter(ApplicationRef.TripTags.PROXIMITY_REACHED));

            LatLng latLng = new LatLng(Double.valueOf(currentLocationLatitude), Double.valueOf(currentLocationLongitude));
            AppSingleton.getInstance(this).startPositionUploadService(tripId, latLng);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    //----------------------------------------------------------------------------------------------
    //Draw location on map

    private void getRoute(){
        // Getting URL to the Google Directions API
        if(origin != null && dest != null && !origin.equals("null") && !dest.equals("null")) {
            String url = getDirectionsUrl(origin, dest);
            DownloadTask downloadTask = new DownloadTask();
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }

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

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
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
            try {
                ArrayList<LatLng> points = null;
                PolylineOptions lineOptions = null;
                MarkerOptions markerOptions = new MarkerOptions();

                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {

                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    if(i==0){
                        HashMap<String, String> point = path.get(0);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));

                    }

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
                    lineOptions.width(8);
                    lineOptions.color(getResources().getColor(R.color.map_path_color));
                }

                // Drawing polyline in the Google Map for the i-th route
                mMap.addPolyline(lineOptions);


                //Steps are direction turn points
                if (steps.length() > 0) {
                    LatLng wp = null;
                    for (int i = 0; i < steps.length(); i++) {
                        try {

                            JSONObject step = (JSONObject) steps.get(i);
                            JSONObject startLocation = step.getJSONObject("start_location");
                            double lat = Double.parseDouble(startLocation.getString("lat"));
                            double lng = Double.parseDouble(startLocation.getString("lng"));
                            wp = new LatLng(lat, lng);
                            //mMap.addMarker(new MarkerOptions().position(wp).title(getTextFromHtmlTag(step.getString("html_instructions"))).icon(BitmapDescriptorFactory.fromResource(R.drawable.point_icon)));
                            //addProximity(wp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    if (wp != null) {
                        zoomMap();
                        String title="";
                        String label="";
                        LatLng latLng;

                        int count;
                        if(isRoundTrip){
                            count = 2;
                        }else{
                            count = myLocationsArrayList.size();
                        }
                        for (int i=0;i<count;i++){
                            label = myLocationsArrayList.get(i).getLocationType() + " " + myLocationsArrayList.get(i).getLocationOrder();
                            if(myLocationsArrayList.get(i).getLocationType().equalsIgnoreCase(ApplicationRef.TripTags.LOCATION_TYPE_PICK_UP)) {
                                title = "P" + myLocationsArrayList.get(i).getLocationOrder();
                            }else if(myLocationsArrayList.get(i).getLocationType().equalsIgnoreCase(ApplicationRef.TripTags.LOCATION_TYPE_DROP_OFF)){
                                title = "D" + myLocationsArrayList.get(i).getLocationOrder();
                            }
                            latLng = new LatLng(Double.parseDouble(myLocationsArrayList.get(i).getLatitude()),Double.parseDouble(myLocationsArrayList.get(i).getLongitude()));
                            addCustomMarker(latLng, label, Utility.getCustomMarkerIcon(RouteMapActivity.this, myLocationsArrayList.get(i).getLocationType(),myLocationsArrayList.get(i).getLocationStatus(),title));
                        }
                    }

                }
            }catch (Exception e){

            }
        }
    }


    private void zoomMap(){
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin,15));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(origin);
        builder.include(dest);
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,90));
    }

    private void addCustomMarker(LatLng point,String title,Bitmap bm){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.title(title);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bm));
        mMap.addMarker(markerOptions);
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

    private String getDirectionsUrl(LatLng origin,LatLng dest){
        int count = myLocationsArrayList.size();
        String parameters=null;

        String way_point="waypoints=";
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
        String str_dest = "destination="+dest.latitude+","+dest.longitude;
        if(isRoundTrip){
            parameters = str_origin+"&"+str_dest+"&key="+getResources().getString(R.string.google_api_key);

        }else{
            for (int i=1;i<count-1;i++){
                way_point += myLocationsArrayList.get(i).getLatitude()+","+myLocationsArrayList.get(i).getLongitude();
            }
            parameters = str_origin+"&"+str_dest+"&"+way_point+"&key="+getResources().getString(R.string.google_api_key);
        }

        return "https://maps.googleapis.com/maps/api/directions/json?" + parameters;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    public void onError(String error) {
        hideLoader();
        if(error.equals(ApplicationRef.Service.INVALID_CODE)){
            onInvalidSession();
        }else {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onInvalidSession() {
        Toast.makeText(this, R.string.invalid_session, Toast.LENGTH_SHORT).show();
        Utility.handleInvalidSession(this);
    }

    @Override
    public void showLoader() {
        try {
            if (loaderFragment == null)
                loaderFragment = new LoaderDialogFragment();
            if (!loaderFragment.isVisible())
                loaderFragment.show(getSupportFragmentManager(), getString(R.string.login_loader_loaderTag));
        }catch (IllegalStateException e){
            Log.i("Loader Frag ill excpn",e+"");
        }catch (Exception e){
            Log.i("Loader Frag excpn",e+"");
        }
    }

    @Override
    public void hideLoader() {
        try {
            if (loaderFragment != null){
                loaderFragment.dismiss();
            }
        } catch (IllegalStateException ie) {
            Utility.logger(ie);
        }
    }
}
