package org.wikipedia.feed.onthisday;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import org.wikipedia.R;

// Displays the notification regarding the On This Day Articles
public class OnThisDayNotifications extends BroadcastReceiver {
    // Sets the ID for the notification
    private static final String CHANNEL_ID = "ON_THIS_DAY_CHANNEL";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent onIntent = new Intent(context, OnThisDayActivity.class);
        PendingIntent pintent = PendingIntent.getActivity(context, 0, onIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.notification_on_this_day_title))
                .setContentText(context.getString(R.string.notification_on_this_day_desc))
                .setContentIntent(pintent)
                .setSmallIcon(R.drawable.ic_w_transparent)
                .setAutoCancel(true);
        
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(notificationChannel);
        }
        manager.notify(1, notificationBuilder.build());
    }

}

