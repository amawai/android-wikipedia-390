package org.wikipedia.travel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.wikipedia.WikipediaApp;
import org.wikipedia.database.contract.TripContract;
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

    public void updateList(@NonNull Trip list) {
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
    }

    public void deleteList(@NonNull Trip list) {
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

    private SQLiteDatabase getReadableDatabase() {
        return WikipediaApp.getInstance().getDatabase().getReadableDatabase();
    }

    private SQLiteDatabase getWritableDatabase() {
        return WikipediaApp.getInstance().getDatabase().getWritableDatabase();
    }
}
