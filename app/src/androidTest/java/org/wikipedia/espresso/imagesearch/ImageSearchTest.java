package org.wikipedia.espresso.imagesearch;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.wikipedia.main.MainActivity;
import static junit.framework.Assert.assertTrue;

/**
 * Created by SunXP on 2018-04-12.
 */

public class ImageSearchTest {
    private UiDevice mDevice;
    private int timeForButtonToAppear;
    private int cameraPosition;
    private int galleryPosition;
    private int photoPosition;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws UiObjectNotFoundException {
        cameraPosition = 1;
        galleryPosition = 2;
        photoPosition = 3;
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        timeForButtonToAppear = 2000;
    }

    @Test
    public void imageSearchButtonsExists() {
        try {
            Thread.sleep(timeForButtonToAppear);
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
            Thread.sleep(timeForButtonToAppear);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UiObject denyPermission = mDevice.findObject(new UiSelector()
                .text("DENY"));

        try {
            //Dismiss permission if it exists
            if (denyPermission.exists()) {
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
                .instance(cameraPosition));

        // test that the camera button exists
        assertTrue(cameraButton.exists());

        UiObject galleryButton = mDevice.findObject(new UiSelector()
                .instance(galleryPosition));

        // test that the gallery button exists
        assertTrue(galleryButton.exists());

        UiObject photoButton = mDevice.findObject(new UiSelector()
                .instance(photoPosition));

        // test that the photo button exists
        assertTrue(photoButton.exists());
    }
}
