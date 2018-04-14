package org.wikipedia.imagesearch;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.wikipedia.activity.SingleFragmentActivity;

import java.util.ArrayList;

/**
 * Created by amawai on 11/04/18.
 */

public class ImageSearchActivity extends SingleFragmentActivity<ImageSearchFragment> {
    public static final String IMAGE_LABEL_ARGS = "IMAGE_LABEL_ARGS";

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, ImageSearchActivity.class);
    }

    @Override
    protected ImageSearchFragment createFragment() {
        ArrayList<String> args = getIntent().getStringArrayListExtra(IMAGE_LABEL_ARGS);
        ImageSearchFragment fragment = ImageSearchFragment.newInstance(args);
        return fragment;
    }
}
