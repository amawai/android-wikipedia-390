package org.wikipedia.travel.destinationpicker;

import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by aman_ on 3/4/2018.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(DestinationActivity.class)
public class DestinationActivityTest {

    Context mContext;
    Intent mIntent;
    DestinationActivity mDestinationActivity;
    DestinationFragment destinationFragment;

    //Initial setup for DestinationActtvity tests
    @Before
    public void setUp() throws Exception {
        mContext = PowerMockito.mock(Context.class);


        PowerMockito.mockStatic(DestinationActivity.class);
        when(DestinationActivity.newIntent(mContext)).thenReturn(mIntent);

        mDestinationActivity = PowerMockito.mock(DestinationActivity.class);
        destinationFragment = new DestinationFragment();
        when(mDestinationActivity.createFragment()).thenReturn(destinationFragment);
    }

    //Asserts that newIntent returns an Intent type
    @Test
    public void newIntent() throws Exception {

        assertEquals(mIntent, DestinationActivity.newIntent(mContext));
    }

    //Asserts that the return type is an instance of Destination Fragment
    @Test
    public void createFragment() throws Exception {
        assertThat(mDestinationActivity.createFragment(), instanceOf(DestinationFragment.class));
    }

}