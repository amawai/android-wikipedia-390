package org.wikipedia.espresso.notes;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wikipedia.R;
import org.wikipedia.main.MainActivity;
import org.wikipedia.travel.TravelPlannerActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static junit.framework.Assert.assertTrue;

/**
 * Created by amawai on 30/03/18.
 */

public class ArticleNoteTest {
    private UiDevice mDevice;

    @Rule
    public ActivityTestRule<TravelPlannerActivity> mActivityTestRule = new ActivityTestRule<>(TravelPlannerActivity.class);

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
        UiObject montrealArticleCard = mDevice.findObject(new UiSelector()
                .text("Montreal Central Station"));
        try {
            montrealArticleCard.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void articleNotesOptionExists() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        UiObject articleNotesTab = mDevice.findObject(new UiSelector()
                .text("Article notes"));

        try {
            articleNotesTab.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        UiObject editButton = mDevice.findObject(new UiSelector()
                .text("EDIT"));

        UiObject deleteButton = mDevice.findObject(new UiSelector()
                .text("DELETE"));

        UiObject dismissButton = mDevice.findObject(new UiSelector()
                .text("DISMISS"));

        assertTrue(editButton.exists());
        assertTrue(deleteButton.exists());
        assertTrue(dismissButton.exists());

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