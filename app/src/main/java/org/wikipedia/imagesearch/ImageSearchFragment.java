package org.wikipedia.imagesearch;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.wikipedia.BackPressedHandler;
import org.wikipedia.R;
import org.wikipedia.WikipediaApp;
import org.wikipedia.activity.FragmentUtil;
import org.wikipedia.history.HistoryEntry;
import org.wikipedia.page.PageTitle;
import org.wikipedia.search.SearchInvokeSource;
import org.wikipedia.settings.Prefs;
import org.wikipedia.views.ViewUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by amawai on 10/04/18.
 */

public class ImageSearchFragment extends Fragment implements BackPressedHandler {
    public interface Callback {
        void switchToSearchFragment(ImageSearchFragment imageSearchFragment,
                                    SearchInvokeSource source, String query);
        void onLoadPage(PageTitle title, HistoryEntry entry);
        void closeImageSearchFragment(@NonNull ImageSearchFragment fragment);
        void onImageSearchProgressBar(boolean enabled);
    }

    public static final String IMAGE_QUERY = "IMAGE_QUERY";
    private Unbinder unbinder;
    private ImageLabelAdapter imageLabelAdapter;

    private WikipediaApp app;

    @BindView(R.id.image_search_list) RecyclerView labelList;

    @NonNull public static ImageSearchFragment newInstance(List <String> labels) {
        ImageSearchFragment fragment = new ImageSearchFragment();

        Bundle args = new Bundle();
        args.putStringArrayList(IMAGE_QUERY, (ArrayList <String>) labels);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = WikipediaApp.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_imagesearch, container, false);
        unbinder = ButterKnife.bind(this, view);

        imageLabelAdapter = new ImageLabelAdapter(getContext());
        labelList.setAdapter(imageLabelAdapter);

        List <String> labels = getArguments().getStringArrayList(IMAGE_QUERY);
        setImageLabelList(labels);

        labelList.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onDestroyView() {
        labelList.setAdapter(null);
        unbinder.unbind();
        unbinder = null;
        super.onDestroyView();
    }

    @Override
    public boolean onBackPressed() {
        Callback callback = callback();
        callback.closeImageSearchFragment(getImageSearchFragment());

        return true;
    }

    //Setting the new list of labels
    public void setImageLabelList(List <String> imageLabels) {
        if(imageLabelAdapter != null) {
            imageLabelAdapter.setLabelList(imageLabels);
        }
    }

    @Nullable
    private Callback callback() {
        return FragmentUtil.getCallback(this, ImageSearchFragment.Callback.class);
    }

    private ImageSearchFragment getImageSearchFragment() {
        return this;
    }

    public final class ImageLabelAdapter extends RecyclerView.Adapter<ImageSearchFragment.ImageLabelHolder> {
        private Context context;
        List <String> labelTitleList;

        public ImageLabelAdapter(Context context) {
            this.context = context;
            this.labelTitleList = new ArrayList <String>();
        }

        @Override
        public ImageLabelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imagesearch_labels, parent, false);
            return new ImageLabelHolder(v);
        }

        @Override
        public void onBindViewHolder(ImageLabelHolder holder, int position) {
            holder.bindItem(labelTitleList.get(position));
        }

        @Override
        public int getItemCount() {
            return labelTitleList.size();
        }

        public void insert(int position, String imageLabel) {
            labelTitleList.add(position, imageLabel);
            notifyItemInserted(position);
        }

        public void setLabelList(List <String> imageLabels) {
            this.labelTitleList = imageLabels;
            notifyDataSetChanged();
        }
    }

    public final class ImageLabelHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int index;

        @BindView(R.id.imagesearch_label_holder) RelativeLayout labelLayout;
        @BindView(R.id.imagesearch_label_title) TextView labelText;

        public ImageLabelHolder(View labelView) {
            super(labelView);
            unbinder = ButterKnife.bind(this, labelView);
            labelLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            index = position;
            String labelString;
            Callback callback = callback();
            if (position >= 0) {
                labelString = this.labelText.getText().toString();
                switch(view.getId()){
                    case R.id.imagesearch_label_holder:
                    case R.id.imagesearch_label_title:
                        if (callback != null) {
                            if (Prefs.isImageSearchEnabled()){
                                callback.switchToSearchFragment(getImageSearchFragment(),
                                        SearchInvokeSource.IMAGE_SEARCH, labelString);
                            } else {
                                PageTitle title = new PageTitle(labelString, app.getWikiSite());
                                callback.onLoadPage(title, new HistoryEntry(title, new Date(), HistoryEntry.SOURCE_SEARCH));
                            }
                        }
                        callback.onImageSearchProgressBar(false);
                        break;
                }
            }
        }

        public void bindItem(String labelTitle) {
            labelText.setText(labelTitle);
        }
    }

}