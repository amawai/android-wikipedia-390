package org.wikipedia.espresso.imagesearch;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
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
import static org.hamcrest.Matchers.allOf;

/**
 * Created by amawai on 12/04/18.
 */

public class ImageSearchFragmentTest {
    private final String MOCK_LABEL_1 = "Test";
    private final String MOCK_LABEL_2 = "Espresso";
    private final String MOCK_LABEL_3 = "Android";
    private final String MOCK_LABEL_4 = "Wikipedia";

    @Rule
    public ActivityTestRule<ImageSearchActivity> mActivityRule =
            new ActivityTestRule<ImageSearchActivity>(ImageSearchActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, ImageSearchActivity.class);

                    ArrayList<String> mockLabels = new ArrayList<>();
                    mockLabels.add(MOCK_LABEL_1);
                    mockLabels.add(MOCK_LABEL_2);
                    mockLabels.add(MOCK_LABEL_3);
                    mockLabels.add(MOCK_LABEL_4);
                    //Populate list before testing
                    result.putStringArrayListExtra(ImageSearchActivity.IMAGE_LABEL_ARGS, mockLabels);
                    return result;
                }
            };


    @Test
    public void testThatLabelsArePopulated() {
        ViewInteraction labelView = onView(
                allOf(withId(R.id.fragment_imagesearch)));
        labelView.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.imagesearch_label_title), withText(MOCK_LABEL_1)));
        textView.check(matches(withText(MOCK_LABEL_1)));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.imagesearch_label_title), withText(MOCK_LABEL_2)));
        textView2.check(matches(withText(MOCK_LABEL_2)));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.imagesearch_label_title), withText(MOCK_LABEL_3)));
        textView3.check(matches(withText(MOCK_LABEL_3)));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.imagesearch_label_title), withText(MOCK_LABEL_4)));
        textView4.check(matches(withText(MOCK_LABEL_4)));
    }
}
