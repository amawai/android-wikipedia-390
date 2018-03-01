package org.wikipedia.travel;

import android.support.annotation.NonNull;

import org.wikipedia.dataclient.WikiSite;
import org.wikipedia.feed.dataclient.DummyClient;
import org.wikipedia.feed.model.Card;
import org.wikipedia.feed.travel.TravelCard;

/**
 * Created by Artem on 2018-02-26.
 */

public class TravelClient extends DummyClient {
    @Override
    public Card getNewCard(@NonNull WikiSite wiki) {
        return new TravelCard();
    }
}
