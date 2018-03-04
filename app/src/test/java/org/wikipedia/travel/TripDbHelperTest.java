package org.wikipedia.travel;

import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.robolectric.RobolectricTestRunner;
import org.wikipedia.travel.database.Trip;
import org.wikipedia.travel.database.TripDbHelper;

import java.util.Date;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

//These tests will be likely be further expanded once the database is finalized.
@RunWith(RobolectricTestRunner.class)
public class TripDbHelperTest {
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
    public void mockTestCreateList() {
        //Stub the create list method
        PowerMockito.doAnswer((i) -> {
            return null;
        }).when(mockDbHelper).createList(mockDb, "Trip of a lifetime", new Trip.Destination("New Zealand"), new Date());
        //Verify interaction with the mock has taken place
        assertNull(mockDbHelper.createList(mockDb, "Trip of a lifetime", new Trip.Destination("New Zealand"), new Date()));
    }
}