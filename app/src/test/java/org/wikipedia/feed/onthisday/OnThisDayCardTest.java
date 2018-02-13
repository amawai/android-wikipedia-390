package org.wikipedia.feed.onthisday;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.wikipedia.dataclient.WikiSite;
import org.wikipedia.test.TestRunner;
import org.wikipedia.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@Config(manifest=Config.NONE)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({DateUtil.class})
public class OnThisDayCardTest {
    private final int YEAR = 2004;
    private final CharSequence FACT = "Napoleon Dynamite came out";

    // private static WikiSite TEST = WikiSite.forLanguageCode("test");
    private static WikiSite TEST = mock(WikiSite.class);
    private List<OnThisDay.Event> events;
    private OnThisDay.Event event;
    private OnThisDayCard card;

    @Before public void setUp() throws Throwable {
        mockStaticDependency();
        mockParameters();
        card = new OnThisDayCard(events, TEST, 0);
    }

    @Test public void testShareString() {
        String expectedShare = "June 11, 2004 (14 years ago): \n" +
                "Napoleon Dynamite came out\n\n" +
                "~ Via Wikipedia";
        assertEquals(expectedShare, card.shareString());
    }

    @NonNull public void mockStaticDependency() {
        PowerMockito.mockStatic(DateUtil.class);
        when(DateUtil.getDefaultDateFor(anyInt())).thenReturn(new GregorianCalendar(2004, 6, 11));
        when(DateUtil.getMonthOnlyDateString(anyObject())).thenReturn("June 11");
        when(DateUtil.getYearDifferenceString(anyInt())).thenReturn("14 years ago");
    }

    @NonNull public void mockParameters() {
        OnThisDay.Event event = mock(OnThisDay.Event.class);
        events = new ArrayList<OnThisDay.Event>();
        events.add(event); //Package the single event in the list
        when(event.year()).thenReturn(YEAR);
        when(event.text()).thenReturn(FACT);
    }
}
