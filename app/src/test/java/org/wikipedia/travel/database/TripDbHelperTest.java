package org.wikipedia.travel.database;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.robolectric.RobolectricTestRunner;
import org.wikipedia.travel.trip.Trip;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//These tests will be likely be further expanded once the database is finalized.
@RunWith(RobolectricTestRunner.class)
public class TripDbHelperTest implements ITripDbHelper {

    SQLiteDatabase mockDb;
    TripDbHelper mockDbHelper;

    @Before
    public void setUp() {
        mockDbHelper = mock(TripDbHelper.class);
    }

    @Test
    public void mockTestGetAllLists() {
        mockDbHelper.getAllLists();
        assertEquals(0, mockDbHelper.getAllLists().size());
    }

    @Test
    public void mockTestGetDestinationLists() {
        mockDbHelper.getDestinationList();
        assertEquals(0, mockDbHelper.getDestinationList().size());
    }

    @Test
    public void mockTestDeleteLists() {
        Trip mockTrip = mock(Trip.class);
        deleteList(mockTrip);
        when(mockTrip.getId()).thenReturn((long)1);
        verify(mockTrip).getId();
    }

    @Test
    public void mockTestCreateList() {
        //Stub the create list method
        PowerMockito.doAnswer((i) -> {
            return null;
        }).when(mockDbHelper).createList(mockDb, "Trip of a lifetime", new Trip.Destination("New Zealand"), new Date());
        //Verify interaction with the mock has taken place
        assertNull(mockDbHelper.createList(mockDb, "Trip of a lifetime", new Trip.Destination("New Zealand"), new Date()));
    }

    @Override
    public void deleteList(@NonNull Trip list) {
        List<Trip> arrList = new ArrayList<>();
        arrList.add(list);

        arrList.remove(list.getId());
    }
}