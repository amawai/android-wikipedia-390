package org.wikipedia.travel.destinationpicker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.wikipedia.BuildConfig;

import static org.junit.Assert.assertNotNull;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

/**
 * Created by aman_ on 3/4/2018.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, packageName = "org.wikipedia", manifest = "AndroidManifest.xml")
public class DestinationFragmentTest {

    
    @Test
    public void onCreateView() throws Exception {
        DestinationFragment destinationFragment = new DestinationFragment();
        startFragment(destinationFragment);
        assertNotNull(destinationFragment);
    }

}