package org.wikipedia.travel.database;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.wikipedia.travel.trip.Trip;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

//Utilizes a similar to approach to @abhandal's DestinationDbHelperTest
public class TripWithLandmarkDbHelperTest {
    private List<Trip> testList = TripDbHelper.instance().getAllLists();
    private List<Trip> emptyList = new ArrayList<>(0);
    private Trip testTrip = new Trip();
    private Trip nullTrip = null;
    private long predictedSize;
    private long actualSize;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    // Tests creating a populated list in database
    @Test
    public void testCreateList() {
        testList.add(testTrip);
        TripDbHelper.instance().createList();
        actualSize = TripDbHelper.instance().getAllLists().size();
        predictedSize = testList.size();
        assertEquals(predictedSize, actualSize);
    }

    // Tests deleting the created Trip from database
    @Test
    public void testDeleteList() {
        testList = TripDbHelper.instance().getAllLists();
        TripDbHelper.instance().deleteList(testList.get(testList.size() - 1));
        testList.remove(testList.size() - 1);
        actualSize = TripDbHelper.instance().getAllLists().size();
        predictedSize = testList.size();
        assertEquals(predictedSize, actualSize);
    }

    // Tests deleting a null list in database, expecting NullPointerException
    @Test
    public void testDeleteNullList() {
        exception.expect(NullPointerException.class);
        TripDbHelper.instance().deleteList(nullTrip);
    }

    // Tests deleting an empty list in database, expecting IndexOutOfBoundsException
    @Test
    public void testDeleteEmptyList() {
        exception.expect(IndexOutOfBoundsException.class);
        TripDbHelper.instance().deleteList(emptyList.get(0));
    }

    // Tests getting list from database
    @Test
    public void testGetAllLists() {
        actualSize = TripDbHelper.instance().getAllLists().size();
        assertTrue(actualSize >= 0);
        assertNotNull(TripDbHelper.instance().getAllLists());
    }

    // Tests deleting a list that does not exist in the database
    @Test
    public void testDeleteNonExistentTrip() {
        testList = TripDbHelper.instance().getAllLists();
        predictedSize = testList.size();
        // Adding a trip to testList that has not been added to the database
        testList.add(testTrip);
        //Attempting to delete the trip from the database
        TripDbHelper.instance().deleteList(testList.get(testList.size() - 1));
        actualSize = TripDbHelper.instance().getAllLists().size();
        assertEquals(predictedSize, actualSize);
    }
}
