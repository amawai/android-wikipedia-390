package org.wikipedia.travel;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.wikipedia.activity.SingleFragmentActivity;


public class TravelDatePickerActivity extends SingleFragmentActivity<TravelDatePickerFragment> {

    // Returns the TravelDatePickerActivity as new Intent so the caller can start the activity
    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, TravelDatePickerActivity.class);
    }

    // Creates the TravelDatePickerFragment
    @Override
    protected TravelDatePickerFragment createFragment() {
        return new TravelDatePickerFragment();
    }
}
