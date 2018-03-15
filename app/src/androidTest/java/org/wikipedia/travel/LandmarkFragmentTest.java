package org.wikipedia.travel;

import org.junit.Test;
import org.wikipedia.travel.landmarkpicker.LandmarkFragment;

import static org.junit.Assert.assertNotNull;


/**
 * Created by mnhn3 on 2018-03-04.
 */


public class LandmarkFragmentTest {

    @Test
    public void onCreateView() throws Exception {
        LandmarkFragment landmarkFragment = new LandmarkFragment();
        assertNotNull(landmarkFragment);
    }

}
