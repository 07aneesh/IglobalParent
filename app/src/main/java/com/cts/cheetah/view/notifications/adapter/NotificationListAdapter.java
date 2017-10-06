package com.cts.cheetah.view.notifications.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cts.cheetah.R;
import com.cts.cheetah.helpers.Utility;
import com.cts.cheetah.model.Notification;
import java.util.ArrayList;



public class NotificationListAdapter extends ArrayAdapter<Notification> {

    private Activity context;
    private ArrayList<Notification> notifications;


    public NotificationListAdapter(Activity context, ArrayList<Notification> notifications) {
        super(context, R.layout.notification_list_item, notifications);
        this.context = context;
        this.notifications = notifications;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Notification getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.notification_list_item, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.notificationId = (TextView) rowView.findViewById(R.id.notificationId);
            viewHolder.notificationMessage = (TextView) rowView.findViewById(R.id.notificationMessage);
            viewHolder.timeSince = (TextView) rowView.findViewById(R.id.timeSince);

            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        Notification notification = getItem(position);
        holder.notificationId.setText("ORDER #" + notification.getNotificationTypeId());
        holder.notificationMessage.setText(notification.getNotificationMessage());
        holder.timeSince.setText(Utility.calculateTimeAgo(Long.parseLong(notification.getReceivedTime())));

        return rowView;
    }

    static class ViewHolder {
        public TextView notificationId;
        public TextView notificationMessage;
        public TextView timeSince;
    }
}
