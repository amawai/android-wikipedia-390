package org.wikipedia.feed.onthisday;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
// This class is responsible for creating the alarm service that will trigger the
// On This Day notifications

public class OnThisDayAlarmService {
    private Context context;
    private PendingIntent mAlarmSender;

    public OnThisDayAlarmService(Context context) {
        this.context = context;
        mAlarmSender = PendingIntent.getBroadcast(context, 0, new Intent(context, OnThisDayNotifications.class), 0);
    }

    // Initializes the alarm
    public void startAlarm(){
        //Set the alarm to trigger to 2 PM everyday
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 14);
        long daily = c.getTimeInMillis();
        // Schedule the alarm!
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC, daily, AlarmManager.INTERVAL_DAY,mAlarmSender);
    }

}
