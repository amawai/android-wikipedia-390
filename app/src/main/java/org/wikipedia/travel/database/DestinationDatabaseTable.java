package org.wikipedia.travel.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import org.wikipedia.database.DatabaseTable;
import org.wikipedia.database.column.Column;
import org.wikipedia.database.contract.DestinationContract;
import org.wikipedia.travel.trip.Trip;

import java.util.ArrayList;
import java.util.List;

public class DestinationDatabaseTable extends DatabaseTable<Trip> {

    private static final int DB_VER_INTRODUCED = 21;

    public DestinationDatabaseTable() {
        super(DestinationContract.TABLE, DestinationContract.URI);
    }

    @Override
    public Trip fromCursor(Cursor cursor) {
        Trip trip = new Trip(DestinationContract.Col.DESTINATION.val(cursor));
        trip.setId(DestinationContract.Col.ID.val(cursor));
        return trip;
    }

    @NonNull @Override public Column<?>[] getColumnsAdded(int version) {
        switch (version) {
            case DB_VER_INTRODUCED:
                List<Column<?>> cols = new ArrayList<>();
                cols.add(DestinationContract.Col.ID);
                cols.add(DestinationContract.Col.DESTINATION);
                return cols.toArray(new Column<?>[cols.size()]);
            default:
                return super.getColumnsAdded(version);
        }
    }

    @Override
    protected ContentValues toContentValues(Trip row) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DestinationContract.Col.DESTINATION.getName(), row.getDestination().getDestinationName());

        return contentValues;
    }

    @Override protected String getPrimaryKeySelection(@NonNull Trip row,
                                                      @NonNull String[] selectionArgs) {
        return super.getPrimaryKeySelection(row, DestinationContract.Col.SELECTION);
    }

    @Override protected String[] getUnfilteredPrimaryKeySelectionArgs(@NonNull Trip row) {
        return new String[] {row.getDestination().getDestinationName()};
    }

    @Override protected int getDBVersionIntroducedAt() {
        return DB_VER_INTRODUCED;
    }
}
