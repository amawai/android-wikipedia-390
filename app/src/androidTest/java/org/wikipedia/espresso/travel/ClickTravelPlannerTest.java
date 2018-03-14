package org.wikipedia.espresso.travel;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wikipedia.R;
import org.wikipedia.main.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.wikipedia.espresso.util.EspressoHelper.viewIsDisplayed;
import static org.wikipedia.espresso.util.EspressoHelper.waitFor;
import static org.wikipedia.espresso.util.EspressoHelper.whileWithMaxSteps;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ClickTravelPlannerTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void travelPlannerShouldBeVisibleInFeed() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
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

        ViewInteraction travelCardTitle = onView(
                allOf(withId(R.id.view_static_card_title), withText(R.string.view_travel_card_title),
                        isDisplayed()));
        travelCardTitle.check(matches(withText(R.string.view_travel_card_title)));

        ViewInteraction travelActionText = onView(
                allOf(withId(R.id.view_static_card_action_text), withText(R.string.view_travel_card_action),
                        isDisplayed()));
        travelActionText.check(matches(withText(R.string.view_travel_card_action)));

        travelActionText.perform(click());
    }

    private static void waitUntilFeedDisplayed() {
        whileWithMaxSteps(
                () -> !viewIsDisplayed(R.id.fragment_feed_feed),
                () -> waitFor(2000));
    }
}
