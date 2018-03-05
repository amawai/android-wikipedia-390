package org.wikipedia.travel;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;


/**
 * Created by mnhn3 on 2018-03-04.
 */


public class PlacesFragmentTest {

    @Test
    public void onCreateView() throws Exception {
        PlacesFragment placesFragment = new PlacesFragment();
        assertNotNull(placesFragment);
    }

}
