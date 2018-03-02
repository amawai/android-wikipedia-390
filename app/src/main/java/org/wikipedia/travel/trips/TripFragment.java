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

import org.wikipedia.R;

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

        tripAdapter = new TripAdapter(getContext());
        tripList.setAdapter(tripAdapter);
        tripList.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d("tripfragment", "oncreate vieww!!");
        getAppCompatActivity().getSupportActionBar().setTitle("Why u tripping");
        return view;
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    /*
    TODO: Create trip_row_layout.xml, i.e. the row displaying trips, that will populate recycle view
    TODO: Discuss how database will be implemented
    TODO: Fix up Holder and Adapter classes
    TODO: Basically just copy paste the code sent on fb lmao

    */

}
