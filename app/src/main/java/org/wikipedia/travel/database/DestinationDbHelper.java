package org.wikipedia.travel.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import org.wikipedia.WikipediaApp;
import org.wikipedia.database.contract.DestinationContract;
import org.wikipedia.travel.trip.Trip;
import org.wikipedia.travel.trip.Trip.Destination;
import org.wikipedia.util.log.L;

import java.util.ArrayList;
import java.util.List;

public class DestinationDbHelper {

    private static DestinationDbHelper INSTANCE;

    public static DestinationDbHelper getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new DestinationDbHelper();
        }
        return INSTANCE;
    }

    public List<Trip> getAllLists() {
        List<Trip> lists = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(DestinationContract.TABLE, null, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                Trip list = Trip.DESTINATION_DATABASE_TABLE.fromCursor(cursor);
                lists.add(list);
            }
        }
        return lists;
    }

    @NonNull
    public Trip createList(@NonNull Destination destination) {
        SQLiteDatabase db = getWritableDatabase();
        return createList(db, destination);
    }

    private Trip createList(SQLiteDatabase db, Destination destination) {
        db.beginTransaction();
        try {
            Trip protoList = new Trip(destination);
            long id = db.insertOrThrow(DestinationContract.TABLE, null,
                    Trip.DATABASE_TABLE.toContentValues(protoList));
            db.setTransactionSuccessful();
            protoList.setId(id);
            return protoList;
        } finally {
            db.endTransaction();
        }
    }

    public Object deleteList(@NonNull Trip list) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            int result = db.delete(DestinationContract.TABLE,
                    DestinationContract.Col.ID.getName() + " = ?", new String[]{Long.toString(list.getId())});
            if (result != 1) {
                L.w("Failed to delete db entry for list " + list.getDestination().getDestinationName());
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return null;
    }

    public Object updateList(@NonNull Trip list) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            int result = db.update(DestinationContract.TABLE, Trip.DATABASE_TABLE.toContentValues(list),
                    DestinationContract.Col.ID.getName() + " = ?", new String[]{Long.toString(list.getId())});
            if (result != 1) {
                L.w("Failed to update db entry for list " + list.getDestination().getDestinationName());
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return null;
    }


    private SQLiteDatabase getReadableDatabase() {
        return WikipediaApp.getInstance().getDatabase().getReadableDatabase();
    }

    private SQLiteDatabase getWritableDatabase() {
        return WikipediaApp.getInstance().getDatabase().getWritableDatabase();
    }

}
