package org.wikipedia.travel;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import org.wikipedia.activity.SingleFragmentActivity;

/**
 * Created by Artem on 2018-02-26.
 */

public class TravelPlannerActivity extends SingleFragmentActivity<TravelFragment> {
    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, TravelPlannerActivity.class);
    }

    @Override
    protected TravelFragment createFragment() {
        return new TravelFragment();
    }
}
