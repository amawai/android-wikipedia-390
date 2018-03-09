package org.wikipedia.travel.trip;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.wikipedia.test.TestRunner;
import org.wikipedia.travel.trip.Trip;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.Date;

@RunWith(TestRunner.class)
@PrepareForTest(Trip.class)
public class TripTest {

    @Test
    public void testMockSetAndGetId() {
        Trip mockTrip = mock(Trip.class);
        final Long[] tripId = new Long[1];

        //Mocking the setter and getter
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                tripId[0] = (Long) invocation.getArguments()[0];
                return null;
            }
        }).when(mockTrip).setId(anyLong());

        when(mockTrip.getId()).thenAnswer(new Answer<Long>() {
            @Override
            public Long answer(InvocationOnMock invocation) throws Throwable {
                return tripId[0];
            }
        });

        mockTrip.setId(21);
        assertEquals(21, mockTrip.getId());
        mockTrip.setId(7);
        assertEquals(7, mockTrip.getId());
    }

    @Test
    public void testMockSetAndGetTitle() {
        Trip mockTrip = mock(Trip.class);
        final String[] tripTitle = new String[1];

        //Mocking the setter and getter
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                tripTitle[0] = (String) invocation.getArguments()[0];
                return null;
            }
        }).when(mockTrip).setTitle(anyString());

        when(mockTrip.getTitle()).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return tripTitle[0];
            }
        });

        mockTrip.setTitle("Trip of a Lifetime");
        assertEquals("Trip of a Lifetime", mockTrip.getTitle());
    }

    @Test
    public void testMockSetAndGetTripDepartureDate() {
        Trip mockTrip = mock(Trip.class);
        final Date[] tripDepartureDate = new Date[1];
        final Date testDate = new Date();

        //Mocking the setter and getter
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                tripDepartureDate[0] = (Date) invocation.getArguments()[0];
                return null;
            }
        }).when(mockTrip).setTripDepartureDate(anyObject());

        when(mockTrip.getTripDepartureDate()).thenAnswer(new Answer<Object>() {
            @Override
            public Date answer(InvocationOnMock invocation) throws Throwable {
                return tripDepartureDate[0];
            }
        });

        mockTrip.setTripDepartureDate(testDate);
        assertTrue(mockTrip.getTripDepartureDate().toString().equals(testDate.toString()));
    }

    //Similar test can be conducted for the addDestination and getDestinations method in the Trip class
    @Test
    public void testMockSetAndGetDestination() {
        Trip mockTrip = mock(Trip.class);
        final Trip.Destination[] testTripDestinations = new Trip.Destination[1];
        final Trip.Destination testDestination = new Trip.Destination("New Zealand");

        //Mocking the setter and getter
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                testTripDestinations[0] = (Trip.Destination) invocation.getArguments()[0];
                return null;
            }
        }).when(mockTrip).setDestination(anyObject());

        when(mockTrip.getDestination()).thenAnswer(new Answer<Object>() {
            @Override
            public Trip.Destination answer(InvocationOnMock invocation) throws Throwable {
                return testTripDestinations[0];
            }
        });

        mockTrip.setDestination(testDestination);
        assertTrue(mockTrip.getDestination().getDestinationName().equals(testDestination.getDestinationName()));
    }

}
