package org.wikipedia.travel.landmarkpicker;

import android.test.MoreAsserts;

import org.junit.Test;
import org.wikipedia.travel.landmarkpicker.LandmarkCard;
import org.wikipedia.travel.landmarkpicker.LandmarkFragment;

/**
 * Created by mnhn3 on 2018-03-04.
 */

public class LandmarkFragmentTest {

    @Test
    public void test_CardAssignableFromFragment(){
        MoreAsserts.assertAssignableFrom(LandmarkCard.class, LandmarkFragment.class);
    }

}
