package com.cts.cheetah.view.gps;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cts.cheetah.BuildConfig;
import com.cts.cheetah.R;
import com.cts.cheetah.components.CustomAlertDialog;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.DocumentImageHelper;
import com.cts.cheetah.helpers.ImageProcessor;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.interfaces.IBaseView;
import com.cts.cheetah.interfaces.ICustomDialogDismiss;
import com.cts.cheetah.interfaces.IImageDownload;
import com.cts.cheetah.model.DocumentImage;
import com.cts.cheetah.model.DriverAccount;
import com.cts.cheetah.view.accounts.DriverAccountController;
import com.cts.cheetah.view.accounts.DriverAccountListAdapter;
import com.cts.cheetah.view.accounts.IDriverAccount;
import com.cts.cheetah.view.accounts.logout.LogoutController;
import com.cts.cheetah.view.main.MainDashboardActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GPSFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GPSFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GPSFragment extends Fragment implements  IGPS, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    JSONObject jsonobject;
    JSONArray jsonarray;
    KeyValue[]  dailyTrips= null;
    View rootView;
    Spinner mySpinner;
    GPSController gpsController;
    GoogleMap map;

    //................................. New
    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    Geocoder geocoder;
    private SpinAdapter adapter;

    public GPSFragment() {
        // Required empty public constructor
    }

    public static GPSFragment newInstance() {
        GPSFragment gpsFragment = new GPSFragment();
        return gpsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gpsController = new GPSController(this, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try
        {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_gps, container, false);
        GetStudentDailyTrip();

            mySpinner = (Spinner) rootView.findViewById(R.id.my_spinner);
            mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View arg1, int position, long arg3) {
                            mMap.clear();                          
                           // int vehicleTripDetailsId = dailyTrips.get(position).getVehicleTripDetailsId();
                           // JsonObject trilLatLong1 =  mySpinner.getItemAtPosition(position);

                            // Log.i("Id:", mSelected.getId());
                            // Here you get the current item (a User object) that is selected by its position
                            KeyValue trip = adapter.getItem(position);
                            int vehicleTri = adapterView.getId();
                            String bb =  adapterView.getItemAtPosition(position).toString();
                            int vehicleTripDetailsId = trip.getId();
                            GetLatitudeAndLongitudeBasedOnTrip(vehicleTripDetailsId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "onCreateView - " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return rootView;
    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap)
//    {
//        try {
//            map = googleMap;
//            LatLng latLong = new LatLng(40.68, -74.04);
//            MarkerOptions options = new MarkerOptions();
//            options.position(latLong).title("vytilla");
//            map.addMarker(options);
//            map.moveCamera(CameraUpdateFactory.newLatLng(latLong));
//        } catch (Exception ex) {
//            Toast.makeText(getActivity().getApplicationContext(), "onMapReady - " + ex.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    }

//    @Override
//    public void onViewCreated(View v, Bundle savedInstanceState) {
//        try {
//            super.onViewCreated(v,savedInstanceState);
//
////            SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
////            mapFragment.getMapAsync(this);
//        } catch (Exception ex) {
//            Toast.makeText(getActivity().getApplicationContext(), "onViewCreated - " + ex.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    }

    private void GetStudentDailyTrip()
    {
        gpsController.GetStudentDailyTrip();
    }

    @Override
    public void onStudentDailyTripData(JSONObject result) {
        try {
            JSONObject resultObject = result.getJSONObject("results");
            JSONArray trips = resultObject.getJSONArray("Trips");
            if(trips != null) {
                dailyTrips = new KeyValue[trips.length()];
                    for (int i = 0; i < trips.length(); i++) {
                        JSONObject trip = (JSONObject) trips.get(i);
                        KeyValue tripVM = new KeyValue();
                        tripVM.setId(trip.getInt("VehicleTripDetailsId"));
                        tripVM.setValue(trip.getString("VehicleTripName"));
                        dailyTrips[i] = tripVM;
                      //  ddlTrips.add(trip.getString("VehicleTripName"));
                    }
               // mySpinner.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ddlTrips));
                adapter = new SpinAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, dailyTrips);

                mySpinner.setAdapter(adapter); // Set the custom adapter to the spinner
               // mySpinner.setAdapter(new ArrayAdapter<TripVM>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, dailyTrips));
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onStudentDailyTripError(String error) {
        try {
            if (error.equals(ApplicationRef.Service.INVALID_CODE)) {
                ((IBaseView) getActivity()).onInvalidSession();
            } else {
                ((IBaseView) getActivity()).onError(error);
            }
        }catch (Exception e){

        }

    }

    @Override
    public void onTripLatitudeLongitudeData(JSONObject result) {
        try {
            JSONObject resultObject = result.getJSONObject("results");
            JSONArray trips = resultObject.getJSONArray("Trips");
            MarkerPoints = new ArrayList<>();
            if (trips != null) {

                for (int i = 0; i < trips.length(); i++) {
                    JSONObject trip = (JSONObject) trips.get(i);
                    String latitude = trip.getString("Latitude");
                    String longitude = trip.getString("Longitude");
                    MarkerPoints.add(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
                }
            }
            List<Address> addresses = null;
            //Initialize Google Play Services
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
            if (MarkerPoints.size() >= 2) {
                //   for (LatLng point : MarkerPoints) {
                for (int i = 0; i < MarkerPoints.size() - 1; i++) {
                    // Do something with the value
                    LatLng origin = MarkerPoints.get(i);
                    LatLng dest = MarkerPoints.get(i + 1);
                    geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                    if (i == 0) {
                        try {
                            addresses = geocoder.getFromLocation(origin.latitude, origin.longitude, 1);

                        } catch (IOException ioException) {
//                        errorMessage = "Service Not Available";
                            // Log.e(TAG, errorMessage, ioException);
                        } catch (IllegalArgumentException illegalArgumentException) {
//                        errorMessage = "Invalid Latitude or Longitude Used";
//                        Log.e(TAG, errorMessage + ". " +
//                                "Latitude = " + location.getLatitude() + ", Longitude = " +
//                                location.getLongitude(), illegalArgumentException);
                        } catch (Exception ex) {
                            String error = ex.getMessage();
                            Toast.makeText(getActivity().getApplicationContext(), "getFromLocation " + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        if (addresses == null || addresses.size() == 0) {
                        } else {
                            Address address = addresses.get(0);
                            ArrayList<String> addressFragments = new ArrayList<String>();

                            // Fetch the address lines using getAddressLine,
                            // join them, and send them to the thread.
                            for (int j = 0; j <= address.getMaxAddressLineIndex(); j++) {
                                addressFragments.add(address.getAddressLine(j));
                            }

                            //String combinedAddress = android.text.TextUtils.join(",", addressFragments);
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String zip = addresses.get(0).getPostalCode();
                            String country = addresses.get(0).getCountryName();
                            String combinedAddress = city +","+state;
                            mMap.addMarker(new MarkerOptions()
                                    .position(origin)
                                    .title("First Point(" + combinedAddress + ")"));
                            // .icon(BitmapDescriptorFactory.fromResource(userIcon))
                            //.setSnippet("Your first recorded location");
                        }

                    }
                    if (i == MarkerPoints.size() - 2) {
                        try {
                            addresses = geocoder.getFromLocation(dest.latitude, dest.longitude, 1);
                        } catch (IOException ioException) {
//                        errorMessage = "Service Not Available";
//                        Log.e(TAG, errorMessage, ioException);
                        } catch (IllegalArgumentException illegalArgumentException) {
//                        errorMessage = "Invalid Latitude or Longitude Used";
//                        Log.e(TAG, errorMessage + ". " +
//                                "Latitude = " + location.getLatitude() + ", Longitude = " +
//                                location.getLongitude(), illegalArgumentException);
                        }
                        if (addresses == null || addresses.size() == 0) {
                        } else {
                            Address address = addresses.get(0);
                            ArrayList<String> addressFragments = new ArrayList<String>();

                            // Fetch the address lines using getAddressLine,
                            // join them, and send them to the thread.
                            for (int j = 0; j <= address.getMaxAddressLineIndex(); j++) {
                                addressFragments.add(address.getAddressLine(j));
                            }

                            //String combinedAddress = android.text.TextUtils.join(",", addressFragments);
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String zip = addresses.get(0).getPostalCode();
                            String country = addresses.get(0).getCountryName();
                            String combinedAddress = city +","+state;
                            mMap.addMarker(new MarkerOptions()
                                    .position(dest)
                                    .title("Last Point(" + combinedAddress + ")"));
                            // .icon(BitmapDescriptorFactory.fromResource(userIcon))
                            // .setSnippet("Your last recorded location");
                        }
                    }


                    try {
                        addresses = geocoder.getFromLocation(dest.latitude, dest.longitude, 1);
                    } catch (IOException ioException) {
//                        errorMessage = "Service Not Available";
//                        Log.e(TAG, errorMessage, ioException);
                    } catch (IllegalArgumentException illegalArgumentException) {
//                        errorMessage = "Invalid Latitude or Longitude Used";
//                        Log.e(TAG, errorMessage + ". " +
//                                "Latitude = " + location.getLatitude() + ", Longitude = " +
//                                location.getLongitude(), illegalArgumentException);
                    }
                    if (addresses == null || addresses.size() == 0) {
                    } else {
                        Address address = addresses.get(0);
                        ArrayList<String> addressFragments = new ArrayList<String>();

                        // Fetch the address lines using getAddressLine,
                        // join them, and send them to the thread.
                        for (int j = 0; j <= address.getMaxAddressLineIndex(); j++) {
                            addressFragments.add(address.getAddressLine(j));
                        }

                        //String combinedAddress = android.text.TextUtils.join(",", addressFragments);
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String zip = addresses.get(0).getPostalCode();
                        String country = addresses.get(0).getCountryName();
                        String combinedAddress = city +","+state;
                        mMap.addMarker(new MarkerOptions()
                                .position(dest)
                                .title(combinedAddress));
                        // .icon(BitmapDescriptorFactory.fromResource(userIcon))
                        // .setSnippet("Your last recorded location");
                    }


                    String url = getUrl(origin, dest);
                    Log.d("onMapClick", url.toString());
                    FetchUrl FetchUrl = new FetchUrl();
                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                }
            } else {
                // Do something with the value
                LatLng origin = MarkerPoints.get(0);
                geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(origin.latitude, origin.longitude, 1);

                    } catch (IOException ioException) {
//                        errorMessage = "Service Not Available";
                        // Log.e(TAG, errorMessage, ioException);
                    } catch (IllegalArgumentException illegalArgumentException) {
//                        errorMessage = "Invalid Latitude or Longitude Used";
//                        Log.e(TAG, errorMessage + ". " +
//                                "Latitude = " + location.getLatitude() + ", Longitude = " +
//                                location.getLongitude(), illegalArgumentException);
                    } catch (Exception ex) {
                        String error = ex.getMessage();
                        Toast.makeText(getActivity().getApplicationContext(), "getFromLocation " + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    if (addresses == null || addresses.size() == 0) {
                    } else //Only one point
                        {
                        Address address = addresses.get(0);
                        ArrayList<String> addressFragments = new ArrayList<String>();

                        // Fetch the address lines using getAddressLine,
                        // join them, and send them to the thread.
                        for (int j = 0; j <= address.getMaxAddressLineIndex(); j++) {
                            addressFragments.add(address.getAddressLine(j));
                        }

                        //String combinedAddress = android.text.TextUtils.join(",", addressFragments);
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                            String combinedAddress = city +","+state;
//                        String zip = addresses.get(0).getPostalCode();
//                        String country = addresses.get(0).getCountryName();
                        mMap.addMarker(new MarkerOptions()
                                .position(origin)
                                .title("First Point(" + combinedAddress + ")"));
                        // .icon(BitmapDescriptorFactory.fromResource(userIcon))
                        //.setSnippet("Your first recorded location");
                    }
            }
        }catch (Exception ex){
            Toast.makeText(getActivity().getApplicationContext(), "getFromLocation "+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTripLatitudeLongitudeError(String error) {
        try {
            if (error.equals(ApplicationRef.Service.INVALID_CODE)) {
                ((IBaseView) getActivity()).onInvalidSession();
            } else {
                ((IBaseView) getActivity()).onError(error);
            }
        }catch (Exception e){

        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void GetLatitudeAndLongitudeBasedOnTrip(int vehicleTripDetailsId)
    {
        gpsController.GetLatitudeAndLongitudeBasedOnTrip(vehicleTripDetailsId);
    }


    //............................................. New code ......................................................
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        try {
            super.onViewCreated(v,savedInstanceState);

//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                checkLocationPermission();
//            }
            // Initializing
            MarkerPoints = new ArrayList<>();

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } catch (Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "onViewCreated - " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setMyLocationEnabled(true);

        //add this here:
        //buildGoogleApiClient();

    }

    private String getUrl(LatLng origin, LatLng dest) {

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
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
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
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
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
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//        if (mLastLocation != null) {
//          Double  lat = mLastLocation.getLatitude();
//            Double lng = mLastLocation.getLongitude();
//
//            LatLng loc = new LatLng(lat, lng);
//            mMap.addMarker(new MarkerOptions().position(loc).title("Current Location"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
//        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

//        mLastLocation = location;
//        if (mCurrLocationMarker != null) {
//            mCurrLocationMarker.remove();
//        }

        //Place current location marker
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//        mCurrLocationMarker = mMap.addMarker(markerOptions);
//
//        //move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
//
//        //stop location updates
//        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
//    public boolean checkLocationPermission(){
//        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Asking user if explanation is needed
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity().getApplicationContext(),
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//                //Prompt the user once explanation has been shown
//                ActivityCompat.requestPermissions(getActivity().getApplicationContext(),
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//
//
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(getActivity().getApplicationContext(),
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getActivity().getApplicationContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

}

