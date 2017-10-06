package com.cts.cheetah.view.notifications.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cts.cheetah.R;
import com.cts.cheetah.helpers.ApplicationRef;
import com.cts.cheetah.interfaces.IBaseView;
import com.cts.cheetah.model.Notification;
import com.cts.cheetah.view.accounts.IDriverAccount;
import com.cts.cheetah.view.main.MainDashboardActivity;
import com.cts.cheetah.view.notifications.adapter.NotificationListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
*
 */
public class NotificationsFragment extends Fragment implements INotification {

    NotificationController notificationController;
    ListView notificationsList;
    NotificationListAdapter notificationListAdapter;
    private OnFragmentInteractionListener mListener;
    TextView noItemsMessage;
    private SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<Notification> notificationArrayList;
    public NotificationsFragment() {


    }


    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationController = new NotificationController(this,getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        getData();
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        noItemsMessage = (TextView) rootView.findViewById(R.id.noItemsMessage);
        notificationsList = (ListView) rootView.findViewById(R.id.notificationList);
        notificationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                onNotificationSelected(parent,view,position, id);
            }
        });

        return rootView;
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

    private void getData(){
        notificationArrayList = new ArrayList<>();
        ((IBaseView) getActivity()).showLoader();
        notificationController.getNotifications();

    }

    @Override
    public void onNotificationSuccess(JSONObject result) {
        ((IBaseView) getActivity()).hideLoader();
        swipeRefreshLayout.setRefreshing(false);
        try{
            Notification notification;
            JSONArray jsonArray = result.getJSONArray("results");
            for (int i=0;i<jsonArray.length();i++){
                notification = new Notification();
                notification.setData(new JSONObject(jsonArray.get(i).toString()));
                notificationArrayList.add(notification);
            }

            notificationListAdapter = new NotificationListAdapter(this.getActivity(),notificationArrayList);
            notificationsList.setAdapter(notificationListAdapter);

            if(notificationArrayList.size() == 0){
                noItemsMessage.setVisibility(View.VISIBLE);
            }else{
                noItemsMessage.setVisibility(View.GONE);
            }

        }catch (JSONException e){

        }
    }

    @Override
    public void onNotificationError(String error) {
        if(swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if(error.equals(ApplicationRef.Service.INVALID_CODE)){
            ((IBaseView) getActivity()).onInvalidSession();
        }else {
            ((IBaseView) getActivity()).onError(error);
        }
    }

    private void onNotificationSelected(AdapterView<?> parent, View view, int position, long id){
        switch(notificationArrayList.get(position).getNotificationType()){
            case "upcomingTrip":
                ((MainDashboardActivity) getActivity()).selectTabForNotification(0,0);
                break;
            case "tripRequest":
                ((MainDashboardActivity) getActivity()).selectTabForNotification(0,2);
                break;
        }
    }


    public String milliseconds(String date)
    {
        //String date_ = date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return String.valueOf(timeInMilliseconds);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return "0";
    }

}
