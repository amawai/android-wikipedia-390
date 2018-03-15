package org.wikipedia.espresso.travel;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wikipedia.R;
import org.wikipedia.travel.TravelPlannerActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class NewTripButtonTest {

    @Rule
    public ActivityTestRule<TravelPlannerActivity> mActivityTestRule = new ActivityTestRule<>(TravelPlannerActivity.class);

    @Test
    public void tripButtonExistsWithCorrectText() {
        ViewInteraction planNewTripButton = onView(
                allOf(withId(R.id.trip_button_new)));

        planNewTripButton.check(matches(isDisplayed()));
        planNewTripButton.check(matches(withText(R.string.plan_new_trip_button)));
    }
}
