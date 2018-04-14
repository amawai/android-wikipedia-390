package org.wikipedia.feed.searchbar;

import android.content.Context;

import org.wikipedia.R;
import org.wikipedia.feed.view.DefaultFeedCardView;
import org.wikipedia.util.FeedbackUtil;
import org.wikipedia.util.ResourceUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchCardView extends DefaultFeedCardView<SearchCard> {

    public interface Callback {
        void onSearchRequested();
        void onVoiceSearchRequested();
        void onImageSearchRequested();
    }

    public SearchCardView(Context context) {
        super(context);
        inflate(getContext(), R.layout.view_search_bar, this);
        setCardBackgroundColor(ResourceUtil.getThemedColor(context, R.attr.searchItemBackground));
        ButterKnife.bind(this);
        FeedbackUtil.setToolbarButtonLongPressToast(findViewById(R.id.voice_search_button));
    }

    @OnClick(R.id.search_container)
    protected void onSearchClick() {
        if (getCallback() != null) {
            getCallback().onSearchRequested();
        }
    }

    @OnClick(R.id.voice_search_button)
    protected void onVoiceSearchClick() {
        if (getCallback() != null) {
            getCallback().onVoiceSearchRequested();
        }
    }

    @OnClick(R.id.search_image_button)
    protected void onImageSearchClick() {
        if (getCallback() != null) {
            getCallback().onImageSearchRequested();
        }
    }
}
