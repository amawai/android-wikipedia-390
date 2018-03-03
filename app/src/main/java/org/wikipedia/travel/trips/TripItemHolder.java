package org.wikipedia.travel.trips;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.Date;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.wikipedia.R;

/**
 * Created by amawai on 02/03/18.
 */

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
        Log.d("tripitemholder", "in constructor");
    }
}