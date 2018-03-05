package org.wikipedia.travel.trips;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.wikipedia.R;
import org.wikipedia.concurrency.CallbackTask;
import org.wikipedia.travel.database.Trip;
import org.wikipedia.travel.database.TripDbHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by amawai on 28/02/18.
 */

public class TripFragment extends Fragment implements View.OnClickListener {
    private Unbinder unbinder;
    private Button planNewTrip;
    private TripAdapter tripAdapter;

    private List<Trip> userTripsList = new ArrayList<>();

    @BindView(R.id.trip_list) RecyclerView tripList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_trip_display, container, false);
        unbinder = ButterKnife.bind(this, view);

        planNewTrip = (Button) view.findViewById(R.id.trip_plan_new);
        planNewTrip.setOnClickListener(this);

        updateUserTripList();
        tripAdapter = new TripAdapter(getContext());
        tripList.setAdapter(tripAdapter);
        tripList.setLayoutManager(new LinearLayoutManager(getContext()));
        getAppCompatActivity().getSupportActionBar().setTitle("Trip Planner");
        return view;
    }


    @Override
    public void onClick(View v) {
        //TODO: Implement functionality of trip creation
        //For now, this creates a random trip and updates the list accordingly
        TripDbHelper tripHelper = TripDbHelper.instance();
        tripHelper.createList(getRandomTripName(), new Trip.Destination("Osaka"), new Date());
        updateUserTripList();
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

    @Override
    public void onResume() {
        super.onResume();
        updateUserTripList();
    }

    @Override
    public void onDestroyView() {
        tripList.setAdapter(null);
        unbinder.unbind();
        unbinder = null;
        super.onDestroyView();
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    private void updateUserTripList() {
        CallbackTask.execute(() -> TripDbHelper.instance().getAllLists(),  new CallbackTask.DefaultCallback<List<Trip>>(){
            @Override
            public void success(List<Trip> list) {
                if (getActivity() == null) {
                    return;
                }
                userTripsList = list;
                tripAdapter.notifyDataSetChanged();
            }
        });
    }

    //Adapter for the RecyclerView
    public final class TripAdapter extends RecyclerView.Adapter<TripItemHolder> {
        private Context context;

        public TripAdapter(Context context) {
            this.context = context;
        }

        @Override
        public TripItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_list, parent, false);
            return new TripItemHolder(v);
        }

        @Override
        public void onBindViewHolder(TripItemHolder holder, int position) {
            holder.bindItem(userTripsList.get(position));
        }

        @Override
        public int getItemCount() {
            return userTripsList.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        // Insert a new item to the RecyclerView on a predefined position, could be used in the future
        public void insert(int position, Trip data) {
            userTripsList.add(position, data);
            notifyItemInserted(position);
        }

        // Remove a RecyclerView item containing a specified Data object, could be used in the future
        public void remove(Trip data) {
            int position = userTripsList.indexOf(data);
            userTripsList.remove(position);
            notifyItemRemoved(position);
        }

    }

    //Individual rows that hold information about a trip
    public final class TripItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RelativeLayout tripLayout;
        public TextView tripName;
        public TextView tripDate;
        private int index;

        public TripItemHolder(View tripView) {
            super(tripView);
            tripLayout = (RelativeLayout) tripView.findViewById(R.id.trip_info);
            tripName = (TextView) tripView.findViewById(R.id.trip_item_name);
            tripDate = (TextView) tripView.findViewById(R.id.trip_item_date);

            tripLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position >= 0) {
                Toast.makeText(getContext(), "You selected the trip " + userTripsList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        }

        public void bindItem(Trip trip) {
            tripName.setText(trip.getTitle());
            tripDate.setText(trip.getTripDepartureDate().toString());
        }

    }
}
