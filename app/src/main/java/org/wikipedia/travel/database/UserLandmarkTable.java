package org.wikipedia.travel.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import org.wikipedia.database.DatabaseTable;
import org.wikipedia.database.column.Column;
import org.wikipedia.database.contract.UserLandmarkContract;

import java.util.ArrayList;

/**
 * Created by Artem on 2018-04-12.
 */

public class UserLandmarkTable extends DatabaseTable<UserLandmark> {

    private static final int DB_VER_INTRODUCED = 22;

    public UserLandmarkTable() {
        super(UserLandmarkContract.TABLE, UserLandmarkContract.URI);
    }

    @Override
    public UserLandmark fromCursor(Cursor cursor) {
        UserLandmark landmark = new UserLandmark(UserLandmarkContract.Col.TITLE.val(cursor));
        landmark.setId(UserLandmarkContract.Col.ID.val(cursor));
        return landmark;
    }

    @Override
    protected ContentValues toContentValues(UserLandmark row) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserLandmarkContract.Col.TITLE.getName(), row.getTitle());
        return contentValues;
    }

    @NonNull
    @Override
    public Column<?>[] getColumnsAdded(int version) {
        switch(version) {
            case DB_VER_INTRODUCED:
                ArrayList<Column<?>> cols = new ArrayList<>();
                cols.add(UserLandmarkContract.Col.ID);
                cols.add(UserLandmarkContract.Col.TITLE);
                cols.add(UserLandmarkContract.Col.TRIPID);
                return cols.toArray(new Column<?>[cols.size()]);
            default:
                return super.getColumnsAdded(version);
        }
    }

    @Override
    protected String[] getUnfilteredPrimaryKeySelectionArgs(@NonNull UserLandmark row) {
        return new String[] {
                String.valueOf(row.getId())
        };
    }

    @Override
    protected int getDBVersionIntroducedAt() {
        return DB_VER_INTRODUCED;
    }
}
