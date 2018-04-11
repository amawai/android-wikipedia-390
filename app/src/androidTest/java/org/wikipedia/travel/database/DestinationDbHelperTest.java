package org.wikipedia.travel.database;

import org.junit.Test;
import org.wikipedia.travel.trip.Trip;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class DestinationDbHelperTest {

    @Test
    public void testCreateAndDeleteList() {
        List<Trip> list = DestinationDbHelper.getInstance().getAllLists();
        Trip testTrip = new Trip(new Trip.Destination("Laval"));
        long predictedSize;
        long newSize;

        // Test for list creation
        list.add(testTrip);
        DestinationDbHelper.getInstance().createList(list.get(list.size()-1).getDestination());
        newSize = DestinationDbHelper.getInstance().getAllLists().size();
        predictedSize = list.size();
        assertEquals(predictedSize, newSize);

        // Test for list deletion
        list = DestinationDbHelper.getInstance().getAllLists();
        DestinationDbHelper.getInstance().deleteList(list.get(list.size()-1));
        list.remove(list.size()-1);
        newSize = DestinationDbHelper.getInstance().getAllLists().size();
        predictedSize = list.size();
        assertEquals(predictedSize, newSize);
    }

    @Test
    public void testGetAllLists() {
       int size = DestinationDbHelper.getInstance().getAllLists().size();
       assertTrue(size >= 0);
       assertNotNull(DestinationDbHelper.getInstance().getAllLists());
    }
}
