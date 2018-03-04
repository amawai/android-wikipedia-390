package org.wikipedia.travel.destinationpicker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.wikipedia.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by aman_ on 3/3/2018.
 */

public class DestinationFragment extends Fragment {
    private Unbinder unbinder;
    private FloatingActionButton nextButton;
    private String[] destinationString;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.travel_destination_picker_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        getAppCompatActivity().getSupportActionBar().setTitle(getString(R.string.view_travel_card_title));

        PlaceAutocompleteFragment autocompleteFragment  = (PlaceAutocompleteFragment)getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                setDestinationArray((String)place.getName(), (String)place.getAddress());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }
        });

        return view;
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    private void setDestinationArray(String destinationName, String destinationAddress) {
        this.destinationString = new String[2];
        this.destinationString[0] = destinationName;
        this.destinationString[1] = destinationAddress;
    }

    public String[] getDestinationString() {
        return this.destinationString;
    }

}
