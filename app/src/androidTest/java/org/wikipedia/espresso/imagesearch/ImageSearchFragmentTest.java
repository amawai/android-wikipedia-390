package org.wikipedia.espresso.imagesearch;


import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.wikipedia.R;
import org.wikipedia.imagesearch.ImageSearchActivity;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by amawai on 12/04/18.
 */

public class ImageSearchFragmentTest {
    private final String label1 = "Test";
    private final String label2 = "Espresso";
    private final String label3 = "Android";
    private final String label4 = "Wikipedia";

    @Rule
    public ActivityTestRule<ImageSearchActivity> mActivityRule =
            new ActivityTestRule<ImageSearchActivity>(ImageSearchActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, ImageSearchActivity.class);

                    ArrayList<String> mockLabels = new ArrayList<>();
                    mockLabels.add(label1);
                    mockLabels.add(label2);
                    mockLabels.add(label3);
                    mockLabels.add(label4);
                    //Populate list before testing
                    result.putStringArrayListExtra(ImageSearchActivity.IMAGE_LABEL_ARGS, mockLabels);
                    return result;
                }
            };


    @Test
    public void testThatLabelsArePopulated() {
        ViewInteraction labelsView = onView(
                allOf(withId(R.id.fragment_imagesearch)));
        labelsView.check(matches(isDisplayed()));
        assertNotNull(labelsView);

        ViewInteraction labelView1 = onView(
                allOf(withId(R.id.imagesearch_label_title), withText(label1)));
        labelView1.check(matches(withText(label1)));

        ViewInteraction labelView2 = onView(
                allOf(withId(R.id.imagesearch_label_title), withText(label2)));
        labelView2.check(matches(withText(label2)));

        ViewInteraction labelView3 = onView(
                allOf(withId(R.id.imagesearch_label_title), withText(label3)));
        labelView3.check(matches(withText(label3)));

        ViewInteraction labelView4 = onView(
                allOf(withId(R.id.imagesearch_label_title), withText(label4)));
        labelView4.check(matches(withText(label4)));
    }
}
