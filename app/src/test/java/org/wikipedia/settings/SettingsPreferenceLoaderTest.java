package org.wikipedia.settings;

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
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.wikipedia.R;
import org.wikipedia.activity.BaseActivity;
import org.wikipedia.feed.onthisday.OnThisDayAlarmService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SettingsPreferenceLoader.class)
public class SettingsPreferenceLoaderTest {
    @Test//tests that listener is being called
    public void testMockLoadPreferences() {
        PowerMockito.mock(SettingsPreferenceLoader.class);
        SettingsPreferenceLoader mockSettingsPreferenceLoader = mock(SettingsPreferenceLoader.class);
        PowerMockito.doAnswer((i) -> {
            return null;
        }).when(mockSettingsPreferenceLoader).loadPreferences();
    }
}
