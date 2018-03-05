package org.wikipedia.travel;

import android.test.MoreAsserts;

import org.junit.Test;

/**
 * Created by mnhn3 on 2018-03-04.
 */

public class PlaceFragmentTest {

    @Test
    public void test_CardAssignableFromFragment(){
        MoreAsserts.assertAssignableFrom(PlacesCard.class, PlacesFragment.class);
    }
}
