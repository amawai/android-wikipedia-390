package org.wikipedia.travel.database;

import android.support.annotation.NonNull;

import org.wikipedia.travel.trip.Trip;

import java.util.Date;
import java.util.List;

/**
 * Created by aman_ on 3/16/2018.
 * To be used for testing database functions
 */

public interface ITripDbHelper {
    public Object deleteList(@NonNull Trip list);
}
