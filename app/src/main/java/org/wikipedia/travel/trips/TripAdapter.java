package org.wikipedia.travel.trips;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wikipedia.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by amawai on 02/03/18.
 */

public final class TripAdapter extends RecyclerView.Adapter<TripItemHolder> {
    List<Trip> list = Collections.emptyList();
    Context context;

    public TripAdapter(List<Trip> list, Context context) {
        this.list = list;
        this.context = context;
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
        holder.tripName.setText(list.get(position).tripName);
        holder.tripDate.setText(list.get(position).date);
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