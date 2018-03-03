package org.wikipedia.travel;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import static org.mockito.Mockito.*;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.wikipedia.travel.Trip;

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Trip.class)
public class TripTest {
    @Test
    public void testMockSetAndGetId() {

    }

    @Test
    public void testMockSetAndGetTitle() {

    }

    @Test
    public void testMockSetAndGetDepartureDate() {

    }

    @Test
    public void testMockSetAndGetDestination() {

    }

    @Test
    public void testMockAddAndGetDestinations() {

    }
}

