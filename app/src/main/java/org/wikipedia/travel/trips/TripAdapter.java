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
    private Cursor cursor;
    Context mContext;
    List<String> list = Collections.emptyList();

    public TripAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    @Override
    public TripItemHolder onCreateViewHolder(ViewGroup parent, int type) {
        Log.d("tripitemholder", "in oncreateviewholder");
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_trip_list, parent, false);
        return new TripItemHolder(view);
    }

    @Override
    public void onBindViewHolder(TripItemHolder holder, int pos) {
        Log.d("tripitemholder", "in onbindviewholder");
        /*if (cursor == null) {
            return;
        }
        cursor.moveToPosition(pos);
        holder.bindItem(cursor);
        */
        //lmao im retarded
        holder.tripName.setText("TRIP NAME");
        holder.tripDate.setText("some date heyhey");
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}