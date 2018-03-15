package org.wikipedia.travel;

import org.junit.Test;
import org.wikipedia.travel.trip.TripFragment;

import static org.junit.Assert.assertNotNull;

/**
 * Created by amawai on 04/03/18.
 */

public class TripFragmentTest {

    @Test
    public void shouldNotBeNull() {
        TripFragment tripFragment = new TripFragment();
        assertNotNull(tripFragment);
    }
}
