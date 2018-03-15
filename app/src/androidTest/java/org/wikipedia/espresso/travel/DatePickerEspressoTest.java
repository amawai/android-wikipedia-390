package org.wikipedia.espresso.travel;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.wikipedia.R;
import org.wikipedia.travel.datepicker.DateActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


/**
 * Created by amawai on 14/03/18.
 */

@SdkSuppress(minSdkVersion = 18)
public class DatePickerEspressoTest {
    private UiDevice mDevice;
    private String today;

    @Rule
    public ActivityTestRule<DateActivity> dActivityTestRule = new ActivityTestRule<>(DateActivity.class);

    @Before
    public void setUp() {
        //Allows testing of components outside of testing context, in this case: datepicker
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        Calendar c = Calendar.getInstance();
        //Formatting the default date as it is displayed in the datepicker fragment
        SimpleDateFormat theDate = new SimpleDateFormat("LLLL dd, yyyy", Locale.getDefault());
        today = theDate.format(c.getTime());
    }

    @Test
    public void allUiComponentsShouldExist() {
        ViewInteraction datePickerTitle = onView(
                allOf(withId(R.id.select_departure_date_view_text),
                        isDisplayed()));
        datePickerTitle.check(matches(withText(R.string.date_departure)));

        ViewInteraction dateDisplay = onView(
                allOf(withId(R.id.selected_date_view_text),
                        isDisplayed()));
        dateDisplay.check(matches(withText(today)));

        ViewInteraction selectDateButton = onView(
                allOf(withId(R.id.date_button_select), withText(R.string.date_select_date),
                        isDisplayed()));
        selectDateButton.check(matches(isDisplayed()));

        ViewInteraction nextButton = onView(
                allOf(withId(R.id.date_button_next)));
        nextButton.check(matches(isDisplayed()));
    }

    @Test
    public void cancelDatePickingShouldNotChangeDisplayedDate() {
        ViewInteraction dateDisplay = onView(
                allOf(withId(R.id.selected_date_view_text),
                        isDisplayed()));
        dateDisplay.check(matches(withText(today)));

        ViewInteraction selectDateButton = onView(
                allOf(withId(R.id.date_button_select), withText(R.string.date_select_date)));
        selectDateButton.perform(click());

        UiObject cancelButton = mDevice.findObject(new UiSelector()
                .text("Cancel"));
        try {
            //Cancel the datepicker without changing the date
            cancelButton.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Ensure that the date hasn't changed after opening and subsequently canceling date change from datepicker
        dateDisplay.check(matches(withText(today)));
    }

    @Test
    public void shouldDisplayUserSetDate() {
        //Check that the datepicker's default displays shows today's date
        ViewInteraction dateDisplay = onView(
                allOf(withId(R.id.selected_date_view_text)));
        dateDisplay.check(matches(withText(today)));

        ViewInteraction selectDateButton = onView(
                allOf(withId(R.id.date_button_select), withText(R.string.date_select_date)));
        selectDateButton.perform(click());

        //Change the date in datepicker
        onView(withClassName(
                Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2018, 5, 1));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UiObject submitDateButton = mDevice.findObject(new UiSelector()
                .text("OK"));

        try {
            //Use the UiAutomator framework to submit the new date
            submitDateButton.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        //Ensure that the displayed date corresponds to the date selected from the datepicker
        dateDisplay.check(matches(withText("May 1, 2018")));
    }
}
