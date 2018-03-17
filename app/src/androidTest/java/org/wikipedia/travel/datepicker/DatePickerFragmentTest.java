package org.wikipedia.travel.datepicker;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;


/**
 * Created by SunXP on 2018-03-04.
 */


public class DatePickerFragmentTest {
    DateFragment datePickerFragment;

    @Before
    public void setUp() {
        datePickerFragment = new DateFragment();
    }

    @Test
    public void fragmentNotNull() {
        assertNotNull(datePickerFragment);
    }

}
