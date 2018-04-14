package org.wikipedia.espresso.translation;


import android.graphics.Rect;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wikipedia.R;
import org.wikipedia.travel.TravelPlannerActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

/**
 * Purpose: This tests the longClick functionality within an article for the Translate button
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LongPressEspressoTest {

    private UiDevice mDevice;

    @Rule
    public ActivityTestRule<TravelPlannerActivity> dActivityTestRule = new ActivityTestRule<>(TravelPlannerActivity.class);

    @Before
    public void setUp() throws UiObjectNotFoundException {
        //Allows testing of components outside of testing context, in this case: destination
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        ViewInteraction planNewTripButton = onView(
                allOf(withId(R.id.trip_button_new)));
        planNewTripButton.perform(click());

        selectDestination();
        //Navigate to the LandmarkFragment
        UiObject nextButton = mDevice.findObject(new UiSelector()
                .text("NEXT"));
        try {
            nextButton.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        try {
            nextButton.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ensureLongPressDisplaysActionBar() throws UiObjectNotFoundException {
        UiObject montrealArticleCard = mDevice.findObject(new UiSelector()
                .text("Montreal Central Station"));
        try {
            montrealArticleCard.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        //Checks that the article with the following description opens up and exists
        UiObject montrealArticle = mDevice.findObject(new UiSelector()
                .text("Railway station in Montreal, Quebec, Canada"));

        Rect coordinates = montrealArticle.getBounds();
        mDevice.swipe(coordinates.centerX(), coordinates.centerY(), coordinates.centerX(), coordinates.centerY(), 100);

    }


    private void selectDestination() throws UiObjectNotFoundException{
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
