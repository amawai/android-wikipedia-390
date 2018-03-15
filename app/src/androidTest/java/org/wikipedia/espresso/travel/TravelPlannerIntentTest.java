package org.wikipedia.espresso.travel;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.wikipedia.R;
import org.wikipedia.main.MainActivity;
import org.wikipedia.travel.TravelPlannerActivity;
import org.wikipedia.travel.datepicker.DateActivity;
import org.wikipedia.travel.destinationpicker.DestinationActivity;
import org.wikipedia.travel.landmarkpicker.LandmarkActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.wikipedia.espresso.util.EspressoHelper.viewIsDisplayed;
import static org.wikipedia.espresso.util.EspressoHelper.waitFor;
import static org.wikipedia.espresso.util.EspressoHelper.whileWithMaxSteps;

/**
 * Created by sophiaquach16 on 14/03/18.
 */

public class TravelPlannerIntentTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void travelPlannerFlowShouldInvokeCorrectIntents() throws UiObjectNotFoundException{
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitUntilFeedDisplayed();

        //Scrolling down to the travel planner card
        onView(withId(R.id.fragment_feed_feed)).perform(RecyclerViewActions.scrollToPosition(10));
        waitFor(1000);

        onView(withId(R.id.fragment_feed_feed)).perform(RecyclerViewActions.scrollToPosition(8));
        waitFor(1000);

        ViewInteraction travelActionText = onView(
                allOf(withId(R.id.view_static_card_action_text), withText(R.string.view_travel_card_action),
                        isDisplayed()));
        travelActionText.perform(click());
        intended(hasComponent(TravelPlannerActivity.class.getName()));

        ViewInteraction buttonToDestination = onView(
                allOf(withId(R.id.trip_button_new))).check(matches(isDisplayed()));
        buttonToDestination.perform(click());
        intended(hasComponent(DestinationActivity.class.getName()));

        selectDestination(); //This method is called in order to simulate user behavior

        ViewInteraction buttonToDate = onView(
                allOf(withId(R.id.destination_button_next)));
        buttonToDate.perform(click());
        intended(hasComponent(DateActivity.class.getName()));

        ViewInteraction buttonToLandmark = onView(
                allOf(withId(R.id.date_button_next)));
        buttonToLandmark.perform(click());
        intended(hasComponent(LandmarkActivity.class.getName()));
    }

    private static void waitUntilFeedDisplayed() {
        whileWithMaxSteps(
                () -> !viewIsDisplayed(R.id.fragment_feed_feed),
                () -> waitFor(2000));
    }

    //Private helper to select destination (necessary in order to move on to the next view)
    private void selectDestination() throws UiObjectNotFoundException {
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
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