package org.wikipedia.feed.searchbar;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.wikipedia.R;
import org.wikipedia.feed.view.DefaultFeedCardView;
import org.wikipedia.util.FeedbackUtil;
import org.wikipedia.util.ResourceUtil;

import butterknife.BindView;
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
    void onSearchClick() {
        if (getCallback() != null) {
            getCallback().onSearchRequested();
        }
    }

    @OnClick(R.id.voice_search_button)
    void onVoiceSearchClick() {
        if (getCallback() != null) {
            getCallback().onVoiceSearchRequested();
        }
    }

    @OnClick(R.id.search_image_button)
    void onImageSearchClick() {
        if (getCallback() != null) {
            getCallback().onImageSearchRequested();
        }
    }
}
