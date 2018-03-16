package org.wikipedia.travel.destinationpicker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by abhandal on 3/3/2018.
 */

public class DestinationFragment extends Fragment implements View.OnClickListener {

    private DestinationAdapter destinationAdapter;

    private List<Trip> userDestinationList = new ArrayList<>();

    private Unbinder unbinder;
    private static String[] destinationString;

    @BindView(R.id.destination_button_next) FloatingActionButton nextButton;
    @BindView(R.id.destination_list_view) RecyclerView destinationList;

    //The method will assemble the destinationFragment and invoke the Google Place Autocomplete widget
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_travel_destination_picker, container, false);
        unbinder = ButterKnife.bind(this, view);
        getAppCompatActivity().getSupportActionBar().setTitle(getString(R.string.view_travel_card_title));

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
            }

            @Override
            public void onError(Status status) {
                Log.i("Autocomplete Failed", status.getStatusMessage());
            }
        });
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
                    userDestinationList.remove(position);
                    destinationAdapter.notifyDataSetChanged();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(destinationList); //set swipe to destinationList
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

        public TripItemHolder(View tripView) {
            super(tripView);
            tripLayout = (RelativeLayout) tripView.findViewById(R.id.trip_info);
            destinationName = (TextView) tripView.findViewById(R.id.trip_name_view_text);
            tripLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position >= 0) {
                Toast.makeText(getContext(), "You selected the trip " + userDestinationList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        }

        public void bindItem(Trip trip) {
            destinationName.setText(trip.getDestination().getDestinationName());
        }
    }

}
