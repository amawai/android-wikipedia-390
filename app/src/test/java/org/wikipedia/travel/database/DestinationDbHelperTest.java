package org.wikipedia.travel.database;

import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.wikipedia.travel.trip.Trip;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class DestinationDbHelperTest {
    SQLiteDatabase mockDb;
    DestinationDbHelper mockDbHelper;
    Trip.Destination mockDestination;

    //makes sure each test starts with an uninitialized singleton and your tests are no longer affecting each other.
    @Before
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = DestinationDbHelper.class.getDeclaredField("INSTANCE");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Before
    public void setUp() {
        mockDbHelper = mock(DestinationDbHelper.class);
        mockDestination = mock(Trip.Destination.class);
    }

    // Tests that DestinationDbHelper returns single instance and not null
    @Test
    public void getInstance() {
        assertTrue(DestinationDbHelper.getInstance() instanceof DestinationDbHelper);
        assertNotNull(DestinationDbHelper.getInstance());
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
        }).when(mockDbHelper).createList(mockDb, mockDestination);
        //Verify interaction with the mock has taken place
        assertNull(mockDbHelper.createList(mockDb, mockDestination));
    }
}