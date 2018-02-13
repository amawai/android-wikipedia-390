package org.wikipedia.feed.onthisday;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.wikipedia.dataclient.WikiSite;
import org.wikipedia.test.TestRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Config(manifest=Config.NONE)
@RunWith(TestRunner.class)
public class OnThisDayCardTest {
    private final int YEAR = 2000;
    private final CharSequence FACT = "Napoleon Dynamite came out";

    // private static WikiSite TEST = WikiSite.forLanguageCode("test");
    private static WikiSite TEST = mock(WikiSite.class);
    private List<OnThisDay.Event> events;
    private OnThisDay.Event event;
    private OnThisDayCard card;

    @Before public void setUp() throws Throwable {
        OnThisDay.Event event = mock(OnThisDay.Event.class);
        events = new ArrayList<OnThisDay.Event>();
        events.add(event); //Package the single event in the list
        when(event.year()).thenReturn(YEAR);
        when(event.text()).thenReturn(FACT);
        card = new OnThisDayCard(events, TEST, 0);
    }

    @Test public void testShareString() {
        // STUB
    }
}
