package org.wikipedia.database.column;

import android.database.Cursor;
import android.support.annotation.NonNull;

import org.wikipedia.travel.Trip;

//Set up a new column for destinations for storage in the TripDatabase Table
public class DestinationColumn extends Column<Trip.Destination> {
    public DestinationColumn(@NonNull String tbl, @NonNull String name, @NonNull String type) {
        super(tbl, name, type);
    }

    @Override
    public Trip.Destination val(@NonNull Cursor cursor) {
        return new Trip.Destination(getString(cursor));
    }
}
