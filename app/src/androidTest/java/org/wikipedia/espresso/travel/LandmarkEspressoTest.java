package org.wikipedia.espresso.travel;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.wikipedia.R;
import org.wikipedia.travel.destinationpicker.DestinationActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by amawai on 14/03/18.
 */

public class LandmarkEspressoTest {
    private UiDevice mDevice;

    @Rule
    public ActivityTestRule<DestinationActivity> dActivityTestRule = new ActivityTestRule<>(DestinationActivity.class);

    @Before
    public void setUp() {
        //Allows testing of components outside of testing context, in this case: destination
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void ensureThatCorrectDestinationIsDisplayed() throws UiObjectNotFoundException{
        ViewInteraction nextDateButton = onView(
                allOf(withId(R.id.destination_button_next)));

        //Select Montreal, QC, Canada as the destination
        selectDestination();

        //Go to the Datepicker fragment
        nextDateButton.perform(click());

        ViewInteraction nextButton = onView(
                allOf(withId(R.id.date_button_next)));
        nextButton.check(matches(isDisplayed()));

        //Go to the Landmark fragment
        nextButton.perform(click());

        //Check that the correct destination is displayed
        ViewInteraction destinationDisplay = onView(
                allOf(withId(R.id.landmark_country_view_text)));
        destinationDisplay.check(matches(withText("Montreal, QC, Canada")));

    }

    private void selectDestination() throws UiObjectNotFoundException{
        ViewInteraction layout = onView(withId(R.id.fragment_place_autocomplete));

        layout.perform(click());

        UiObject searchBar = mDevice.findObject(new UiSelector()
                .className("android.widget.EditText"));

        try {
            searchBar.setText("Montreal");
            closeSoftKeyboard();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        //Select one of the options from the dropdpown
        mDevice.findObject(new UiSelector()
                .text("QC, Canada")).click();
    }
}
