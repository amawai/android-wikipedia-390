package org.wikipedia.feed.onthisday;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import java.util.Calendar;


import org.apache.tools.ant.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowAlarmManager;
import org.wikipedia.R;
import org.wikipedia.main.MainActivity;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by SunXP on 2018-02-11.
 */

@RunWith(RobolectricTestRunner.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest(Context.class)
public class OTDAlarmServiceTest {
    Context context;
    PendingIntent mAlarmSender;
    Intent intent;
    OnThisDayNotifications onThisDayNotifications;
    AlarmManager alarmManager;
    Calendar calendar;

    @Before
    public void setUp() {
        context = PowerMockito.mock(Context.class);
        calendar = mock(Calendar.class);
        intent = mock(Intent.class);
        alarmManager = mock(AlarmManager.class);
        mAlarmSender = mock(PendingIntent.class);
    }

    @Test
    public void testStartAlarm() {
        intent = new Intent(RuntimeEnvironment.application, OnThisDayAlarmService.class);
        mAlarmSender = PendingIntent.getBroadcast(RuntimeEnvironment.application, 0, intent, 0);

        //mock the 2pm object
        calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 14);
        long daily = calendar.getTimeInMillis();
        alarmManager.setRepeating(AlarmManager.RTC, daily, AlarmManager.INTERVAL_DAY, mAlarmSender);
        //test that the alarm manager has scheduled the alarm
        verify(alarmManager).setRepeating(AlarmManager.RTC, daily, AlarmManager.INTERVAL_DAY,mAlarmSender);
    }

    @Test
    public void testStartAlarmService() {
        OnThisDayAlarmService mockAlarm = mock(OnThisDayAlarmService.class);

        mockAlarm.startAlarm();
        //test to check that startAlarm method is called
        verify(mockAlarm).startAlarm();
    }

    @Test
    public void testGetSystemService() {
        OnThisDayAlarmService alarmService = new OnThisDayAlarmService(context);
        when(context.getSystemService(Context.ALARM_SERVICE)).thenReturn(mock(AlarmManager.class));

        alarmService.startAlarm();
        //test to check that the getSystemService gets called by the alarmservice object
        verify(context).getSystemService(Context.ALARM_SERVICE);
    }


    @Test
    public void testAlarmManager() throws Exception {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        AlarmManager alarmManager = (AlarmManager) RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
        ShadowAlarmManager shadowAlarmManager = shadowOf(alarmManager);
        OnThisDayAlarmService alarm = new OnThisDayAlarmService(context);

        //test that a new alarm is always scheduled
        assertNotNull(shadowAlarmManager.getNextScheduledAlarm());

        alarm.startAlarm();

        ShadowAlarmManager.ScheduledAlarm scheduledAlarm = shadowAlarmManager.getNextScheduledAlarm();
        //test that alarmmanager sending intent every day
        assertEquals(AlarmManager.INTERVAL_DAY, scheduledAlarm.interval);
    }

}
