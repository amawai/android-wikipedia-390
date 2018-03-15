package org.wikipedia.travel;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.wikipedia.activity.SingleFragmentActivity;
import org.wikipedia.travel.trip.TripFragment;

/**
 * Created by Artem on 2018-02-26.
 */

public class TravelPlannerActivity extends SingleFragmentActivity<TripFragment> {

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, TravelPlannerActivity.class);
    }

    @Override
    protected TripFragment createFragment() {
        return new TripFragment();
    }

}
