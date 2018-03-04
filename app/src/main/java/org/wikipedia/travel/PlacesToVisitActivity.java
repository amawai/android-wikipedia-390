package org.wikipedia.travel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import org.wikipedia.R;
import org.wikipedia.activity.SingleFragmentActivity;

import java.util.ArrayList;
import java.util.List;

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

