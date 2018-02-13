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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

    @Before
    public void setUp() {
        context = PowerMockito.mock(Context.class);
        intent = mock(Intent.class);
        alarmManager = PowerMockito.mock(AlarmManager.class);
        mAlarmSender = mock(PendingIntent.class);
    }

    @Test
    public void testGetSystemService() {
        OnThisDayAlarmService alarmService = new OnThisDayAlarmService(context);
        when(context.getSystemService(Context.ALARM_SERVICE)).thenReturn((AlarmManager) RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE));

        alarmService.supercedeAlarmManage(alarmManager);

        alarmService.startAlarm();
        //test to check that the getSystemService gets using Context.ALARM_SERVICE
        verify(context).getSystemService(Context.ALARM_SERVICE);
    }


    @Test
    public void testAlarmManagerInterval() throws Exception {
        Context runTimeContext = RuntimeEnvironment.application.getApplicationContext();
        AlarmManager alarmManager = (AlarmManager) RuntimeEnvironment.application.getSystemService(Context.ALARM_SERVICE);
        ShadowAlarmManager shadowAlarmManager = shadowOf(alarmManager);
        OnThisDayAlarmService alarm = new OnThisDayAlarmService(runTimeContext);

        alarm.startAlarm();

        //Check to see if an alarm has been scheduled
        assertNotNull(shadowAlarmManager.getNextScheduledAlarm());

        ShadowAlarmManager.ScheduledAlarm scheduledAlarm = shadowAlarmManager.getNextScheduledAlarm();
        //test that alarmmanager sending intent every day
        assertEquals(AlarmManager.INTERVAL_DAY, scheduledAlarm.interval);
    }

}
