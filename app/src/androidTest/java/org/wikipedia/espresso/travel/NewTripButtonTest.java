package org.wikipedia.espresso;


import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wikipedia.R;
import org.wikipedia.travel.TravelPlannerActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NewTripButtonTest {

    @Rule
    public ActivityTestRule<TravelPlannerActivity> mActivityTestRule = new ActivityTestRule<>(TravelPlannerActivity.class);

    public interface WhileCondition {
        boolean condition();
    }

    public interface WhileBody {
        void body();
    }
    @SuppressWarnings("checkstyle:magicnumber")
    public static void whileWithMaxSteps(WhileCondition condition, WhileBody body) {
        whileWithMaxSteps(condition, body, 5);
    }

    public static void whileWithMaxSteps(WhileCondition condition, WhileBody body, int maxSteps) {
        int steps = 0;
        while (condition.condition() && ++steps < maxSteps) {
            body.body();
        }
        if (steps >= maxSteps) {
            throw new RuntimeException("Loop condition exceeded maximum steps.");
        }
    }

    public static boolean viewIsDisplayed(int viewId) {
        final boolean[] isDisplayed = {true};

        onView(withId(viewId))
                .withFailureHandler((Throwable error, Matcher<View> viewMatcher) -> isDisplayed[0] = false)
                .check(matches(isDisplayed()));

        return isDisplayed[0];
    }

    public static void waitFor(final long millis) {
        onView(isRoot()).perform(doWaitFor(millis));
    }

    private static ViewAction doWaitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }

    @Test
    public void newTripButtonTest() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
            onView(allOf(withId(R.id.trip_button_new))).check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
