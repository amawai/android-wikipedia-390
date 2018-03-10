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
import org.wikipedia.travel.landmarkpicker.LandmarkActivity;
import org.wikipedia.travel.trip.TripFragment;
import org.wikipedia.travel.destinationpicker.DestinationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Artem and his peeps on 2018-02-26.
 */

public class TravelFragment extends Fragment implements View.OnClickListener {

    private Unbinder unbinder;
    private FloatingActionButton nextButton;

    @BindView(R.id.travel_dnext_button_next) Button dNextButton;
    @BindView(R.id.travel_destination_button_next) Button destinationButton;
    @BindView(R.id.travel_go_plan_trip_button_next) Button goToPlanTrip;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_travel_trip, container, false);

        //currently the button on the 1st travel planner activity
        dNextButton.setOnClickListener(this);

        //Sets listeners for button clicks
        destinationButton.setOnClickListener(this);

        goToPlanTrip.setOnClickListener(this);


        unbinder = ButterKnife.bind(this, view);
        getAppCompatActivity().getSupportActionBar().setTitle(getString(R.string.view_travel_card_title));

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

    //When the next button selected it will create a new destination picker activity
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.travel_destination_button_next:
                getContext().startActivity(new Intent(DestinationActivity.newIntent(getContext())));
                break;
            case R.id.travel_go_plan_trip_button_next:
                goToTripView(v);
                break;
            case R.id.travel_dnext_button_next:
                Intent i = new Intent(getActivity(), LandmarkActivity.class);
                startActivity(i);
        }
    }

}
