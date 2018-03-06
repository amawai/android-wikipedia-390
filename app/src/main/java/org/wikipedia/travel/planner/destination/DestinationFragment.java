package org.wikipedia.travel.planner.destination;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import org.wikipedia.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by aman_ on 3/3/2018.
 */

public class DestinationFragment extends Fragment {
    private Unbinder unbinder;
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    private String[] destinationString;

    public static DestinationFragment newInstance() {

        DestinationFragment fragment = new DestinationFragment();
        return fragment;
    }

    // The method will assemble the destinationFragment and invoke the Google Place Autocomplete widget
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.travel_destination_picker_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        getAppCompatActivity().getSupportActionBar().setTitle(getString(R.string.view_travel_card_title));

        autocompleteFragment = (SupportPlaceAutocompleteFragment)getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                setDestinationArray((String)place.getName(), (String)place.getAddress());
            }

            @Override
            public void onError(Status status) {
                Log.i("Autocomplete Failed", status.getStatusMessage());
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    // Grabs the user selected city and stores it in a 2 element array.
    // The destinationName is the name of city, destinationAddress includes the city, state, and country
    private void setDestinationArray(String destinationName, String destinationAddress) {
        this.destinationString = new String[2];
        this.destinationString[0] = destinationName;
        this.destinationString[1] = destinationAddress;
    }

    // Returns the destinationArray, to be used for getting wikipedia articles for city surrounding
    public String[] getDestinationString() {
        return this.destinationString;
    }

}
