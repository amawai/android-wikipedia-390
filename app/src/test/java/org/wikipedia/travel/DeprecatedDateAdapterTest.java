package org.wikipedia.travel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wikipedia.test.TestRunner;
import org.wikipedia.travel.database.DeprecatedDateAdapter;

import static junit.framework.Assert.assertEquals;

@RunWith(TestRunner.class)
public class DeprecatedDateAdapterTest {
    private DeprecatedDateAdapter dateAdapter;

    @Before
    public void setup() {
        dateAdapter = new DeprecatedDateAdapter(1, 1, 1);
    }

    //Getter/setter tests required, because the getters and setters have been overriden
    @Test
    public void testDataFields() {
        int year = 1993;
        dateAdapter.setYear(year);
        assertEquals(dateAdapter.getYear(), year);
    }
}
