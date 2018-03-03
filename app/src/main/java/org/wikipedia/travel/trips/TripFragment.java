package org.wikipedia.travel.trips;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.wikipedia.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by amawai on 28/02/18.
 */

public class TripFragment extends Fragment {
    private Unbinder unbinder;
    private FloatingActionButton planNewTrip;
    private TripAdapter tripAdapter;

    @BindView(R.id.trip_list) RecyclerView tripList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_trip_display, container, false);
        unbinder = ButterKnife.bind(this, view);

        List<Trip> data = fill_with_data();

        tripAdapter = new TripAdapter(data, getContext());
        tripList.setAdapter(tripAdapter);
        tripList.setLayoutManager(new LinearLayoutManager(getContext()));
        getAppCompatActivity().getSupportActionBar().setTitle("Trip Creation");
        return view;
    }

    //Mock data for now until database access is complete
    public List<Trip> fill_with_data() {
        List<Trip> data = new ArrayList<>();

        data.add(new Trip("Moscow Trip", "January 1st ", 1));
        data.add(new Trip("Japan Adventure", "February 25th ", 2));
        data.add(new Trip("Canada Exploration", "March 12th", 3));
        data.add(new Trip("Mountain Trek", "April 30th ", 4));
        data.add(new Trip("whatever", "May 17th ",5));
        data.add(new Trip("I want vacation", "September 9th:", 6));

        return data;
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }
}
