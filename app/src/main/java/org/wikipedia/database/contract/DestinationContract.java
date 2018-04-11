package org.wikipedia.database.contract;

import android.net.Uri;

import org.wikipedia.database.DbUtil;
import org.wikipedia.database.column.DestinationColumn;
import org.wikipedia.database.column.IdColumn;

@SuppressWarnings("checkstyle:interfaceistype")
public interface DestinationContract {
    String TABLE = "destination";
    Uri URI = Uri.withAppendedPath(AppContentProviderContract.AUTHORITY_BASE, "/travel");

    interface Col {
        IdColumn ID = new IdColumn(TABLE);
        DestinationColumn DESTINATION = new DestinationColumn(TABLE, "tripDestinationName", "text not null");

        String[] SELECTION = DbUtil.qualifiedNames(DESTINATION);
        String[] ALL = DbUtil.qualifiedNames(ID, DESTINATION);
    }
}
