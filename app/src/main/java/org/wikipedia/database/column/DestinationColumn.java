package org.wikipedia.database.column;

import android.database.Cursor;
import android.support.annotation.NonNull;

import org.wikipedia.travel.database.Trip;

//Set up a new column for destinations for storage in the TripDatabase Table
public class DestinationColumn extends Column<Trip.Destination> {
    public DestinationColumn(@NonNull String tbl, @NonNull String name, @NonNull String type) {
        super(tbl, name, type);
    }

    @Override
    public Trip.Destination val(@NonNull Cursor cursor) {
        //Should be noted again here that this Destination object is being constructed solely based on the name of the destination location
        //and not the list of landmarks to visit. This is merely to simplify the storage of the destination for a particular user's
        //trip
        return new Trip.Destination(getString(cursor));
    }
}
