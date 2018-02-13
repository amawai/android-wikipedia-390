package org.wikipedia.feed.onthisday;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.wikipedia.R;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by amawai on 11/02/18.
 */

@RunWith(RobolectricTestRunner.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest(Context.class)
public class OTDNotificationTest {
    Context mContext;
    Intent mIntent;
    OnThisDayNotifications otdNotification;
    NotificationManager mNotificationManager;

    @Before
    public void setUp(){
        mContext = PowerMockito.mock(Context.class); //Using PowerMockito so that we can mock final methods
        mIntent = new Intent(RuntimeEnvironment.application, OnThisDayNotifications.class);
        otdNotification = new OnThisDayNotifications();
        mNotificationManager = mock(NotificationManager.class);

        when(mContext.getString(R.string.notification_on_this_day_title)).thenReturn("Testing title");
        when(mContext.getString(R.string.notification_on_this_day_desc)).thenReturn("Testing desc");
    }

    @Test
    public void testOnReceive() {
        otdNotification.onReceive(mContext, mIntent);

        verify(mContext).getString(R.string.notification_on_this_day_title);
        verify(mContext).getString(R.string.notification_on_this_day_desc);
        verify(mContext).getSystemService(Context.NOTIFICATION_SERVICE);
    }


}
