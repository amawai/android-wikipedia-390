package org.wikipedia.travel;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import org.wikipedia.R;
import org.wikipedia.feed.travel.TravelCard;
import org.wikipedia.feed.view.StaticCardView;
import org.wikipedia.travel.planner.TravelPlannerActivity;

/**
 * Created by Artem on 2018-02-26.
 */

public class TravelCardView extends StaticCardView<TravelCard> {
    public TravelCardView(Context context) {
        super(context);
    }

    @Override public void setCard(@NonNull TravelCard card) {
        super.setCard(card);
        setTitle(getString(R.string.view_travel_card_title));
        setSubtitle(getString(R.string.view_travel_card_subtitle));
        //setIcon(R.drawable.ic_casino_accent50_24dp); // TODO: change icon
        setContainerBackground(R.color.accent50); // TODO: change color
        setAction(R.drawable.ic_arrow_forward_black_24dp , R.string.view_travel_card_action);
    }

    protected void onContentClick(View v) { openTravelPlanner(); }


    protected void onActionClick(View v) { openTravelPlanner(); }

    private void openTravelPlanner() {
        getContext().startActivity(new Intent(TravelPlannerActivity.newIntent(getContext())));
    }
}
