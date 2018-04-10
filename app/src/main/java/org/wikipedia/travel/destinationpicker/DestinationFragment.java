package org.wikipedia.travel.destinationpicker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import org.wikipedia.R;
import org.wikipedia.activity.FragmentUtil;
import org.wikipedia.travel.trip.Trip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by abhandal on 3/3/2018.
 */

public class DestinationFragment extends Fragment {

    private Unbinder unbinder;
    private DestinationAdapter destinationAdapter;
    private List<Trip> userDestinationList = new ArrayList<>();
    private SupportPlaceAutocompleteFragment autocompleteFragment;

    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
    @BindView(R.id.destination_history_view_recycler) RecyclerView destinationList;

    Place destination;

    public interface Callback{
        void onPlaceSelected(Place place);
        void onRequestDestinationListUpdate();
        String onRequestOpenDestinationName();
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
                if(getCallback() != null) {
                    getCallback().onPlaceSelected(place);
                    updateUserTripList();
                }
            }

            @Override
            public void onError(Status status) {
                Log.i("Autocomplete Failed", status.getStatusMessage());
            }
        });

        updateUserTripList();

        // Reverse the order of the destination history list
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        destinationAdapter = new DestinationAdapter(getContext());
        destinationList.setAdapter(destinationAdapter);
        destinationList.setLayoutManager(mLayoutManager);
        return view;
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    public Callback getCallback() {
        return FragmentUtil.getCallback(this, Callback.class);
    }

    private void updateUserTripList() {
        if(getCallback() != null) {
            getCallback().onRequestDestinationListUpdate();
        }
    }

    public void setUserDestinationList(List<Trip> trips) {
        if(destinationAdapter != null) {
            destinationAdapter.setUserDestination(trips);
        }
    }

    // Adapter for the RecyclerView
    public final class DestinationAdapter extends RecyclerView.Adapter<DestinationItemHolder> {
        private Context context;
        private List<Trip> userDestinationList;

        public DestinationAdapter(Context context) {
            this.context = context;
            this.userDestinationList = new ArrayList <>();
        }

        @Override
        public DestinationItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_travel_destination_picker_saved_destinations, parent, false);
            return new DestinationItemHolder(v);
        }

        @Override
        public void onBindViewHolder(DestinationItemHolder holder, int position) {
            holder.bindItem(userDestinationList.get(position));
        }

        @Override
        public int getItemCount() {
            return userDestinationList.size();
        }

        public void setUserDestination(List<Trip> trips) {
            this.userDestinationList = trips;
            notifyDataSetChanged();
            mLayoutManager.scrollToPositionWithOffset(userDestinationList.size()-1, 0);
        }

        // Remove a RecyclerView item containing a specified Data object, could be used in the future
        public void remove(Trip data) {
            int position = userDestinationList.indexOf(data);
            userDestinationList.remove(position);
            notifyItemRemoved(position);
        }

        // Insert a new item to the RecyclerView on a predefined position, could be used in the future
        public void insert(int position, Trip data) {
            userDestinationList.add(position, data);
            notifyItemInserted(position);
        }
    }

    public final class DestinationItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int index;
        private long id;

        @BindView(R.id.destination_view_list) RelativeLayout destinationLayout;
        @BindView(R.id.destination_name_view_text) TextView destinationName;
        @BindView(R.id.destination_item_icon) ImageView destinationIcon;

        public DestinationItemHolder(View destinationView) {
            super(destinationView);

            unbinder = ButterKnife.bind(this, destinationView);
            destinationLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        public void bindItem(Trip trip) {
            destinationName.setText(trip.getDestination().getDestinationName());
            id = trip.getId();
        }
    }


}
