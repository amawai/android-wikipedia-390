package org.wikipedia.travel.landmarkpicker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.wikipedia.activity.SingleFragmentActivity;

/**
 * Created by mnhn3 on 2018-03-04.
 */

public class LandmarkActivity extends SingleFragmentActivity<LandmarkFragment> {

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, LandmarkActivity.class);
    }

    @Override
    protected LandmarkFragment createFragment() {
        return new LandmarkFragment();
    }

}

