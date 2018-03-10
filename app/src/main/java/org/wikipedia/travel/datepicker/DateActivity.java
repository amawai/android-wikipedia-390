package org.wikipedia.travel.datepicker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import org.wikipedia.activity.SingleFragmentActivity;


public class DateActivity extends SingleFragmentActivity<DateFragment> {

    //Returns the DateActivity as new Intent so the caller can start the activity
    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, DateActivity.class);
    }

    //Creates the DateFragment
    @Override
    protected DateFragment createFragment() {
        return new DateFragment();
    }

}
