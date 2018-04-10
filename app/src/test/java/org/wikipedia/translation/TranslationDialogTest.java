package org.wikipedia.translation;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.wikipedia.R;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;


/**
 * Created by SunXP on 2018-03-30.
 */

@RunWith(RobolectricTestRunner.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest(Context.class)
public class TranslationDialogTest {

    Context mContext;
    Bundle mBundle;

    @Before
    public void setUp() {
    }

    @Test
    public void testFragment() {
        TranslationDialog translationDialog = new TranslationDialog();
        translationDialog.newInstance("Dog");
        // tests that the dialog instance is created
        assertNotNull(translationDialog);
    }


}