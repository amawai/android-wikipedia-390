package org.wikipedia.espresso.util;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;
import org.wikipedia.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Credit to original Wikipedia Android App
 */

public class EspressoHelper {

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
}