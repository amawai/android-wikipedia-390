package org.wikipedia.database.contract;

import android.net.Uri;

import org.wikipedia.database.DbUtil;
import org.wikipedia.database.column.DateColumn;
import org.wikipedia.database.column.DeprecatedDateAdapterColumn;
import org.wikipedia.database.column.DestinationColumn;
import org.wikipedia.database.column.IdColumn;
import org.wikipedia.database.column.StrColumn;

public interface DestinationContract {
    String TABLE = "destinations";
    Uri URI = Uri.withAppendedPath(AppContentProviderContract.AUTHORITY_BASE, "/destination");

    interface Col {
        IdColumn ID = new IdColumn(TABLE);
        DestinationColumn DESTINATION = new DestinationColumn(TABLE, "tripDestinationName", "text not null");

        String[] SELECTION = DbUtil.qualifiedNames(DESTINATION);
        String[] ALL = DbUtil.qualifiedNames(ID, DESTINATION);
    }
}
