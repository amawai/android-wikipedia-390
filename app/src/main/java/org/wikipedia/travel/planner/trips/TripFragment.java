package org.wikipedia.travel.planner.trips;

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
import org.wikipedia.travel.planner.MainPlannerFragment;

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
    private TripAdapter tripAdapter;

    @BindView(R.id.trip_list) RecyclerView tripList;
    @BindView(R.id.trip_plan_new) Button newTripButton;

    public static TripFragment newInstance(List<Trip> trips) {

        Bundle args = tripListToBundle(trips);

        TripFragment fragment = new TripFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static Bundle tripListToBundle(List<Trip> trips) {
        Bundle args = new Bundle();
        String[] titles = new String[trips.size()];
        String[] dates = new String[trips.size()];

        for(int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);
            titles[i] = trip.getTitle();
            dates[i] = trip.getTripDepartureDate().toString();
        }

        args.putStringArray("TITLE", titles);
        args.putStringArray("DATE", dates);

        return args;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_trip_display, container, false);
        unbinder = ButterKnife.bind(this, view);

        newTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPlannerFragment parent = (MainPlannerFragment) getParentFragment();
                parent.newTrip();
            }
        });

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

    public void updateUserTripList(List<Trip> trips) {
        tripAdapter.updateData(tripListToBundle(trips));
    }


    //Adapter for the RecyclerView
    public final class TripAdapter extends RecyclerView.Adapter<TripItemHolder> {
        private Context context;
        private String[] titles;
        private String[] dates;
        private int count;

        public TripAdapter(Context context) {
            this.context = context;
            updateData(getArguments());
        }

        @Override
        public TripItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_list, parent, false);
            return new TripItemHolder(v);
        }

        @Override
        public void onBindViewHolder(TripItemHolder holder, int position) {
            String title = titles[position];
            String date = dates[position];
            holder.bindItem(title, date);
        }

        public void updateData(Bundle args) {
            titles = args.getStringArray("TITLE");
            dates = args.getStringArray("DATE");
            count = titles.length;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return count;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

    }

    //Individual rows that hold information about a trip
    public final class TripItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RelativeLayout tripLayout;
        public TextView tripName;
        public TextView tripDate;

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
                MainPlannerFragment parent = (MainPlannerFragment) getParentFragment();
                parent.openTrip(position);
            }
        }

        public void bindItem(String title, String date) {
            tripName.setText(title);
            tripDate.setText(date);
        }

    }
}
