package org.wikipedia.travel.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import org.wikipedia.database.DatabaseTable;
import org.wikipedia.database.column.Column;
import org.wikipedia.database.contract.TripContract;
import org.wikipedia.travel.trip.Trip;

import java.util.ArrayList;
import java.util.List;

//Used the format/methods for the ReadlistTable, ReadingListPageTable in order to properly integrate a new table into the application
//Only setup for a trip with a single destination
public class TripDatabaseTable extends DatabaseTable<Trip> {

    private static final int DB_VER_INTRODUCED = 19;

    public TripDatabaseTable() {
        super(TripContract.TABLE, TripContract.URI);
    }

    @Override public Trip fromCursor(@NonNull Cursor cursor) {
        Trip trip = new Trip(TripContract.Col.TITLE.val(cursor),
                TripContract.Col.DESTINATION.val(cursor),
                TripContract.Col.DATE.val(cursor));
        trip.setId(TripContract.Col.ID.val(cursor));
        return trip;
    }

    public Trip fromDestinationCursor(@NonNull Cursor cursor) {
        Trip trip = new Trip(TripContract.Col.DESTINATION.val(cursor), TripContract.Col.DATE.val(cursor));
        trip.setId(TripContract.Col.ID.val(cursor));
        return trip;
    }

    @NonNull @Override public Column<?>[] getColumnsAdded(int version) {
        switch (version) {
            case DB_VER_INTRODUCED:
                List<Column<?>> cols = new ArrayList<>();
                cols.add(TripContract.Col.ID);
                cols.add(TripContract.Col.TITLE);
                cols.add(TripContract.Col.DESTINATION);
                cols.add(TripContract.Col.DATE);
                return cols.toArray(new Column<?>[cols.size()]);
            default:
                return super.getColumnsAdded(version);
        }
    }

    @Override protected ContentValues toContentValues(@NonNull Trip row) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TripContract.Col.TITLE.getName(), row.getTitle());
        contentValues.put(TripContract.Col.DESTINATION.getName(), row.getDestination().getDestinationName());
        contentValues.put(TripContract.Col.DATE.getName(), row.getTripDepartureDate().getTime());
        return contentValues;
    }

    @Override protected String getPrimaryKeySelection(@NonNull Trip row,
                                                      @NonNull String[] selectionArgs) {
        return super.getPrimaryKeySelection(row, TripContract.Col.SELECTION);
    }

    @Override protected String[] getUnfilteredPrimaryKeySelectionArgs(@NonNull Trip row) {
        return new String[] {row.getTitle()};
    }

    @Override protected int getDBVersionIntroducedAt() {
        return DB_VER_INTRODUCED;
    }

}

