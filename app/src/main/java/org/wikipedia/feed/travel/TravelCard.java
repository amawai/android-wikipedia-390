package org.wikipedia.feed.travel;

import android.support.annotation.NonNull;

import org.wikipedia.dataclient.WikiSite;
import org.wikipedia.feed.model.Card;
import org.wikipedia.feed.model.CardType;

/**
 * Created by Artem on 2018-02-26.
 */

public class TravelCard extends Card {
    @NonNull
    @Override
    public CardType type() {
        return CardType.TRAVEL;
    }
}
