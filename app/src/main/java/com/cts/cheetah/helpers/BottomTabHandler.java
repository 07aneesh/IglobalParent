package com.cts.cheetah.helpers;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.cts.cheetah.R;

/**
 * Created by manu.palassery on 23-03-2017.
 */

public class BottomTabHandler {

    //static TabLayout bottomTabLayout;

    public BottomTabHandler(){

    }

    public static void configureBottomTab(final Context context, final TabLayout bottomTabLayout, final int selection){
       // bottomTabLayout = bottomTabLayout;
        try {
            bottomTabLayout.addTab(bottomTabLayout.newTab().setText(R.string.trip_label_orders));
            bottomTabLayout.addTab(bottomTabLayout.newTab().setText(R.string.trip_label_earnings));
            bottomTabLayout.addTab(bottomTabLayout.newTab().setText(R.string.trip_label_account));
            bottomTabLayout.addTab(bottomTabLayout.newTab().setText(R.string.trip_label_notifications));
            //
            bottomTabLayout.getTabAt(0).setIcon(R.drawable.orders);
            bottomTabLayout.getTabAt(1).setIcon(R.drawable.earnings);
            bottomTabLayout.getTabAt(2).setIcon(R.drawable.account);
            bottomTabLayout.getTabAt(3).setIcon(R.drawable.notifications);
        }catch (Exception e){
            Log.i("EXC",e+"");
        }

        /*bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Intent intent;
                switch(tab.getPosition()){
                    case 0:
                        intent = new Intent(context, TripsDashboardActivity.class);
                        context.startActivity(intent);
                        break;
                    case 1:
                        break;
                    case 2:
                        intent = new Intent(context, DriverAccount.class);
                        context.startActivity(intent);
                        break;
                    case 3:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

    }
}
