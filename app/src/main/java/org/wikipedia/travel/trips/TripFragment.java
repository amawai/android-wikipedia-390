package org.wikipedia.travel.trips;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.wikipedia.R;
import org.wikipedia.database.contract.PageHistoryContract;
import org.wikipedia.database.contract.TripContract;
import org.wikipedia.travel.Trip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by amawai on 28/02/18.
 */

public class TripFragment extends Fragment {
    private Unbinder unbinder;
    private FloatingActionButton planNewTrip;
    private TripAdapter tripAdapter;

    @BindView(R.id.trip_list) RecyclerView tripList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_trip_display, container, false);
        unbinder = ButterKnife.bind(this, view);

        List<Trip> data = fillWithMockData();

        tripAdapter = new TripAdapter(data, getContext());
        tripList.setAdapter(tripAdapter);
        tripList.setLayoutManager(new LinearLayoutManager(getContext()));
        getAppCompatActivity().getSupportActionBar().setTitle("Why u tripping");
        return view;
    }

    //Mock data for now until database access is complete
    public List<Trip> fillWithMockData() {
        List<Trip> data = new ArrayList<>();

        Date today = new Date();
        Trip.Destination destination = new Trip.Destination("Osaka");
        today.getTime();

        data.add(new Trip("I want vacation", destination, today));
        data.add(new Trip("Japan Adventure", destination, today));
        data.add(new Trip("Canada Exploration",destination, today ));
        data.add(new Trip("Mountain Trek", destination, today));
        data.add(new Trip("whatever", destination, today));


        return data;
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
