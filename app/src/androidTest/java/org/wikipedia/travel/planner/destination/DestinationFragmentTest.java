package org.wikipedia.travel.planner.destination;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by aman_ on 3/4/2018.
 */

public class DestinationFragmentTest {

    // Expected: Fragment to not be null when invoked
    @Test
    public void onCreateView() throws Exception {
        DestinationFragment destinationFragment = new DestinationFragment();
        assertNotNull(destinationFragment);
    }
}