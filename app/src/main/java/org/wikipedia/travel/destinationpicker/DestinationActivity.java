package org.wikipedia.travel.destinationpicker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.wikipedia.activity.SingleFragmentActivity;


/**
 * Created by aman_ on 3/3/2018.
 */


public class DestinationActivity extends SingleFragmentActivity<DestinationFragment> {

    // Returns the DestinationActivity as new Intent so the caller can start the activity
    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, DestinationActivity.class);
    }

    // Creates the DestinationFragment
    @Override
    protected DestinationFragment createFragment() {
        return new DestinationFragment();
    }
}
