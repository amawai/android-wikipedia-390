package org.wikipedia.imagesearch;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.wikipedia.activity.SingleFragmentActivity;

/**
 * Created by amawai on 11/04/18.
 */

public class ImageSearchActivity extends SingleFragmentActivity<ImageSearchFragment>{

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, ImageSearchActivity.class);
    }

    @Override
    protected ImageSearchFragment createFragment() {
        return new ImageSearchFragment();
    }

}
