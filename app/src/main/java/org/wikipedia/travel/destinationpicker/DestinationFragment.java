package org.wikipedia.travel.destinationpicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.wikipedia.R;
import org.wikipedia.travel.datepicker.DateActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by abhandal on 3/3/2018.
 */

public class DestinationFragment extends Fragment implements View.OnClickListener {

    private Unbinder unbinder;
    private FloatingActionButton nextButton;
    private static String[] destinationString;

    //The method will assemble the destinationFragment and invoke the Google Place Autocomplete widget
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_travel_destination_picker, container, false);
        unbinder = ButterKnife.bind(this, view);
        getAppCompatActivity().getSupportActionBar().setTitle(getString(R.string.view_travel_card_title));

        nextButton = (FloatingActionButton) view.findViewById(R.id.destination_button_next);
        nextButton.setOnClickListener(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.fragment_place_autocomplete);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                setDestinationArray((String) place.getName(), (String) place.getAddress());
            }

            @Override
            public void onError(Status status) {
                Log.i("Autocomplete Failed", status.getStatusMessage());
            }
        });
        return view;
    }

    public void onClick(View v) {
        Intent i = new Intent(getActivity(), DateActivity.class);
        startActivity(i);
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    //Grabs the user selected city and stores it in a 2 element array.
    //The destinationName is the name of city, destinationAddress includes the city, state, and country
    private void setDestinationArray(String destinationName, String destinationAddress) {
        this.destinationString = new String[2];
        this.destinationString[0] = destinationName;
        this.destinationString[1] = destinationAddress;
    }

    //Returns the destinationArray, to be used for getting wikipedia articles for city surrounding
    public static String[] getDestinationString() {
        return destinationString;
    }

}
