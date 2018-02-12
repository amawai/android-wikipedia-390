package org.wikipedia.settings;

import static org.junit.Assert.*;
import org.junit.Test;

public class PrefsTest {

    @Test
    //Make sure that
    public void testIsOnThisDayNotificationEnabled() {
        assertFalse((Boolean) Prefs.isOnThisDayNotificationEnabled() == null);
    }
    /*
    @Test
    public void testSetOnThisDayNotificationEnabled() {
    }
    */
}


