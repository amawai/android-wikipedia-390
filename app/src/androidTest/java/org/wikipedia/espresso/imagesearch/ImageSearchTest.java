package org.wikipedia.espresso.imagesearch;

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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.wikipedia.R;
import org.wikipedia.main.MainActivity;
import org.wikipedia.main.MainFragment;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertFalse;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static junit.framework.Assert.assertTrue;

/**
 * Created by SunXP on 2018-04-12.
 */

public class ImageSearchTest {
    private UiDevice mDevice;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws UiObjectNotFoundException {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        ViewInteraction imageSearchButton = onView(allOf(withId(R.id.search_image_button)));
    }

    @Test
    public void imageSearchButtonsExists() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UiObject imageSearchButton = mDevice.findObject(new UiSelector()
                .className("android.widget.ImageView").enabled(true).instance(1));

        //tests that the image search button exists in the main feed
        assertTrue(imageSearchButton.exists());

        try {
            imageSearchButton.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UiObject denyPermission = mDevice.findObject(new UiSelector()
                .text("DENY"));

        try {
            //Dismiss permission if it exists
            if (denyPermission.exists()){
                denyPermission.click();
            }
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        UiObject chooserDialog = mDevice.findObject(new UiSelector()
                .text("Image Search: Select Source"));

        // test that the dialog appears
        assertTrue(chooserDialog.exists());

        UiObject cameraButton = mDevice.findObject(new UiSelector()
                .instance(1));

        // test that the camera button exists
        assertTrue(cameraButton.exists());

        UiObject galleryButton = mDevice.findObject(new UiSelector()
                .instance(2));

        // test that the gallery button exists
        assertTrue(galleryButton.exists());

        UiObject photoButton = mDevice.findObject(new UiSelector()
                .instance(3));
        
        // test that the photo button exists
        assertTrue(photoButton.exists());
    }

}
