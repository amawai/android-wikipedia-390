package org.wikipedia.travel.trips;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.Date;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.wikipedia.R;

/**
 * Created by amawai on 02/03/18.
 */

public final class TripItemHolder extends RecyclerView.ViewHolder {
    public FrameLayout tripLayout;
    public TextView tripName;
    public TextView tripDate;
    private int index;

    public TripItemHolder(View tripView) {
        super(tripView);
        tripLayout = (FrameLayout) tripView.findViewById(R.id.trip_info);
        tripName = (TextView) tripView.findViewById(R.id.trip_item_name);
        tripDate = (TextView) tripView.findViewById(R.id.trip_item_date);
        Log.d("tripitemholder", "in constructor");
    }

    /*void bindItem(@NonNull Cursor cursor) {
        index = cursor.getPosition();
           /*
           IndexedTripEntry indexedEntry
                    = new IndexedTripEntry(TripEntry.DATABASE_TABLE.fromCursor(cursor), index);
            getView().setItem(indexedEntry);
            getView().setTitle(indexedEntry.getEntry().getTitle().getDisplayText());
            getView().setDescription(indexedEntry.getEntry().getTitle().getDescription());\
            getView().setSelected(selectedIndices.contains(indexedEntry.getIndex()));

            // Check the previous item, see if the times differ enough
            // If they do, display the section header.
            // Always do it this is the first item.
            String curTime = getDateString(indexedEntry.getEntry().getTimestamp());
            String prevTime = "";
            if (cursor.getPosition() != 0) {
                cursor.moveToPrevious();
                HistoryEntry prevEntry = HistoryEntry.DATABASE_TABLE.fromCursor(cursor);
                prevTime = getDateString(prevEntry.getTimestamp());
                cursor.moveToNext();
            }
            getView().setHeaderText(curTime.equals(prevTime) ? null : curTime);
    }*/

}