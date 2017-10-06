package com.cts.cheetah.view.orders.mytrips.maps.Route;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.cts.cheetah.R;


public class NotificationActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        TextView tv = (TextView) findViewById(R.id.tv_notification);
        Bundle data = getIntent().getExtras();
        tv.setText(data.getString("content"));
    }
}
