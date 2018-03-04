package org.wikipedia.travel.trips;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import org.wikipedia.R;
import org.wikipedia.database.contract.PageHistoryContract;
import org.wikipedia.database.contract.TripContract;
import org.wikipedia.travel.database.Trip;
import org.wikipedia.travel.database.TripDbHelper;

import java.util.ArrayList;
import java.util.Collections;
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

    @BindView(R.id.trip_list) RecyclerView tripList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_trip_display, container, false);
        unbinder = ButterKnife.bind(this, view);

        planNewTrip = (Button) view.findViewById(R.id.trip_plan_new);
        planNewTrip.setOnClickListener(this);

        List<Trip> data = TripDbHelper.instance().getAllLists();

        tripAdapter = new TripAdapter(data, getContext());
        tripList.setAdapter(tripAdapter);
        tripList.setLayoutManager(new LinearLayoutManager(getContext()));
        getAppCompatActivity().getSupportActionBar().setTitle("Why u tripping");
        return view;
    }

    @Override
    public void onClick(View v) {
        //TODO: Implement functionality of trip creation
        TripDbHelper tripHelper = TripDbHelper.instance();
        //Make something that checks uniqueness of names
        tripHelper.createList(getRandomTripName(), new Trip.Destination("Osaka"), new Date());
        List<Trip> savedTrips = tripHelper.getAllLists();
    }

    //Temporary measure to add mock trips, to be deleted once full functionality is complete
    protected String getRandomTripName() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 12) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }
    
    //Adapter for the RecyclerView
    public final class TripAdapter extends RecyclerView.Adapter<TripItemHolder> {
        private List<Trip> list = Collections.emptyList();
        private Context context;
        private Cursor mCursor;

        public TripAdapter(List<Trip> list, Context context) {
            this.list = list;
            this.context = context;
        }

        public void setCursor(Cursor cursor) {
            mCursor = cursor;
        }
        @Override
        public TripItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_list, parent, false);
            TripItemHolder holder = new TripItemHolder(v);
            return holder;

        }

        @Override
        public void onBindViewHolder(TripItemHolder holder, int position) {
            //Use the provided TripItemHolder on the onCreateViewHolder method to populate the trip row on the RecyclerView
            holder.tripName.setText(list.get(position).getTitle());
            holder.tripDate.setText(list.get(position).getTripDepartureDate().toString());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        // Insert a new item to the RecyclerView on a predefined position
        public void insert(int position, Trip data) {
            list.add(position, data);
            notifyItemInserted(position);
        }

        // Remove a RecyclerView item containing a specified Data object
        public void remove(Trip data) {
            int position = list.indexOf(data);
            list.remove(position);
            notifyItemRemoved(position);
        }

    }

    public final class TripItemHolder extends RecyclerView.ViewHolder {
        public RelativeLayout tripLayout;
        public TextView tripName;
        public TextView tripDate;
        private int index;

        public TripItemHolder(View tripView) {
            super(tripView);
            tripLayout = (RelativeLayout) tripView.findViewById(R.id.trip_info);
            tripName = (TextView) tripView.findViewById(R.id.trip_item_name);
            tripDate = (TextView) tripView.findViewById(R.id.trip_item_date);
        }

    }
}
