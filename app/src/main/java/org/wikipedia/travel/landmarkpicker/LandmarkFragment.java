package org.wikipedia.travel.landmarkpicker;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.wikipedia.R;
import org.wikipedia.WikipediaApp;
import org.wikipedia.activity.FragmentUtil;
import org.wikipedia.dataclient.WikiSite;
import org.wikipedia.dataclient.mwapi.MwQueryResponse;
import org.wikipedia.history.HistoryEntry;
import org.wikipedia.nearby.NearbyClient;
import org.wikipedia.nearby.NearbyPage;
import org.wikipedia.nearby.NearbyResult;
import org.wikipedia.page.PageTitle;
import org.wikipedia.travel.MainPlannerFragment;
import org.wikipedia.util.FeedbackUtil;
import org.wikipedia.util.ThrowableUtil;
import org.wikipedia.util.log.L;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

/**
 * Created by mnhn3 on 2018-03-04.
 */

public class LandmarkFragment extends Fragment implements View.OnClickListener {
    public interface Callback {
        void onLoadPage(PageTitle title, HistoryEntry entry);
        String getLmDestinationName();
        void onSave(List<LandmarkCard> saveList);
    }
    private Unbinder unbinder;
    private RecyclerView.LayoutManager linearLayoutManager;
    private List<LandmarkCard> cardsList = new ArrayList<>();
    private String destinationName;
    private LandmarkAdapter adapter;
    private NearbyResult lastResult;

    private List<LandmarkCard> selectedLandmarks;

    @BindView(R.id.landmark_view_recycler) RecyclerView recyclerView;
    @BindView(R.id.landmark_country_view_text) TextView destinationText;

    public static LandmarkFragment newInstance(String destinationName) {

        Bundle args = new Bundle();
        args.putString("DESTINATION", destinationName);

        LandmarkFragment fragment = new LandmarkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_travel_landmark_picker, container, false);//change xml to fragment
        unbinder = ButterKnife.bind(this, view);
        //destinationName = getArguments().getString("DESTINATION");

        //set, display and get results using desName
        //destinationName = DestinationFragment.destination.getAddress().toString();
        destinationName = getCallback().getLmDestinationName();
        destinationText.setText(destinationName);

        //recyclerView = (RecyclerView) view.findViewById(R.id.landmark_view_recycler);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        adapter = new LandmarkAdapter(cardsList, getContext());

        recyclerView.setAdapter(adapter);
        retrieveArticles(destinationName);
        selectedLandmarks = new ArrayList<LandmarkCard>();
        return view;
    }

    @Override
    public void onDestroyView() {
        recyclerView.setAdapter(null);
        unbinder.unbind();
        unbinder = null;
        super.onDestroyView();
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    public void setLandmarkCards(List<LandmarkCard> landmarks) {
        adapter.setLandmarkCardList(landmarks);
    }

    private void setDestination(String[] destinationString, View view) {//removed deprecated function
        TextView landmark_country_view_text = (TextView) view.findViewById(R.id.landmark_country_view_text);
        landmark_country_view_text.setText(destinationString[1]);
    }


    @Override
    public void onClick(View v) {
        String message = "Your trip has been saved.";
        FeedbackUtil.showMessage(getActivity(), message);
        getCallback().onSave(selectedLandmarks);
    }

    private void onLoadPage(@NonNull PageTitle title, HistoryEntry entry) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.onLoadPage(title, entry);
        }
    }

    private String getLmDestinationName(){
        Callback callback = getCallback();
        if (callback != null) {
            callback.getLmDestinationName();
        }
            return "";
    }
    private void onSave(List<LandmarkCard> saveList){
        Callback callback = getCallback();
        if (callback != null) {
            callback.onSave(saveList);
        }
    }

    //Provide the adapter with new landmark data to display
    private void fillList(Map<String, String> landMarkList){

        cardsList.clear();
        for (String key : landMarkList.keySet()) {
            LandmarkCard card = new LandmarkCard(
                key, "some description", landMarkList.get(key)
            );
            cardsList.add(card);
        }
        adapter.setLandmarkCardList(cardsList);
    }

    private void extractLandmarkArticles(NearbyResult nearbyArticles) {
        Map<String, String> landMarkList = new HashMap<String, String>();
        for (NearbyPage item : nearbyArticles.getList()) {
            landMarkList.put(item.getTitle(), item.getThumbUrl());
        }
        fillList(landMarkList);
    }

    //Location can be entered as followed: "City, State/Province/Country" or simply "City"
    private void retrieveArticles(String location) {
        double lat = 0;
        double longi = 0;
        Geocoder gc = new Geocoder(getContext());
        try {
            List<Address> addresses= gc.getFromLocationName(location, 5);
            List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
            for(Address a : addresses){
                if(a.hasLatitude() && a.hasLongitude()){
                    lat = a.getLatitude();
                    longi = a.getLongitude();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        NearbyClient client = new NearbyClient();
        WikiSite wiki = WikipediaApp.getInstance().getWikiSite();
        client.request(wiki, lat,longi, 5000,
                new NearbyClient.Callback() {
                    @Override public void success(@NonNull Call<MwQueryResponse> call,
                                                  @NonNull NearbyResult result) {
                        if (!isResumed()) {
                            return;
                        }
                        lastResult = result;
                        //Send over the list of NearbyPages to the next function
                        extractLandmarkArticles(result);
                    }

                    @Override public void failure(@NonNull Call<MwQueryResponse> call,
                                                  @NonNull Throwable caught) {
                        if (!isResumed()) {
                            return;
                        }
                        ThrowableUtil.AppError error = ThrowableUtil.getAppError(getActivity(), caught);
                        L.e(caught);
                    }
                });
    }



    @Nullable
    private Callback getCallback() {
        return FragmentUtil.getCallback(this, Callback.class);
    }

    public class LandmarkAdapter extends RecyclerView.Adapter<LandmarkAdapter.ViewHolder> {

        private List<LandmarkCard> landmarkCardList;
        private Context context;

        public LandmarkAdapter(List<LandmarkCard> landmarkCards, Context context) {
            this.landmarkCardList = landmarkCards;
            this.context = context;
        }

        @Override
        public LandmarkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_card_travel_landmark_picker_landmarks, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(LandmarkAdapter.ViewHolder holder, int position) {
            LandmarkCard landmarkCard = landmarkCardList.get(position);
            holder.bindItem(landmarkCard);
        }

        public void setLandmarkCardList(List<LandmarkCard> landmarkList) {
            this.landmarkCardList = landmarkList;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return landmarkCardList.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        // Insert and remove for implementation of view closing feature
        public void insert(int position, LandmarkCard landmarkCard) {
            landmarkCardList.add(position, landmarkCard);
            notifyItemInserted(position);
        }

        public void remove(LandmarkCard landmarkCard) {
            int position = landmarkCardList.indexOf(landmarkCard);
            landmarkCardList.remove(position);
            notifyItemRemoved(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private CardView cv;
            private TextView textViewTitle;
            private TextView textViewDesc;
            private CheckBox checkBox;

            ViewHolder(View itemView) {
                super(itemView);

                cv = (CardView) itemView.findViewById(R.id.view_card);
                textViewTitle = (TextView) itemView.findViewById(R.id.landmark_title_text_view);
                textViewDesc = (TextView) itemView.findViewById(R.id.landmark_desc_text_view);
                checkBox = (CheckBox) itemView.findViewById(R.id.landmark_check_box);

                cv.setOnClickListener(this);
                checkBox.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                LandmarkCard card = cardsList.get(position);
                PageTitle title = new PageTitle(card.getTitle(), lastResult.getWiki(), card.getThumbUrl());
                switch (v.getId()){
                    case R.id.view_card:
                        if (position >= 0) {
                            //Load the page when clicking on the article
                            getCallback().onLoadPage(title, new HistoryEntry(title, new Date(), HistoryEntry.SOURCE_LANDMARK));
                        }
                        break;
                    case R.id.landmark_check_box:
                        //Adds the LandmarkCard object, comprised of card title and thumbUrl into list
                        if (card.getChecked()) {
                            card.setChecked(false);
                            selectedLandmarks.remove(card);
                        }
                        else  {
                            card.setChecked(true);
                            selectedLandmarks.add(card);
                        }
                        break;
                }
            }

            public void bindItem(LandmarkCard landmarkCard) {
                textViewTitle.setText(landmarkCard.getTitle());
                textViewDesc.setText(landmarkCard.getDesc());
            }

        }
    }
}