package org.wikipedia.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.wikipedia.R;
import org.wikipedia.travel.trips.TripFragment;
import org.wikipedia.travel.destinationpicker.DestinationActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Artem on 2018-02-26.
 */

public class TravelFragment extends Fragment implements View.OnClickListener{
    private Unbinder unbinder;
    private FloatingActionButton nextButton;
    private Button goToPlanTrip;
    private Button destinationButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_travel_planner, container, false);

        // Sets listeners for button clicks
        destinationButton = (Button) view.findViewById(R.id.tp_next_button);
        destinationButton.setOnClickListener(this);

        unbinder = ButterKnife.bind(this, view);
        getAppCompatActivity().getSupportActionBar().setTitle(getString(R.string.view_travel_card_title));

        goToPlanTrip = (Button) view.findViewById(R.id.trip_view);
        goToPlanTrip.setOnClickListener(this);
        return view;
    }

    public void goToTripView(View v) {
        Fragment fragment = new TripFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.travel_planner_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    // When the next button selected it will create a new destination picker activity
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.tp_next_button:
                getContext().startActivity(new Intent(DestinationActivity.newIntent(getContext())));
                break;
            case R.id.trip_view:
                goToTripView(v);
                break;
        }
    }
}
