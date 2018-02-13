package org.wikipedia.main;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import org.wikipedia.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class OnThisDayShareTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void onThisDayShareTest() {
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(2000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

        ViewInteraction appCompatTextView = onView(
allOf(withId(R.id.fragment_onboarding_skip_button), withText("Skip"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
1),
0),
isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatTextView2 = onView(
allOf(withId(R.id.fragment_onboarding_skip_button), withText("Skip"),
childAtPosition(
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
1),
0),
isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction actionMenuItemView = onView(
allOf(withId(R.id.menu_overflow_button), withContentDescription("More options"),
childAtPosition(
childAtPosition(
withId(R.id.single_fragment_toolbar),
1),
1),
isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction actionMenuItemView2 = onView(
allOf(withId(R.id.menu_overflow_button), withContentDescription("More options"),
childAtPosition(
childAtPosition(
withId(R.id.single_fragment_toolbar),
1),
1),
isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction actionMenuItemView3 = onView(
allOf(withId(R.id.menu_overflow_button), withContentDescription("More options"),
childAtPosition(
childAtPosition(
withId(R.id.single_fragment_toolbar),
1),
1),
isDisplayed()));
        actionMenuItemView3.perform(click());

        ViewInteraction imageView = onView(
allOf(withId(R.id.view_card_on_this_day_footer_share_button), withContentDescription("Share via"),
childAtPosition(
childAtPosition(
withId(R.id.more_events_layout),
2),
0),
isDisplayed()));
        imageView.check(matches(isDisplayed()));

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
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
