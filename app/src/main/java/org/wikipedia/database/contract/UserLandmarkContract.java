package org.wikipedia.database.contract;

import android.net.Uri;

import org.wikipedia.database.column.IdColumn;
import org.wikipedia.database.column.LongColumn;
import org.wikipedia.database.column.StrColumn;

/**
 * Created by Artem on 2018-04-12.
 */

public interface UserLandmarkContract {
    String TABLE = "usertriplandmarks";
    Uri URI = Uri.withAppendedPath(AppContentProviderContract.AUTHORITY_BASE, "/localtriplandmark");

    interface Col {
        IdColumn ID = new IdColumn(TABLE);
        StrColumn TITLE = new StrColumn(TABLE, "landmarktitle", "text");
        LongColumn TRIPID = new LongColumn(TABLE, "tripid", "integer not null");
    }
}
