package org.wikipedia.travel.planner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wikipedia.BackPressedHandler;
import org.wikipedia.R;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Artem on 2018-03-02.
 */

public class DateFragment extends Fragment {
    public static DateFragment newInstance() {

        return new DateFragment();
    }

    public static TripsFragment newInstance(ArrayList<String> trips) {
        Bundle args = new Bundle();
        TripsFragment fragment = new TripsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel_data, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
