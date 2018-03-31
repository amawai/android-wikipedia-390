package org.wikipedia.espresso.translation;

import android.graphics.Rect;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Root;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.wikipedia.R;
import org.wikipedia.travel.TravelPlannerActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by amawai on 14/03/18.
 */

public class TranslatorDialogEspressoTest {
    private UiDevice mDevice;

    @Rule
    public ActivityTestRule<TravelPlannerActivity> dActivityTestRule = new ActivityTestRule<>(TravelPlannerActivity.class);

    @Before
    public void setUp() throws UiObjectNotFoundException{
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
    public void translatorDialogAppearanceTest() throws UiObjectNotFoundException {
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


        ViewInteraction translateButton = onView(withText("Translate")).inRoot(isPopupWindow());
        translateButton.perform(click());

        UiObject translationOverlay = mDevice.findObject(new UiSelector()
                .text("Translation"));

        assertTrue(translationOverlay.exists());
    }

    public static Matcher<Root> isPopupWindow() {
        return isPlatformPopup();
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
