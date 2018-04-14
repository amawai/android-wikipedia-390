package org.wikipedia.travel.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.wikipedia.WikipediaApp;
import org.wikipedia.database.contract.TripContract;
import org.wikipedia.database.contract.UserLandmarkContract;
import org.wikipedia.travel.landmarkpicker.LandmarkCard;
import org.wikipedia.travel.trip.Trip;
import org.wikipedia.util.log.L;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//As with the database table, use the ReadingListDbHelper as a basis in constructing this class
public class TripDbHelper {

    private static TripDbHelper INSTANCE;

    public static TripDbHelper instance() {
        if (INSTANCE == null) {
            INSTANCE = new TripDbHelper();
        }
        return INSTANCE;
    }

    public List<Trip> getAllLists() {
        List<Trip> lists = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(TripContract.TABLE, null, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                Trip list = Trip.DATABASE_TABLE.fromCursor(cursor);
                lists.add(list);
            }
        }
        return lists;
    }

    @NonNull
    public Trip createList(@NonNull String title, @NonNull Trip.Destination destination, @NonNull Date date) {
        SQLiteDatabase db = getWritableDatabase();
        return createList(db, title, destination, date);
    }

    @NonNull
    public Trip createList() {
        Trip trip = new Trip();
        return this.createList(trip.getTitle(), trip.getDestination(), trip.getTripDepartureDate());
    }

    @NonNull
    public Trip createList(@NonNull SQLiteDatabase db, @NonNull String title, @NonNull Trip.Destination destination, @NonNull Date date) {
        db.beginTransaction();
        try {
            Trip protoList = new Trip(title, destination, date);
            long id = db.insertOrThrow(TripContract.TABLE, null,
                    Trip.DATABASE_TABLE.toContentValues(protoList));
            db.setTransactionSuccessful();
            protoList.setId(id);
            return protoList;
        } finally {
            db.endTransaction();
        }
    }

    public Object updateList(@NonNull Trip list) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            int result = db.update(TripContract.TABLE, Trip.DATABASE_TABLE.toContentValues(list),
                    TripContract.Col.ID.getName() + " = ?", new String[]{Long.toString(list.getId())});
            if (result != 1) {
                L.w("Failed to update db entry for list " + list.getTitle());
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return null;
    }

    public Object deleteList(@NonNull Trip list) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            int result = db.delete(TripContract.TABLE,
                    TripContract.Col.ID.getName() + " = ?", new String[]{Long.toString(list.getId())});
            if (result != 1) {
                L.w("Failed to delete db entry for list " + list.getTitle());
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return null;
    }

    @Nullable
    public Trip getFullListById(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Trip list = null;
        try (Cursor cursor = db.query(TripContract.TABLE, null,
                TripContract.Col.ID.getName() + " = ?", new String[]{Long.toString(id)},
                null, null, null)) {
            if (cursor.moveToFirst()) {
                list = Trip.DATABASE_TABLE.fromCursor(cursor);
            }
        }
        if (list == null) {
            return null;
        }
        return list;
    }

    public Void addUserLandmarks(long tripId, List<LandmarkCard> landmarks) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {
            for (LandmarkCard lm: landmarks) {
                // Extract just the relevant fields
                UserLandmark toSave = new UserLandmark(lm.getTitle());
                ContentValues content = UserLandmark.DATABASE_TABLE.toContentValues(toSave);
                content.put(UserLandmarkContract.Col.TRIPID.getName(), tripId);
                db.insertOrThrow(UserLandmarkContract.TABLE, null, content);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return null;
    }

    public Void deleteUserLandmarks(long tripId, List<LandmarkCard> landmarks) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (LandmarkCard lm: landmarks) {

                db.delete(UserLandmarkContract.TABLE,
                        UserLandmarkContract.Col.TITLE.getName() + " = ? AND "
                                + UserLandmarkContract.Col.TRIPID.getName() + " = ?",
                        new String[]{lm.getTitle(), Long.toString(tripId)});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return null;
    }

    public List<UserLandmark> loadUserLandmarks(long tripId) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<UserLandmark> result = new ArrayList<>();

        try (Cursor cursor = db.query(UserLandmarkContract.TABLE, null, UserLandmarkContract.Col.TRIPID.getName() + " = ?", new String[]{Long.toString(tripId)}, null, null, null)) {
            while (cursor.moveToNext()) {
                UserLandmark landmark = UserLandmark.DATABASE_TABLE.fromCursor(cursor);
                result.add(landmark);
            }
        }
        return result;
    }

    private SQLiteDatabase getReadableDatabase() {
        return WikipediaApp.getInstance().getDatabase().getReadableDatabase();
    }

    private SQLiteDatabase getWritableDatabase() {
        return WikipediaApp.getInstance().getDatabase().getWritableDatabase();
    }

}
