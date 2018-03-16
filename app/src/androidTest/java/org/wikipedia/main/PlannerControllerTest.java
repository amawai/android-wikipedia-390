package org.wikipedia.main;


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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wikipedia.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PlannerControllerTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void plannerControllerTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try{
            ViewInteraction appCompatTextView = onView(
                    allOf(withId(R.id.fragment_onboarding_skip_button), withText("Skip"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.LinearLayout")),
                                            1),
                                    0),
                            isDisplayed()));
            appCompatTextView.perform(click());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            ViewInteraction appCompatImageView = onView(
                    allOf(withId(R.id.view_list_card_header_menu), withContentDescription("More options"),
                            childAtPosition(
                                    allOf(withId(R.id.view_list_card_header),
                                            childAtPosition(
                                                    withClassName(is("android.support.constraint.ConstraintLayout")),
                                                    0)),
                                    3),
                            isDisplayed()));
            appCompatImageView.perform(click());

            ViewInteraction appCompatTextView2 = onView(
                    allOf(withId(R.id.title), withText("Hide this card"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                            0),
                                    0),
                            isDisplayed()));
            appCompatTextView2.perform(click());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try{
            ViewInteraction appCompatTextView3 = onView(
                    allOf(withId(R.id.view_announcement_action_negative), withText("Got it"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.widget.LinearLayout")),
                                            2),
                                    0),
                            isDisplayed()));
            appCompatTextView3.perform(click());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{

            ViewInteraction appCompatImageView2 = onView(
                    allOf(withId(R.id.view_list_card_header_menu), withContentDescription("More options"),
                            childAtPosition(
                                    allOf(withId(R.id.view_on_this_day_card_header),
                                            childAtPosition(
                                                    withClassName(is("android.widget.LinearLayout")),
                                                    0)),
                                    3),
                            isDisplayed()));
            appCompatImageView2.perform(click());

            ViewInteraction appCompatTextView4 = onView(
                    allOf(withId(R.id.title), withText("Hide this card"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                            0),
                                    0),
                            isDisplayed()));
            appCompatTextView4.perform(click());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{

            ViewInteraction appCompatImageView3 = onView(
                    allOf(withId(R.id.view_list_card_header_menu), withContentDescription("More options"),
                            childAtPosition(
                                    allOf(withId(R.id.view_list_card_header),
                                            childAtPosition(
                                                    withClassName(is("android.support.constraint.ConstraintLayout")),
                                                    0)),
                                    3),
                            isDisplayed()));
            appCompatImageView3.perform(click());

            ViewInteraction appCompatTextView5 = onView(
                    allOf(withId(R.id.title), withText("Hide this card"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                            0),
                                    0),
                            isDisplayed()));
            appCompatTextView5.perform(click());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{

            ViewInteraction appCompatImageView4 = onView(
                    allOf(withId(R.id.view_static_card_action_overflow), withContentDescription("More options"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("org.wikipedia.feed.mainpage.MainPageCardView")),
                                            0),
                                    2),
                            isDisplayed()));
            appCompatImageView4.perform(click());

            ViewInteraction appCompatTextView6 = onView(
                    allOf(withId(R.id.title), withText("Hide this card"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                            0),
                                    0),
                            isDisplayed()));
            appCompatTextView6.perform(click());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{

            ViewInteraction appCompatImageView5 = onView(
                    allOf(withId(R.id.view_static_card_action_overflow), withContentDescription("More options"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("org.wikipedia.feed.random.RandomCardView")),
                                            0),
                                    2),
                            isDisplayed()));
            appCompatImageView5.perform(click());

            ViewInteraction appCompatTextView7 = onView(
                    allOf(withId(R.id.title), withText("Hide this card"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                            0),
                                    0),
                            isDisplayed()));
            appCompatTextView7.perform(click());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{

            ViewInteraction appCompatImageView6 = onView(
                    allOf(withId(R.id.view_list_card_header_menu), withContentDescription("More options"),
                            childAtPosition(
                                    allOf(withId(R.id.view_featured_article_card_header),
                                            childAtPosition(
                                                    withClassName(is("android.support.constraint.ConstraintLayout")),
                                                    0)),
                                    3),
                            isDisplayed()));
            appCompatImageView6.perform(click());

            ViewInteraction appCompatTextView8 = onView(
                    allOf(withId(R.id.title), withText("Hide this card"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                            0),
                                    0),
                            isDisplayed()));
            appCompatTextView8.perform(click());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{

            ViewInteraction appCompatImageView7 = onView(
                    allOf(withId(R.id.view_list_card_header_menu), withContentDescription("More options"),
                            childAtPosition(
                                    allOf(withId(R.id.view_featured_image_card_header),
                                            childAtPosition(
                                                    withClassName(is("android.widget.LinearLayout")),
                                                    0)),
                                    3),
                            isDisplayed()));
            appCompatImageView7.perform(click());

            ViewInteraction appCompatTextView9 = onView(
                    allOf(withId(R.id.title), withText("Hide this card"),
                            childAtPosition(
                                    childAtPosition(
                                            withClassName(is("android.support.v7.view.menu.ListMenuItemView")),
                                            0),
                                    0),
                            isDisplayed()));
            appCompatTextView9.perform(click());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.view_static_card_action_container),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("org.wikipedia.travel.TravelCardView")),
                                        0),
                                1),
                        isDisplayed()));
        linearLayout.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.trip_button_new), withText("Plan a new trip"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_travel_planner_view_pager),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction noSwipeViewPager = onView(
                allOf(withId(R.id.fragment_travel_planner_view_pager),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_travel_planner),
                                        0),
                                1),
                        isDisplayed()));
        noSwipeViewPager.perform(swipeLeft());

        ViewInteraction button = onView(
                allOf(withId(R.id.planner_next),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.planner_title), withText("Destination"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Destination")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.planner_title), withText("Destination"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Destination")));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.planner_next), withText("NEXT"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction noSwipeViewPager2 = onView(
                allOf(withId(R.id.fragment_travel_planner_view_pager),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_travel_planner),
                                        0),
                                1),
                        isDisplayed()));
        noSwipeViewPager2.perform(swipeLeft());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.planner_title), withText("Departure Date"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Departure Date")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.planner_title), withText("Departure Date"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Departure Date")));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.planner_next), withText("NEXT"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction noSwipeViewPager3 = onView(
                allOf(withId(R.id.fragment_travel_planner_view_pager),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_travel_planner),
                                        0),
                                1),
                        isDisplayed()));
        noSwipeViewPager3.perform(swipeLeft());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.planner_title), withText("Landmarks"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("Landmarks")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.planner_title), withText("Landmarks"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView6.check(matches(withText("Landmarks")));

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.planner_save), withText("SAVE"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction noSwipeViewPager4 = onView(
                allOf(withId(R.id.fragment_travel_planner_view_pager),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragment_travel_planner),
                                        0),
                                1),
                        isDisplayed()));
        noSwipeViewPager4.perform(swipeRight());

        ViewInteraction relativeLayout = onView(
                allOf(withId(R.id.trip_info),
                        childAtPosition(
                                allOf(withId(R.id.trip_item_container),
                                        childAtPosition(
                                                withId(R.id.trip_list_view_recycler),
                                                0)),
                                0),
                        isDisplayed()));
        relativeLayout.check(matches(isDisplayed()));

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
