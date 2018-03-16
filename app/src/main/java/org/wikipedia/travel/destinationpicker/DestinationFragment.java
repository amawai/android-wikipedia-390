package org.wikipedia.travel.destinationpicker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import org.wikipedia.R;
import org.wikipedia.concurrency.CallbackTask;
import org.wikipedia.travel.database.TripDbHelper;
import org.wikipedia.travel.datepicker.DateActivity;
import org.wikipedia.travel.trip.Trip;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.wikipedia.activity.FragmentUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by abhandal on 3/3/2018.
 */

public class DestinationFragment extends Fragment {

    private DestinationAdapter destinationAdapter;

    private List<Trip> userDestinationList = new ArrayList<>();

    private Unbinder unbinder;
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    @BindView(R.id.destination_text_view) TextView tvDestination;
    Place destination;

    @BindView(R.id.destination_button_next) FloatingActionButton nextButton;
    @BindView(R.id.destination_list_view) RecyclerView destinationList;
  
    public interface Callback{
        void onPlaceSelected(Place place);
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

        nextButton.setOnClickListener(this);

        updateUserDestinationList();
        destinationAdapter = new DestinationAdapter(getContext());
        destinationList.setAdapter(destinationAdapter);
        destinationList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Adds event listener for when user swipes destination history left
        onSwipeLeft();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.fragment_place_autocomplete);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                setDestinationArray((String) place.getName(), (String) place.getAddress());
                Date currentTime = Calendar.getInstance().getTime();
                TripDbHelper tripHelper = TripDbHelper.instance();
                tripHelper.createList(getRandomTripName(), new Trip.Destination((String) place.getName()), currentTime);
                updateUserDestinationList();
              
        autocompleteFragment = (SupportPlaceAutocompleteFragment)getChildFragmentManager().findFragmentById(R.id.fragment_place_autocomplete);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                destination = place;
                updateDestinationText((String)place.getName());
                if(getCallback() != null) {
                    getCallback().onPlaceSelected(place);
                }
            }

            @Override
            public void onError(Status status) {
                Log.i("Autocomplete Failed", status.getStatusMessage());
            }
        });
        //There is no need to always display the search bar result everytime the view is created
        //tvDestination.setText(getArguments().getString("DESTINATION"));
        return view;
    }

    //Temporary measure to add mock trips, to be deleted once full functionality is complete
    protected String getRandomTripName() {
        String validCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder tripName = new StringBuilder();
        Random rnd = new Random();
        while (tripName.length() < 12) {
            int index = (int) (rnd.nextFloat() * validCharacters.length());
            tripName.append(validCharacters.charAt(index));
        }
        return tripName.toString();

    }

    // Method is called to handle when a user swipes saved destination to the Left
    private void onSwipeLeft() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swiped

                // Removes destination from list when user swipes left
                if (direction == ItemTouchHelper.LEFT) {
                    TripDbHelper tripHelper = TripDbHelper.instance();
                    tripHelper.deleteList(userDestinationList.get(position));
                    Toast.makeText(getContext(), userDestinationList.get(position).getDestination().getDestinationName() + " has been removed", Toast.LENGTH_SHORT).show();
                    updateUserDestinationList();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(destinationList); //set swipe to destinationList
    }

    // Handles event when user taps a saved destination city
    protected void goToDateActivity(View v) {
        onClick(v);
    }

    // Goes to next activity which is Date Picker
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), DateActivity.class);
        startActivity(i);
      
    private void updateDestinationText(String destination) {
        tvDestination.setText(destination);
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    public Callback getCallback() {
        return FragmentUtil.getCallback(this, Callback.class);
    }

    // Updates the destination arrayList
    private void updateUserDestinationList() {
        CallbackTask.execute(() -> TripDbHelper.instance().getDestinationList(),  new CallbackTask.DefaultCallback<List<Trip>>(){
            @Override
            public void success(List<Trip> list) {
                if (getActivity() == null) {
                    return;
                }
                userDestinationList = list;
                destinationAdapter.notifyDataSetChanged();
            }
        });
    }

    //Adapter for the RecyclerView
    public final class DestinationAdapter extends RecyclerView.Adapter<TripItemHolder> {
        private Context context;

        public DestinationAdapter(Context context) {
            this.context = context;
        }

        @Override
        public TripItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_list, parent, false);
            return new TripItemHolder(v);
        }

        @Override
        public void onBindViewHolder(TripItemHolder holder, int position) {
            holder.bindItem(userDestinationList.get(position));
        }

        @Override
        public int getItemCount() {
            return userDestinationList.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        // Insert a new item to the RecyclerView on a predefined position, could be used in the future
        public void insert(int position, Trip data) {
            userDestinationList.add(position, data);
            notifyItemInserted(position);
        }

        // Remove a RecyclerView item containing a specified Data object, could be used in the future
        public void remove(int data) {
            int position = userDestinationList.indexOf(data);
            userDestinationList.remove(position);
            notifyItemRemoved(position);
        }

    }

    //Individual rows that hold information about a trip
    public final class TripItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RelativeLayout tripLayout;
        public TextView destinationName;
        public TextView tripDate;
        private int index;
        private String savedDestination;

        public TripItemHolder(View tripView) {
            super(tripView);
            tripLayout = (RelativeLayout) tripView.findViewById(R.id.trip_info);
            destinationName = (TextView) tripView.findViewById(R.id.trip_name_view_text);
            tripDate = (TextView) tripView.findViewById(R.id.trip_date_view_text);
            tripLayout.setOnClickListener(this);
        }

        // Handles event when user taps saved destination city
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position >= 0) {
                savedDestination = userDestinationList.get(position).getDestination().getDestinationName();
                setDestinationArray(savedDestination, savedDestination);
                goToDateActivity(v);
            }
        }

        // Adds the text to the destination layout
        public void bindItem(Trip trip) {
            destinationName.setText(trip.getDestination().getDestinationName());
            tripDate.setText(trip.getTripDepartureDate().toString());
        }
    }
      
}
