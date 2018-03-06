package org.wikipedia.travel.planner;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.wikipedia.activity.SingleFragmentActivity;

/**
 * Created by Artem on 2018-02-26.
 */

public class TravelPlannerActivity extends SingleFragmentActivity<MainPlannerFragment> {
    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, TravelPlannerActivity.class);
    }

    @Override
    protected MainPlannerFragment createFragment() {
        return new MainPlannerFragment();
    }

    @Override
    public void onBackPressed() {
        if(!getFragment().onBackPressed()) {
            super.onBackPressed();
        };
    }
}
