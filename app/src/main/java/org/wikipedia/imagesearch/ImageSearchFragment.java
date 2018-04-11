package org.wikipedia.imagesearch;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.wikipedia.R;
import org.wikipedia.search.SearchResultsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by amawai on 10/04/18.
 */

public class ImageSearchFragment extends Fragment {
    private Unbinder unbinder;
    private SearchResultsFragment searchResultsFragment;
    private ImageLabelAdapter imageLabelAdapter;


    @BindView(R.id.image_search_list) RecyclerView labelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_imagesearch, container, false);
        unbinder = ButterKnife.bind(this, view);

        FragmentManager childFragmentManager = getChildFragmentManager();
        searchResultsFragment = (SearchResultsFragment)childFragmentManager.findFragmentById(
                R.id.fragment_search_results);

        imageLabelAdapter = new ImageLabelAdapter(getContext());
        labelList.setAdapter(imageLabelAdapter);
        List<String> mock = new ArrayList<String>();
        mock.add("Apple");
        mock.add("Banana");
        mock.add("Dog");
        mock.add("Kitten");
        setImageLabelList(mock);

        labelList.setLayoutManager(new LinearLayoutManager(getContext()));

        getAppCompatActivity().getSupportActionBar().setTitle("Trip Planner");
        return view;
    }


    @Override
    public void onDestroyView() {
        labelList.setAdapter(null);
        unbinder.unbind();
        unbinder = null;
        super.onDestroyView();
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    //Setting the new list of labels
    public void setImageLabelList(List<String> imageLabels) {
        if(imageLabelAdapter != null) {
            imageLabelAdapter.setLabelList(imageLabels);
        }
    }


    public final class ImageLabelAdapter extends RecyclerView.Adapter<ImageSearchFragment.ImageLabelHolder> {
        private Context context;
        List<String> labelTitleList;

        public ImageLabelAdapter(Context context) {
            this.context = context;
            this.labelTitleList = new ArrayList<String>();
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

        public void setLabelList(List<String> imageLabels) {
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
            unbinder = ButterKnife.bind(this, labelView); //Can't tell if this is necessary
            labelLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            index = position;
            String labelString;
            if (position >= 0) {
                labelString = this.labelText.getText().toString();
                switch(view.getId()){
                    case R.id.imagesearch_label_holder:
                    case R.id.imagesearch_label_title:
                        Log.d("imagesearch", "selecting article " + labelString);
                        //searchResultsFragment.startSearch(labelString, false);
                        break;
                }
            }
        }

        public void bindItem(String labelTitle) {
            labelText.setText(labelTitle);
        }
    }
}