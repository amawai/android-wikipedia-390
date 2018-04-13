package org.wikipedia.travel.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import org.wikipedia.database.DatabaseTable;
import org.wikipedia.database.contract.UserLandmarkContract;
import org.wikipedia.travel.landmarkpicker.LandmarkCard;

/**
 * Created by Artem on 2018-04-12.
 */

public class UserLandmarkTable extends DatabaseTable <UserLandmark> {

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
        contentValues.put(UserLandmarkContract.Col.ID.getName(), row.getId());
        contentValues.put(UserLandmarkContract.Col.TITLE.getName(), row.getTitle());
        return contentValues;
    }

    @Override
    protected String[] getUnfilteredPrimaryKeySelectionArgs(@NonNull UserLandmark row) {
        return new String[] { String.valueOf(row.getId()) };
    }

    @Override
    protected int getDBVersionIntroducedAt() {
        return DB_VER_INTRODUCED;
    }
}
