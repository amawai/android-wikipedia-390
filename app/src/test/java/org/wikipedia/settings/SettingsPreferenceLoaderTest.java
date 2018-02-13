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
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.wikipedia.R;
import org.wikipedia.feed.onthisday.OnThisDayAlarmService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

//@RunWith(RobolectricTestRunner.class)
//@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
//@PrepareForTest(Context.class)
public class SettingsPreferenceLoaderTest {
    @Test
    public void testMockLoadPreferences() {
        SettingsPreferenceLoader mockSettingsPreferenceLoader = mock(SettingsPreferenceLoader.class);
        verify(mockSettingsPreferenceLoader).loadPreferences();
    }
}
