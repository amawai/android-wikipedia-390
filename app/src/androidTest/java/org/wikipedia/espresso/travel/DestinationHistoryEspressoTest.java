package org.wikipedia.espresso.travel;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.wikipedia.R;
import org.wikipedia.travel.TravelPlannerActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;

@SdkSuppress(minSdkVersion = 18)
public class DestinationHistoryEspressoTest {
    private UiDevice mDevice;

    @Rule
    public ActivityTestRule<TravelPlannerActivity> dActivityTestRule = new ActivityTestRule<>(TravelPlannerActivity.class);

    @Before
    public void setUp() {
        //Allows testing of components outside of testing context, in this case: Google Android Places Autocomplete
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        //Navigate to the Destinations fragment
        ViewInteraction planNewTripButton = onView(
                allOf(withId(R.id.trip_button_new)));
        planNewTripButton.perform(click());
    }

    @Test
    public void selectedDestinationShouldAppearInDestinationHistory() throws UiObjectNotFoundException {
        ViewInteraction destinationInput = onView(withId(R.id.fragment_place_autocomplete));

        destinationInput.perform(click());

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

        ViewInteraction editText = onView(
                allOf(withId(R.id.place_autocomplete_search_input),
                        isDisplayed()));

        //Check that the destination selected from the dropdown appears
        editText.check(matches(withText("Montreal")));

        //Checks that destination history is visible after search selection
        ViewInteraction destinationHistory = onView((
                allOf(withId(R.id.destination_history_view_recycler),
                        isDisplayed())));
        destinationHistory.check(matches(isDisplayed()));

        //Checks if destination history gets populated with matched search result
        ViewInteraction destinationName = onView(withId(R.id.destination_history_view_recycler));
        destinationName.check(matches(hasDescendant(withText("Montreal, QC, Canada"))));

        assertTrue(destinationName.check(matches(hasDescendant(withText("Montreal, QC, Canada")))) instanceof ViewInteraction);
    }
}
