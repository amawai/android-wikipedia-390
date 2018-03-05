package org.wikipedia.travel;

import android.test.MoreAsserts;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * Created by mnhn3 on 2018-03-04.
 */

public class PlaceFragmentTest {

    @Test
    public void test_CardAssignableFromFragment(){
        MoreAsserts.assertAssignableFrom(PlacesCard.class, PlacesFragment.class);
    }
}
