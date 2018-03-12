package org.wikipedia.espresso;


import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wikipedia.R;
import org.wikipedia.main.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ClickTravelPlannerTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

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

    private static void waitUntilFeedDisplayed() {
        whileWithMaxSteps(
                () -> !viewIsDisplayed(R.id.fragment_feed_feed),
                () -> waitFor(2000));
    }

    @Test
    public void mainActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitUntilFeedDisplayed();

        onView(withId(R.id.fragment_feed_feed)).perform(RecyclerViewActions.scrollToPosition(10));
        waitFor(1000);

        onView(withId(R.id.fragment_feed_feed)).perform(RecyclerViewActions.scrollToPosition(8));
        waitFor(1000);
       /* ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.menu_overflow_button), withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.single_fragment_toolbar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());*/

        /*ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.menu_overflow_button), withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.single_fragment_toolbar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction imageView = onView(
                allOf(withId(R.id.single_fragment_toolbar_wordmark),
                        childAtPosition(
                                allOf(withId(R.id.single_fragment_toolbar),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));*/

       /* ViewInteraction linearLayout = onView(
                allOf(withId(R.id.view_static_card_container),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));*/
        // ViewInteraction wtv = onView(withId(R.id.view_static_card_title), withText("PLAN A TRIP"));
        //onView(withId(R.id.view_static_card_title(matches(withText(containsString("PLAN A TRIP"))));

        ViewInteraction textView = onView(
                allOf(withId(R.id.view_static_card_title), withText("Travel Planner"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.view_static_card_container),
                                        0),
                                0),
                        isDisplayed()));
        textView.perform(click());//check(matches(withText("Travel Planner")));

        /*ViewInteraction textView2 = onView(
                allOf(withId(R.id.view_static_card_action_text), withText("PLAN A TRIP"),
                        childAtPosition(
                                allOf(withId(R.id.view_static_card_action_container),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                1)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("PLAN A TRIP")));*/

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
