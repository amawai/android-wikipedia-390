package org.wikipedia.database.contract;

import android.net.Uri;

import org.wikipedia.database.DbUtil;
import org.wikipedia.database.column.DateColumn;
import org.wikipedia.database.column.DeprecatedDateAdapterColumn;
import org.wikipedia.database.column.DestinationColumn;
import org.wikipedia.database.column.IdColumn;
import org.wikipedia.database.column.StrColumn;

//Adopts a similar style to the contracts contained within this package, specific to trips
@SuppressWarnings("checkstyle:interfaceistype")
public interface TripContract {
    String TABLE = "trips";
    Uri URI = Uri.withAppendedPath(AppContentProviderContract.AUTHORITY_BASE, "/trip");

    interface Col {
        IdColumn ID = new IdColumn(TABLE);
        StrColumn TITLE = new StrColumn(TABLE, "tripTitle", "text not null");
        //Will only account for one destination for the trip
        DestinationColumn DESTINATION = new DestinationColumn(TABLE, "tripDestinationName", "text not null");
        DateColumn DATE = new DeprecatedDateAdapterColumn(TABLE, "tripDepartureDate", "text not null");

        String[] SELECTION = DbUtil.qualifiedNames(TITLE);
        String[] ALL = DbUtil.qualifiedNames(ID, TITLE, DESTINATION, DATE);
    }
}

