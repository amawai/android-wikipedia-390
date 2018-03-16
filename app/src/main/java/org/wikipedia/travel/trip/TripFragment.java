package org.wikipedia.travel.trip;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.wikipedia.R;

import org.wikipedia.concurrency.CallbackTask;
import org.wikipedia.travel.database.TripDbHelper;
import org.wikipedia.util.FeedbackUtil;

import org.wikipedia.activity.FragmentUtil;
import org.wikipedia.util.DateUtil;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;
import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

/**
 * Created by amawai on 28/02/18.
 */

public class TripFragment extends Fragment{

    private Unbinder unbinder;

    private TripAdapter tripAdapter;

    private List<Trip> userTripsList = new ArrayList<>();

    @BindView(R.id.trip_list_view_recycler) RecyclerView tripList;
    @BindView(R.id.trip_button_new) Button planNewTrip;

    public static TripFragment newInstance() {
        TripFragment fragment = new TripFragment();
        return fragment;
    }

    /*
        Callbacks
     */
    public interface Callback{
        void onNewTrip();
        void onOpenTrip(long id);
        void onRequestTripListUpdate();
        void onDeleteTrip(long id);
    }

    public Callback getCallback() {
        return FragmentUtil.getCallback(this, Callback.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_travel_trip, container, false);
        unbinder = ButterKnife.bind(this, view);

        planNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCallback().onNewTrip();
            }
        });
        updateUserTripList();
        tripAdapter = new TripAdapter(getContext());
        tripList.setAdapter(tripAdapter);

        tripList.setLayoutManager(new LinearLayoutManager(getContext()));
        getAppCompatActivity().getSupportActionBar().setTitle("Trip Planner");
        return view;
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
        if(getCallback() != null) {
            getCallback().onRequestTripListUpdate();
        }
    }

    public void setUserTripList(List<Trip> trips) {
        if(tripAdapter != null) {
            tripAdapter.setUserTrips(trips);
        }
    }


    //Adapter for the RecyclerView
    public final class TripAdapter extends RecyclerView.Adapter<TripItemHolder> {
        private Context context;
        private List<Trip> userTripsList;

        public TripAdapter(Context context) {
            this.context = context;
            this.userTripsList = new ArrayList<>();
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

        public void setUserTrips(List<Trip> trips) {
            Log.d("SetUserTrips", trips.size() + "");
            this.userTripsList = trips;
            notifyDataSetChanged();
        }
    }

    //Individual rows that hold information about a trip
    public final class TripItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int index;
        private long id;

        @BindView(R.id.trip_info) RelativeLayout tripLayout;
        @BindView(R.id.trip_name_view_text) TextView tripName;
        @BindView(R.id.trip_date_view_text) TextView tripDate;
        @BindView(R.id.trip_item_edit) ImageView tripEdit;
        @BindView(R.id.trip_item_delete) ImageView tripDelete;

        public TripItemHolder(View tripView) {
            super(tripView);

            unbinder = ButterKnife.bind(this, tripView);
            tripName.setOnClickListener(this);
            tripDate.setOnClickListener(this);
            tripEdit.setOnClickListener(this);
            tripDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position >= 0) {
                switch (v.getId()){
                    case (R.id.trip_name_view_text): case (R.id.trip_date_view_text):
                        Toast.makeText(getContext(), "You selected the trip " + userTripsList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.trip_item_edit:
                        if (getCallback() != null) {
                            getCallback().onOpenTrip(id);
                        }
                        Toast.makeText(getContext(), "Edit the trip " + userTripsList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.trip_item_delete:
                        if(getCallback()!=null) {
                            getCallback().onDeleteTrip(id);
                        }
                        Toast.makeText(getContext(), "Delete the trip " + userTripsList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        FeedbackUtil.showMessage(getActivity(), "Error");
                }

            }
        }

        public void bindItem(Trip trip) {
            Date departureDate = trip.getTripDepartureDate();
            String departureText = DateUtil.getDateWithWeekday(departureDate);
            tripName.setText(trip.getDestination().getDestinationName());
            tripDate.setText(departureText);
            id = trip.getId();
        }
    }
}
