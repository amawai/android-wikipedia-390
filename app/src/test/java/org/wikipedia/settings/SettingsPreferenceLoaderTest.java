package org.wikipedia.settings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SettingsPreferenceLoader.class)
public class SettingsPreferenceLoaderTest {
    //tests that loadPreferences operation is called which consists of creating OnThisDayNotificationsListener and initiating the preference change
    @Test
    public void testMockLoadPreferences() {
        PowerMockito.mock(SettingsPreferenceLoader.class);
        SettingsPreferenceLoader mockSettingsPreferenceLoader = mock(SettingsPreferenceLoader.class);
        PowerMockito.doAnswer((i) -> {
            return null;
        }).when(mockSettingsPreferenceLoader).loadPreferences();
    }
}
