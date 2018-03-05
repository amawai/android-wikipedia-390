package org.wikipedia.travel;

import android.content.Context;
import android.content.Intent;
import android.test.MoreAsserts;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

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
