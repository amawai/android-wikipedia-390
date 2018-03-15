package org.wikipedia.database.column;

import android.database.Cursor;
import android.support.annotation.NonNull;

import org.wikipedia.travel.database.DeprecatedDateAdapter;

import java.util.Date;

/**
 * Created by Artem on 2018-03-14.
 */

public class DeprecatedDateAdapterColumn extends DateColumn {
    public DeprecatedDateAdapterColumn(@NonNull String tbl, @NonNull String name, @NonNull String type) {
        super(tbl, name, type);
    }

    @Override
    public Date val(@NonNull Cursor cursor) {
        return ((Date) new DeprecatedDateAdapter(getLong(cursor)));
    }
}
