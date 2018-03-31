package org.wikipedia.espresso.translation;


import android.support.test.espresso.ViewInteraction;
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
import org.wikipedia.page.PageActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

/**
 * Purpose: This tests the longClick functionality within an article for the Translate button
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LongPressEspressoTest {

    @Rule
    public ActivityTestRule <PageActivity> mActivityTestRule = new ActivityTestRule <>(PageActivity.class);

    @Test
    public void mainActivityTest4() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction observableWebView = onView(
                allOf(withId(R.id.page_web_view),
                        childAtPosition(
                                allOf(withId(R.id.page_contents_container),
                                        childAtPosition(
                                                withId(R.id.page_refresh_container),
                                                0)),
                                0),
                        isDisplayed()));
        observableWebView.perform(longClick());
    }

    private static Matcher <View> childAtPosition(
            final Matcher <View> parentMatcher, final int position) {

        return new TypeSafeMatcher <View>() {
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
