package com.cts.cheetah.helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.cts.cheetah.R;
import com.cts.cheetah.service.IBaseService;
import com.cts.cheetah.view.orders.mytrips.maps.Route.NotificationActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;


public class PositionUploadService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

	Context context;
	Double lat;
	Double lon;
	String tripId;
	String locationOrder;
	String locationId;
	String locationType;
	String locationLatitude;
	String locationLongitude;
	String statusCode;
	final long MINIMUM_TIME_INTERVAL=0;//in milliseconds
	final float MINIMUM_DISTANCE_INTERVAL = 500;//in meters
	boolean checkProximity = true;


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.tripId = intent.getStringExtra(ApplicationRef.TripTags.TRIP_ID);
		this.statusCode = intent.getStringExtra(ApplicationRef.StatusCodes.STATUS_CODE);
		this.locationOrder=intent.getStringExtra(ApplicationRef.TripTags.LOCATION_ORDER);
		this.locationId=intent.getStringExtra(ApplicationRef.TripTags.LOCATION_ID);
		this.locationType=intent.getStringExtra(ApplicationRef.TripTags.LOCATION_TYPE);
		this.lat = intent.getDoubleExtra(ApplicationRef.TripTags.LATITUDE,0);
		this.lon = intent.getDoubleExtra(ApplicationRef.TripTags.LONGITUDE,0);
		this.locationLatitude=String.valueOf(lat);
		this.locationLongitude=String.valueOf(lon);
		proximityPoint = new LatLng(lat,lon);
		context = getApplicationContext();
		if(lat == 0.0 || lon == 0.0){
			checkProximity = false;
		}
		buildGoogleApiClient();
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		stopLocationUpdates();
		super.onTaskRemoved(rootIntent);
		stopSelf();

	}

	@Override
	public void onDestroy(){
		stopLocationUpdates();
	}


	private void sendPosition(){
		IBaseService iBaseService = new IBaseService() {
			@Override
			public void onSuccess(JSONObject result) throws JSONException {
				//Toast.makeText(context,"Pos updation success",Toast.LENGTH_SHORT).show();
 			}

			@Override
			public void onError(String error) {
				Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
			}
		};

		if (Utility.haveNetworkConnectivity(context)) {
			try {
				int method = com.android.volley.Request.Method.POST;
				String jsonData=getTripAcceptStatus(tripId, statusCode, String.valueOf(lat),String.valueOf(lon));
				com.cts.cheetah.service.Service service = new com.cts.cheetah.service.Service();
				service.sendJsonObject(ApplicationRef.Service.ADD_DRIVER_LOCATION,jsonData,iBaseService,context,Utility.getAccessToken(context),method);
			} catch (Exception e) {
				Utility.logger(e);
			}
		} else {
			Toast.makeText(context,context.getString(R.string.service_error_noConnection),Toast.LENGTH_SHORT).show();
		}
	}

	private String getTripAcceptStatus(String tripId,String statusCode,String latitude,String longitude){
		String jsonString =  "{\"orderId\":\"" + tripId + "\", \"orderStatusId\":\"" + statusCode + "\",\"latitude\":\"" + latitude + "\", \"longitude\":" + longitude + "\"}";
		return jsonString;
	}


	//Googple API callbacks start ------------------------------------------------------------------
	protected static final String TAG = "location-updates-sample";
	protected GoogleApiClient mGoogleApiClient;

	protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
	protected final static String LOCATION_KEY = "location-key";
	protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
	LatLng proximityPoint;
	private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 100; // in Meters
	//The desired interval for location updates. Inexact. Updates may be more or less frequent.
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 60000;
	//The fastest rate for active location updates. Exact. Updates will never be more frequent
	//than this value.
	public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
	//Stores parameters for requests to the FusedLocationProviderApi.
	protected LocationRequest mLocationRequest;

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
		mGoogleApiClient.connect();

	}

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
	 * Requests location updates from the FusedLocationApi.
	 */
	protected void startLocationUpdates() {
		// The final argument to {@code requestLocationUpdates()} is a LocationListener
		// (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
		try {
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
		}catch (SecurityException e){

		}
	}

	protected void stopLocationUpdates() {
		try {
			if (mGoogleApiClient.isConnected()) {
				LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
			}
		}catch (SecurityException e){

		}
	}

	@Override
	public void onLocationChanged(Location location) {
		mCurrentLocation = location;
		lat = location.getLatitude();
		lon = location.getLongitude();
		sendPosition();
		if(checkProximity) {
			checkProximity();
		}
	}


	@Override
	public void onConnected(@Nullable Bundle bundle) {
		try {
			if (mCurrentLocation == null) {
				mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
				startLocationUpdates();
			}
		}catch (SecurityException e){
		}
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.i(TAG, "Connection suspended");
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
	}

	private boolean enterStatus=false;
	private boolean exitStatus=true;
	String notificationTitle;
	String notificationContent;
	String tickerMessage;
	protected Location mCurrentLocation;

	private void checkProximity(){
		if(proximityPoint != null) {
			try {
				Location pointLocation = new Location("POINT_LOCATION");
				pointLocation.setLatitude(proximityPoint.latitude);
				pointLocation.setLongitude(proximityPoint.longitude);
				float distance = mCurrentLocation.distanceTo(pointLocation);
				if (distance <= 500) {
					if (!enterStatus) {
						Toast.makeText(context, "Distance from Point:" + distance, Toast.LENGTH_LONG).show();
						notificationTitle = "Proximity - Entry";
						notificationContent = "Entered the region:" + mCurrentLocation.getLatitude();
						tickerMessage = "Entered the region:" + mCurrentLocation.getLatitude();
						showNotification();
						sendLocalBroadcast();
						enterStatus = true;
						exitStatus = false;
					}

				} else if (distance > 20) {
                    /*if (!exitStatus) {
                        notificationTitle = "Proximity - Exit";
                        notificationContent = "Exit the region:" + mCurrentLocation.getLatitude();
                        tickerMessage = "Exit the region:" + mCurrentLocation.getLatitude();
                        showNotification();
                        exitStatus = true;
                        enterStatus = false;
                    }*/
				}
			} catch (Exception e) {

			}
		}
	}

	private void sendLocalBroadcast(){
		Intent bIntent = new Intent(ApplicationRef.TripTags.PROXIMITY_REACHED);
		bIntent.putExtra(ApplicationRef.TripTags.LOCATION_ID, locationId);
		bIntent.putExtra(ApplicationRef.TripTags.LOCATION_TYPE, locationType);
		LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(PositionUploadService.this.getApplicationContext());
		broadcaster.sendBroadcast(bIntent);
	}

	private void showNotification( ){
		Intent notificationIntent = new Intent(getApplicationContext(),NotificationActivity.class);
		notificationIntent.putExtra("content", notificationContent );
		notificationIntent.setData(Uri.parse("tel:/"+ (int) System.currentTimeMillis()));
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
		nManager.notify((int) System.currentTimeMillis(), notification);
	}


	//Googple API callbacks end ------------------------------------------------------------------
}
