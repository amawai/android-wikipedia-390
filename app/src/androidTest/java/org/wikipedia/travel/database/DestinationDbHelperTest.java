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

public class DestinationDbHelperTest {
    private List<Trip> testList = DestinationDbHelper.getInstance().getAllLists();
    private List<Trip> emptyList = new ArrayList <>(0);
    private Trip testTrip = new Trip(new Trip.Destination("Laval"));
    private Trip nullTrip = null;
    private long predictedSize;
    private long actualSize;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    // Tests creating a populated list in database
    @Test
    public void testCreateList() {
        testList.add(testTrip);
        DestinationDbHelper.getInstance().createList(testList.get(testList.size() - 1).getDestination());
        actualSize = DestinationDbHelper.getInstance().getAllLists().size();
        predictedSize = testList.size();
        assertEquals(predictedSize, actualSize);
    }

    // Tests deleting the created Trip from database
    @Test
    public void testDeleteList() {
        testList = DestinationDbHelper.getInstance().getAllLists();
        DestinationDbHelper.getInstance().deleteList(testList.get(testList.size()-1));
        testList.remove(testList.size() - 1);
        actualSize = DestinationDbHelper.getInstance().getAllLists().size();
        predictedSize = testList.size();
        assertEquals(predictedSize, actualSize);
    }

    // Tests deleting a null list in database, expecting NullPointerException
    @Test
    public void testDeleteNullList() {
        exception.expect(NullPointerException.class);
        DestinationDbHelper.getInstance().deleteList(nullTrip);
    }

    // Tests deleting an empty list in database, expecting IndexOutOfBoundsException
    @Test
    public void testDeleteEmptyList() {
        exception.expect(IndexOutOfBoundsException.class);
        DestinationDbHelper.getInstance().deleteList(emptyList.get(0));
    }

    // Tests deleting a null list in database, expecting NullPointerException
    @Test
    public void testCreateNullList() {
        exception.expect(NullPointerException.class);
        DestinationDbHelper.getInstance().createList(nullTrip.getDestination());
    }

    // Tests creating an empty list in database, expecting IndexOutOfBoundsException
    @Test
    public void testCreateEmptyList() {
        exception.expect(IndexOutOfBoundsException.class);
        DestinationDbHelper.getInstance().createList(emptyList.get(0).getDestination());
    }

    // Tests getting list from database
    @Test
    public void testGetAllLists() {
       actualSize = DestinationDbHelper.getInstance().getAllLists().size();
       assertTrue(actualSize >= 0);
       assertNotNull(DestinationDbHelper.getInstance().getAllLists());
    }

    // Tests deleting a list that does not exist in the database
    @Test
    public void testDeleteNonExistentTrip() {
        testList = DestinationDbHelper.getInstance().getAllLists();
        predictedSize = testList.size();
        // Adding a trip to testList that has not been added to the database
        testList.add(testTrip);
        //Attempting to delete the trip from the database
        DestinationDbHelper.getInstance().deleteList(testList.get(testList.size() - 1));
        actualSize = DestinationDbHelper.getInstance().getAllLists().size();
        assertEquals(predictedSize, actualSize);
    }
}
