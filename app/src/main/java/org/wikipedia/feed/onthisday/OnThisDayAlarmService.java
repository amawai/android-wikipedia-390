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
    private AlarmManager am;

    public OnThisDayAlarmService(Context context) {
        this.context = context;
        mAlarmSender = PendingIntent.getBroadcast(context, 1, new Intent(context, OnThisDayNotifications.class), PendingIntent.FLAG_UPDATE_CURRENT);
        am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    }

    // Initializes the alarm
    public void startAlarm() {
        //Set the alarm to trigger to 2 PM everyday
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 14);
        long daily = c.getTimeInMillis();
        // Schedule the alarm!
        am.setRepeating(AlarmManager.RTC, daily, AlarmManager.INTERVAL_DAY, mAlarmSender);
    }

    public void supercedeAlarmManage(AlarmManager alarmManager) {
        am = alarmManager;
    }

    //Cancels the alarm to disable the notification
    public void cancelAlarm() {
        if (mAlarmSender != null && am != null) {
            am.cancel(mAlarmSender);
        }
    }
}
