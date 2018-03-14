package org.wikipedia.espresso.travel;


import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import org.junit.Rule;
import org.wikipedia.travel.datepicker.DateActivity;

/**
 * Created by amawai on 14/03/18.
 */

@SdkSuppress(minSdkVersion = 18)
public class DatePickerEspressoTest {
    private UiDevice mDevice;
    private String today;

    @Rule
    public ActivityTestRule<DateActivity> dActivityTestRule = new ActivityTestRule<>(DateActivity.class);

}
