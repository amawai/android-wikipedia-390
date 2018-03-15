package org.wikipedia.travel.destinationpicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import org.wikipedia.R;
import org.wikipedia.activity.FragmentUtil;
import org.wikipedia.travel.datepicker.DateActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by abhandal on 3/3/2018.
 */

public class DestinationFragment extends Fragment implements View.OnClickListener {

    private Unbinder unbinder;
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    @BindView(R.id.destination_text_view) TextView tvDestination;
    Place destination;

    public interface Callback{
        void onPlaceSelected(Place place);
    }

    public static DestinationFragment newInstance(String destination) {

        Bundle args = new Bundle();
        args.putString("DESTINATION", destination);

        DestinationFragment fragment = new DestinationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //The method will assemble the destinationFragment and invoke the Google Place Autocomplete widget
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_travel_destination_picker, container, false);
        unbinder = ButterKnife.bind(this, view);

        autocompleteFragment = (SupportPlaceAutocompleteFragment)getChildFragmentManager().findFragmentById(R.id.fragment_place_autocomplete);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                destination = place;
                updateDestinationText(place);
                if(getCallback() != null) {
                    getCallback().onPlaceSelected(place);
                }
            }

            @Override
            public void onError(Status status) {
                Log.i("Autocomplete Failed", status.getStatusMessage());
            }
        });

        tvDestination.setText(getArguments().getString("DESTINATION"));
        return view;
    }

    public void onClick(View v) {
        Intent i = new Intent(getActivity(), DateActivity.class);
        startActivity(i);
    }

    private void updateDestinationText(Place destination) {
        tvDestination.setText(destination.getName());
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    public Callback getCallback() {
        return FragmentUtil.getCallback(this, Callback.class);
    }
}
