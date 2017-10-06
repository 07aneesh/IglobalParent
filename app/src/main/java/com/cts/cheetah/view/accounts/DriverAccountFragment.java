package com.cts.cheetah.view.accounts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cts.cheetah.BuildConfig;
import com.cts.cheetah.R;
import com.cts.cheetah.components.CustomAlertDialog;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.helpers.CustomFontHelper;
import com.cts.cheetah.helpers.DocumentImageHelper;
import com.cts.cheetah.helpers.ImageProcessor;
import com.cts.cheetah.helpers.PreferenceManager;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.interfaces.IBaseView;
import com.cts.cheetah.interfaces.ICustomDialogDismiss;
import com.cts.cheetah.interfaces.IImageDownload;
import com.cts.cheetah.model.DriverAccount;
import com.cts.cheetah.view.accounts.logout.LogoutController;
import com.cts.cheetah.view.login.LoginActivity;
import com.cts.cheetah.view.main.MainDashboardActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DriverAccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DriverAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverAccountFragment extends Fragment implements ICustomDialogDismiss,IDriverAccount{

    private OnFragmentInteractionListener mListener;
    DriverAccountController mDriverAccountController;
    ArrayList<String> headerList;
    DriverAccount driverAccount;
    TextView driverName;
    TextView driverId;
    ImageView driverImage;
    //ImageView vehicleImage;
    TextView vehicleId;
    TextView vehicleType;
    ExpandableListView expandableListView;
    DriverAccountListAdapter adapter;
    //ProgressBar driverImageProgress,vehicleImageProgress; an
    ProgressBar driverImageProgress;

    public DriverAccountFragment() {
        // Required empty public constructor
    }

    public static DriverAccountFragment newInstance() {
        DriverAccountFragment fragment = new DriverAccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDriverAccountController = new DriverAccountController(this,getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_driver_account, container, false);

        driverImage = (ImageView) rootView.findViewById(R.id.driverImage);
        driverImageProgress = (ProgressBar) rootView.findViewById(R.id.driverImageProgress);
        driverName = (TextView) rootView.findViewById(R.id.driverName);
        driverId = (TextView) rootView.findViewById(R.id.driverId);
       // vehicleImage = (ImageView) rootView.findViewById(R.id.vehicleImage);
       // vehicleImageProgress = (ProgressBar) rootView.findViewById(R.id.vehicleImageProgress);
//        vehicleId = (TextView) rootView.findViewById(R.id.vehicleId);
//        vehicleType = (TextView) rootView.findViewById(R.id.vehicleType);
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.driverDetailsExpandableList);

        TextView versionNo = (TextView) rootView.findViewById(R.id.versionNo);
        versionNo.setText(BuildConfig.BUILD_NO);

        Button logoutBtn = (Button) rootView.findViewById(R.id.driverLogoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutHandler();
            }
        });

        Utility.deleteTempDocFiles(getContext());//Clears temp files before start
        getDriverAccountData();
        //CustomFontHelper.findViews(getContext(),rootView,CustomFontHelper.OSWALD);
        return rootView;
    }

    @Override
    public void onDriverAccountData(JSONObject result) {
        try {
            driverAccount = new DriverAccount();
            ((IBaseView) getActivity()).hideLoader();
            driverAccount.setData(result);
            setAccountData();
        }catch (Exception e){

        }
    }

    @Override
    public void onDriverAccountError(String error) {
        try {
            if (error.equals(ApplicationRef.Service.INVALID_CODE)) {
                ((IBaseView) getActivity()).onInvalidSession();
            } else {
                ((IBaseView) getActivity()).onError(error);
            }
        }catch (Exception e){

        }

    }

    private void setAccountData(){
        try {
            driverName.setText(driverAccount.getDriverName());
            driverId.setText(driverAccount.getDriverId());
            setImageFromRemote(driverImage, driverAccount.getDriverThumbImage(), driverImageProgress);
//            setImageFromRemote(vehicleImage, driverAccount.getVehicleThumbImage(), vehicleImageProgress);
//            vehicleId.setText(driverAccount.getVehicleLicenseNo());
//            if (driverAccount.getVehicleLicenseNo().equalsIgnoreCase("null")) {
//                vehicleId.setText("");
//            }
          //  vehicleType.setText(driverAccount.getVehicleType());
            adapter = new DriverAccountListAdapter(getContext(), driverAccount.getHeaderList(), driverAccount);
            expandableListView.setAdapter(adapter);
        }catch (Exception e){

        }
    }

    private void setImageFromRemote(final ImageView imageView, final String url, final ProgressBar pBar){
        try {
            if (Utility.checkForDownloadedImage(url, getContext())) {
                //If image already downloaded and saved locally
                String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
                String pathName = (Utility.getDocImageFolderName(getContext()) + File.separator + fileName);
                imageView.setImageBitmap(Utility.getBitmapFromUrl(50, 50, pathName));
                pBar.setVisibility(View.GONE);
            } else {
                //Download image from remote location
                IImageDownload iImageDownload = new IImageDownload() {
                    @Override
                    public void onImageDownloaded(Bitmap bitmap) {
                        try {
                            if (bitmap != null) {
                                imageView.setImageBitmap(Utility.getRoundedShape(bitmap, 1500));
                                //Save image to device
                                String imageName = url.substring(url.lastIndexOf("/"), url.length());
                                DocumentImageHelper documentImageHelper = new DocumentImageHelper(getContext());
                                documentImageHelper.saveDownloadedImage(imageName, bitmap);
                            }
                            pBar.setVisibility(View.GONE);
                        } catch (Exception e) {

                        }
                    }
                };
                String url1 = url.replaceAll(" ", "%20");
                ImageProcessor imageProcessor = new ImageProcessor();
                imageProcessor.downloadImage(imageView, url1, iImageDownload);
            }
        }catch (Exception e){

        }
    }

    private void logoutHandler(){
        try {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(getContext(), this);
            customAlertDialog.showAlert(getString(R.string.logout), getString(R.string.do_you_want_to_logout), true, true);
        }catch (Exception e){

        }
    }

    @Override
    public void onDialogDismiss(String result) {
        try {
            if (result.equals(ApplicationRef.OK)) {
                ((MainDashboardActivity) getActivity()).showLoader();
                LogoutController logoutController = new LogoutController(getContext(),this);
                //logoutController.logout();
                logoutController.setAvailibilityAsOffline();
            }
        }catch (Exception e){

        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void getDriverAccountData(){
        ((IBaseView) getActivity()).showLoader();
        mDriverAccountController.getDriverAccountData();
    }

    public void reloadData(){
        getDriverAccountData();
    }


}
