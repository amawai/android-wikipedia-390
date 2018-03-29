package org.wikipedia.settings;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Prefs.class)
public class PrefsTest {
    @Test
    public void testMockIsOnThisDayNotificationEnabledTrueCase() {
        PowerMockito.mockStatic(Prefs.class);
        Prefs mockPref = mock(Prefs.class);
        PowerMockito.when(mockPref.isOnThisDayNotificationEnabled()).thenReturn(true);
        assertEquals(mockPref.isOnThisDayNotificationEnabled(), true);
    }

    @Test
    public void testMockIsOnThisDayNotificationFalseCase() {
        PowerMockito.mockStatic(Prefs.class);
        Prefs mockPref = mock(Prefs.class);
        PowerMockito.when(mockPref.isOnThisDayNotificationEnabled()).thenReturn(false);
        assertEquals(mockPref.isOnThisDayNotificationEnabled(), false);
    }

    @Test
    public void testMockSetOnThisDayNotificationEnabled() {
        PowerMockito.mockStatic(Prefs.class);
        Prefs mockPref = mock(Prefs.class);
        verify(mockPref).setOnThisDayNotificationEnabled(true);
    }

    //Testing Setters and Getters for Private Browsing Enabled preference variable
    @Test
    public void testMockPrivatBrowsingEnabledTrueCase() {
        PowerMockito.mockStatic(Prefs.class);
        Prefs mockPref = mock(Prefs.class);
        PowerMockito.when(mockPref.isPrivateBrowsingEnabled()).thenReturn(true);
        assertEquals(mockPref.isPrivateBrowsingEnabled(), true);
    }

    @Test
    public void testMockPrivateBrowsingEnabledFalseCase() {
        PowerMockito.mockStatic(Prefs.class);
        Prefs mockPref = mock(Prefs.class);
        PowerMockito.when(mockPref.isPrivateBrowsingEnabled()).thenReturn(false);
        assertEquals(mockPref.isPrivateBrowsingEnabled(), false);
    }


    @Test
    public void testMockSetPrivateBrowsingEnabled() {
        PowerMockito.mockStatic(Prefs.class);
        Prefs mockPref = mock(Prefs.class);
        verify(mockPref).setPrivateBrowsingEnabled(true);
    }
}


