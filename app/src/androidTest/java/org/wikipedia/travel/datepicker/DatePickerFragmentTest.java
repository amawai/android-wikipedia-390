package org.wikipedia.travel.datepicker;

import org.junit.Before;
import org.junit.Test;
import org.wikipedia.travel.planner.date.TravelDatePickerFragment;

import static junit.framework.Assert.assertNotNull;


/**
 * Created by SunXP on 2018-03-04.
 */


public class DatePickerFragmentTest {
    TravelDatePickerFragment datePickerFragment;

    @Before
    public void setUp() {
        datePickerFragment = new TravelDatePickerFragment();
    }

    @Test
    public void fragmentNotNull() {
        assertNotNull(datePickerFragment);
    }

}
