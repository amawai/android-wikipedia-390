package org.wikipedia.travel;

import org.junit.Before;
import org.junit.Test;
import org.wikipedia.dataclient.WikiSite;
import org.wikipedia.feed.model.Card;
import org.wikipedia.feed.model.CardType;
import org.wikipedia.feed.travel.TravelCard;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Created by Artem on 2018-02-27.
 */

public class TravelClientTest {
    private WikiSite wiki;
    private TravelClient client;

    @Before
    public void setup() {
         wiki = mock(WikiSite.class);
         client = new TravelClient();
    }
    @Test
    public void testClientMakesCards() {
        Card card = client.getNewCard(wiki);

        // Client should make TravelCard objects
        assertNotNull(card);
        assertEquals(card.getClass(), TravelCard.class);
        assertEquals(card.type(), CardType.TRAVEL);
    }
}
