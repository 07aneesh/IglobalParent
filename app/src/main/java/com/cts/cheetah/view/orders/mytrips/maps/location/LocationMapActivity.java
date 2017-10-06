package com.cts.cheetah.view.orders.mytrips.maps.location;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.cts.cheetah.helpers.RejectReasonHelper;
import com.cts.cheetah.helpers.StatusCodeManager;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.interfaces.IBaseView;
import com.cts.cheetah.interfaces.IOptionsDialogDismiss;
import com.cts.cheetah.model.ICommonDataListener;
import com.cts.cheetah.model.IssueItem;
//import com.cts.cheetah.view.orders.mytrips.arrival.TripArrivedActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LocationMapActivity extends AppCompatActivity implements OnMapReadyCallback,IBaseView,ILocationMap {

    private TextView toolBarTitle;
    private GoogleMap mMap;
    ImageView focusToRouteBtn,mapReportBtn;
    Button arrivedBtn;
    LatLng origin;
    String locationType;
    String locationLatitude;
    String locationLongitude;
    String locationOrder;
    String locationId;
    String locationStatus="";
    String tripId;
    String controlNo;
    ArrayList<IssueItem> rejectionReasonsList;
    static final int REJECT_REASON_OPTIONS = 2;
    String currentStatus;
    String currentStatusCode;
    LoaderDialogFragment loaderFragment;
    LocationMapActivityController locationMapActivityController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_map);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolBarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.findViewById(R.id.common_appbar_icons).setVisibility(View.INVISIBLE);

        ImageView homeButton = (ImageView) toolbar.findViewById(R.id.ic_home_btn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        focusToRouteBtn = (ImageView) findViewById(R.id.focusToRouteBtn);

        arrivedBtn = (Button) findViewById(R.id.arrivedBtn);
        arrivedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppSingleton.getInstance(LocationMapActivity.this).stopPositionUploadService();
//                Intent intent = new Intent(LocationMapActivity.this,TripArrivedActivity.class);
//                intent.putExtra(ApplicationRef.TripTags.TRIP_ID,tripId);
//                intent.putExtra(ApplicationRef.TripTags.TRIP_CONTROL_NO,controlNo);
//                intent.putExtra(ApplicationRef.TripTags.LOCATION_ID,locationId);
//                intent.putExtra(ApplicationRef.TripTags.LOCATION_ORDER,locationOrder);
//                intent.putExtra(ApplicationRef.TripTags.LOCATION_TYPE,locationType);
//                intent.putExtra(ApplicationRef.TripTags.LATITUDE,locationLatitude);
//                intent.putExtra(ApplicationRef.TripTags.LONGITUDE,locationLongitude);
//                startActivity(intent);
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            try {
                tripId = bundle.getString(ApplicationRef.TripTags.TRIP_ID);
                controlNo = bundle.getString(ApplicationRef.TripTags.TRIP_CONTROL_NO);
                toolBarTitle.setText("ORDER " + controlNo);
                locationId = bundle.getString(ApplicationRef.TripTags.LOCATION_ID);
                locationOrder = bundle.getString(ApplicationRef.TripTags.LOCATION_ORDER);
                locationType = bundle.getString(ApplicationRef.TripTags.LOCATION_TYPE);
                locationLatitude = bundle.getString(ApplicationRef.TripTags.LATITUDE);
                locationLongitude = bundle.getString(ApplicationRef.TripTags.LONGITUDE);
                origin = new LatLng(Double.parseDouble(locationLatitude), Double.parseDouble(locationLongitude));

            }catch (Exception e){

            }

        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationMapFragment);
        mapFragment.getMapAsync(this);

        mapReportBtn = (ImageView) findViewById(R.id.mapReportBtn);
        mapReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsAlert();
            }
        });

        locationMapActivityController = new LocationMapActivityController(LocationMapActivity.this,this);
        currentStatus=ApplicationRef.StatusCodes.ORDER_STATUS_IN_TRANSIT;
        currentStatusCode= StatusCodeManager.getStatusCode(this,ApplicationRef.StatusCodes.ORDER_STATUS,currentStatus);
        rejectionReasonsList = new ArrayList<>();
        getData();
    }

    private void getData(){
        showLoader();
        final RejectReasonHelper rejectReasonHelper = new RejectReasonHelper(this);
        ICommonDataListener iCommonDataListener = new ICommonDataListener() {
            @Override
            public void onCommonDataSuccess(String api, JSONObject object) {
                rejectionReasonsList = rejectReasonHelper.getRejectionListArray(object);
                hideLoader();
            }

            @Override
            public void onCommonDataError(String api, String error) {
                onError(error);
            }
        };
        rejectReasonHelper.getRejectReason(ApplicationRef.Service.ISSUE,iCommonDataListener);

    }

    @Override
    public void onLocationDataSuccess(JSONObject object) {

    }

    @Override
    public void onError(String error) {
        hideLoader();
        AppSingleton.getInstance(this).stopPositionUploadService();
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

    private void showOptionsAlert(){
        //
        if(rejectionReasonsList.size() > 0) {
            IOptionsDialogDismiss iOptionsDialogDismiss = new IOptionsDialogDismiss() {
                @Override
                public void onOptionsDismiss(int identifier, int position, String reason) {
                    String statusCode = ApplicationRef.Service.ISSUE;
                    String reasonId = String.valueOf(rejectionReasonsList.get(position).getId());
                    String state = currentStatus;
                    String locationId = "";
                    String locationType="";
                    locationMapActivityController.sendRejectReason(tripId,locationId,locationType,statusCode,reason,reasonId,state,"","");
                }
            };
            ArrayList<String> tempRejectionReasonsList = RejectReasonHelper.getTempRejectionListArray(rejectionReasonsList);
            String reasons[] = tempRejectionReasonsList.toArray(new String[tempRejectionReasonsList.size()]);
            OptionsAlertDialog optionsAlertDialog = new OptionsAlertDialog(LocationMapActivity.this, iOptionsDialogDismiss);
            optionsAlertDialog.showRadioOptionsDialog(getResources().getString(R.string.trip_reject), reasons, true, REJECT_REASON_OPTIONS);
        }
    }

    @Override
    public void onSendIssueSuccess(JSONObject object) {

    }

    @Override
    public void onSendIssueError(String error) {
        onError(error);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            addMarker(origin, locationType, Utility.getCustomMarkerIcon(LocationMapActivity.this, locationType, locationStatus, locationOrder));
            zoomMap(origin);

            mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                @Override
                public void onCameraMoveStarted(int i) {
                    focusToRouteBtn.setVisibility(View.VISIBLE);
                }
            });


            focusToRouteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zoomMap(origin);
                    focusToRouteBtn.setVisibility(View.INVISIBLE);
                }
            });
        }catch (Exception e){

        }
    }

    private void addMarker(LatLng point, String title, Bitmap bm){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.title(title);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bm));
        mMap.addMarker(markerOptions);
    }

    private void zoomMap(LatLng origin){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin,15));
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
}
