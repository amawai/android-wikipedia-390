package org.wikipedia.travel;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.wikipedia.activity.SingleFragmentActivity;


/**
 * Created by mnhn3 on 2018-03-04.
 */
public class PlacesToVisitActivity extends SingleFragmentActivity<PlacesFragment> {
    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, PlacesToVisitActivity.class);
    }

    @Override
    protected PlacesFragment createFragment() {
        return new PlacesFragment();
    }
}

