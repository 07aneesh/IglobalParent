package com.cts.cheetah.view.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cts.cheetah.R;
import com.cts.cheetah.components.LoaderDialogFragment;
import com.cts.cheetah.helpers.AppSingleton;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.CustomFontHelper;
import com.cts.cheetah.helpers.PositionUploadService;
import com.cts.cheetah.helpers.PreferenceManager;
import com.cts.cheetah.helpers.RuntimePermissionHelper;
import com.cts.cheetah.helpers.StatusCodeManager;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.interfaces.IBaseView;
import com.cts.cheetah.interfaces.IPermissionCheck;
import com.cts.cheetah.view.accounts.DriverAccountFragment;
//import com.cts.cheetah.view.earnings.main.EarningsMainFragment;
import com.cts.cheetah.view.accounts.logout.LogoutController;
import com.cts.cheetah.view.gps.GPSFragment;
import com.cts.cheetah.view.notifications.fragment.NotificationsFragment;
//import com.cts.cheetah.view.orders.main.TripsMainFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.lang.reflect.Field;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainDashboardActivity extends AppCompatActivity implements IBaseView,IMainDashboard {

    BottomNavigationView bottomNavigationView;
    LoaderDialogFragment loaderFragment;
  //  public static SearchView searchView;
    private TextView toolBarTitle;
   // Button btn_goOffline;
    MainDashboardController mainDashboardController;
    String targetedTab="";
    String fragmentTag="";
    boolean isLoaderVisible=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bottom_bar);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolBarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
       // btn_goOffline = (Button) toolbar.findViewById(R.id.go_offline);
        toolbar.findViewById(R.id.ic_home_btn).setVisibility(View.GONE);
       // searchView = (SearchView) toolbar.findViewById(R.id.search);

        mainDashboardController = new MainDashboardController(this,this);

//        btn_goOffline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mainDashboardController.setAvailibilityStatus();
//            }
//        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            targetedTab = bundle.getString("tab");
        }


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        CustomFontHelper.findViews(this,bottomNavigationView,CustomFontHelper.OSWALD);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item_logout:
                                //Logout
                                fragmentTag = "Logout";
                               // searchView.setVisibility(View.INVISIBLE);
                                setTitleText(getResources().getString(R.string.earnings_label_title));
                                //selectedFragment = EarningsMainFragment.newInstance();
                                LogoutController logoutCtrl = new LogoutController();
                                logoutCtrl.logout();
                                break;
                            case R.id.action_item3:
                                //Account
                                fragmentTag = "account";
                               // searchView.setVisibility(View.INVISIBLE);
                                setTitleText(getResources().getString(R.string.accounts_label_title));
                                selectedFragment = DriverAccountFragment.newInstance();
                                break;
                            case R.id.action_itemGps:
                                //Account
                                fragmentTag = "gps";
                                // searchView.setVisibility(View.INVISIBLE);
                                setTitleText(getResources().getString(R.string.accounts_label_GPS));
                                    selectedFragment = GPSFragment.newInstance();
                                break;
                            case R.id.action_item4:
                                //Notifications
                                fragmentTag = "notifications";
                               // searchView.setVisibility(View.INVISIBLE);
                                setTitleText(getResources().getString(R.string.notifications_label_title));
                                selectedFragment = NotificationsFragment.newInstance();
                                break;
                        }

                        //if(item.getItemId() == R.id.action_item1 || item.getItemId() == R.id.action_item3 || item.getItemId() == R.id.action_item4 ) {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_layout, selectedFragment);
                            transaction.commit();
                        //}

                        return true;
                    }
                });


        //Manually displaying the first fragment - one time only
        setTitleText(getResources().getString(R.string.accounts_label_title));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, DriverAccountFragment.newInstance());
        transaction.commit();


    }

    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            //Timber.e(e, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            //Timber.e(e, "Unable to change value of shift mode");
        }
    }

    public void selectTab(int tabPosition){
        //Explicitly selects a tab by providing id.
       // searchView.setVisibility(View.INVISIBLE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        setTitleText(getResources().getString(R.string.accounts_label_title));
        transaction.replace(R.id.frame_layout, DriverAccountFragment.newInstance());
        transaction.commit();
        bottomNavigationView.getMenu().getItem(tabPosition).setChecked(true);
    }

    public void selectTabForNotification(int tabPosition,int childTabPosition){
//        if(tabPosition == 0){
//            bottomNavigationView.getMenu().getItem(tabPosition).setChecked(true);
//
//            TripsMainFragment tripsMainFragment = new TripsMainFragment().newInstance();
//            tripsMainFragment.selectTab(childTabPosition);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.frame_layout, tripsMainFragment);
//            transaction.commit();
//
//        }
    }

    public void setTitleText(String name){
        if(name != null) {
            //getSupportActionBar().setTitle(name);
           toolBarTitle.setText(name);
        }
    }

    @Override
    public void onAvailabilityStatusSuccess(JSONObject result) {
        try {
            JSONObject resultObject = result.getJSONObject("results");
            String statusCode = resultObject.getString("availability");
            String statusMessage = (result.getString("statusMessage"));
            PreferenceManager.getInstance(this).setDriverAvailabilityStatus(statusCode);
            Toast.makeText(MainDashboardActivity.this,statusMessage,Toast.LENGTH_SHORT).show();
        }catch (JSONException e){

        }

        hideLoader();
       // changeOfflineButton();
       /// updateLocation();
    }

    @Override
    public void onAvailabilityStatusError(String error) {
        onError(error);
    }

    /**
     * changeOfflineButton() checks driver availabilty status stored in Preference Manager and
     * changes the text and icon of "Go Offline/Online" button
     *
     */
//    private void changeOfflineButton(){
//        String status = StatusCodeManager.getStatusName(this,ApplicationRef.StatusCodes.DRIVER_STATUS,PreferenceManager.getInstance(this).getDriverAvailabilityStatus());
//        try {
//            Drawable img = null;
//            if (!status.equals("")) {
//                if (status.equals(ApplicationRef.StatusCodes.DRIVER_AVAILABILITY_ONLINE)) {
//                    img = getResources().getDrawable(R.drawable.icon_go_offline);
//                    btn_goOffline.setText("GO OFFLINE");
//                } else if (status.equals(ApplicationRef.StatusCodes.DRIVER_AVAILABILITY_OFFLINE)) {
//                    img = getResources().getDrawable(R.drawable.icon_go_online);
//                    btn_goOffline.setText("GO ONLINE");
//                }
//                img.setBounds(0, 0, 60, 60);
//                btn_goOffline.setCompoundDrawables(img, null, null, null);
//            }
//        }catch (Exception e){
//
//        }
//    }

    @Override
    public void onError(String error) {
        hideLoader();
        try {
            if (error.equals(ApplicationRef.Service.INVALID_CODE)) {
                onInvalidSession();
            } else {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onInvalidSession() {
        Toast.makeText(this, R.string.invalid_session, Toast.LENGTH_SHORT).show();
        Utility.handleInvalidSession(this);
    }

    public void hideLoader() {
        try {
            if (loaderFragment != null){
                loaderFragment.dismiss();
                isLoaderVisible = false;
            }
        } catch (IllegalStateException ie) {
            Utility.logger(ie);
        }

    }

    public void showLoader() {
        try {
            if(isLoaderVisible == false) {
                if (loaderFragment == null)
                    loaderFragment = new LoaderDialogFragment();
                if (!loaderFragment.isVisible())
                    loaderFragment.show(getSupportFragmentManager(), getString(R.string.login_loader_loaderTag));
                isLoaderVisible = true;
            }
        }catch (Exception e){

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        /*getMenuInflater().inflate(R.menu.menu_search, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                viewPagerSearch(newText);

                return false;
            }
        });
*/

        return super.onCreateOptionsMenu(menu);
    }

    public void viewPagerSearch(String newText){

        //((CompletedTripsFragment) getSupportFragmentManager().getFragments().get(1)).searchList(newText);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            this.moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onResume() {
        super.onResume();
        if( fragmentTag.equals("account")){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, DriverAccountFragment.newInstance());
            transaction.commit();
        }
        //getSupportFragmentManager().findFragmentByTag();
    }

    // Update Location in fixed interval
    private void updateLocation() {
        if(mainDashboardController.isDriverOnline()) {
            IPermissionCheck iPermissionCheck = new IPermissionCheck() {
                @Override
                public void onPermissionResult(boolean result) {
                    if (result) {
                        AppSingleton.getInstance(MainDashboardActivity.this).startPositionUploadService(null, null);
                    }
                }
            };
            RuntimePermissionHelper runtimePermissionHelper = new RuntimePermissionHelper(this, ((RelativeLayout) findViewById(R.id.activity_main)), iPermissionCheck);
            runtimePermissionHelper.checkLocationPermission();
        }
    }


}
