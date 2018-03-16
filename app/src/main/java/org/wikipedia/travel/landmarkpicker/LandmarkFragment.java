package org.wikipedia.travel.landmarkpicker;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wikipedia.R;
import org.wikipedia.WikipediaApp;
import org.wikipedia.dataclient.WikiSite;
import org.wikipedia.dataclient.mwapi.MwQueryResponse;
import org.wikipedia.nearby.NearbyClient;
import org.wikipedia.nearby.NearbyPage;
import org.wikipedia.nearby.NearbyResult;
import org.wikipedia.travel.destinationpicker.DestinationFragment;
import org.wikipedia.travel.trip.Trip;
import org.wikipedia.util.FeedbackUtil;
import org.wikipedia.util.ThrowableUtil;
import org.wikipedia.util.log.L;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

/**
 * Created by mnhn3 on 2018-03-04.
 */

public class LandmarkFragment extends Fragment implements View.OnClickListener {

    private Unbinder unbinder;
    private RecyclerView.LayoutManager linearLayoutManager;
    private List<LandmarkCard> cardsList = new ArrayList<>();
    private String destinationName;

    //@BindView(R.id.landmark_button_next) FloatingActionButton nextButton;
    //@BindView(R.id.landmark_view_recycler) RecyclerView recyclerView;
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
        destinationName = getArguments().getString("DESTINATION");
        destinationText.setText(destinationName);

        recyclerView = (RecyclerView) view.findViewById(R.id.landmark_view_recycler);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        fillList(cardsList);

        LandmarkAdapter adapter = new LandmarkAdapter(cardsList, getContext());

        recyclerView.setAdapter(adapter);

        return view;
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    private void setDestination(String[] destinationString, View view) { //to be implemented with destination fragment
        //landmark_city_text textview editors are removed for landmark_city_text for now, since address includes it
        //TextView landmark_city_text = (TextView) view.findViewById(R.id.landmark_city_text);
        TextView landmark_country_view_text = (TextView) view.findViewById(R.id.landmark_country_view_text);
        //landmark_city_text.setText(destinationString[0]);
        landmark_country_view_text.setText(destinationString[1]);
    }

    public List <String> listNearbyPlaces(){ //use geocoder to take address list and and return placecard list with titles
        List<String> landmarksList = new ArrayList<String>();
    /*
    //Currently has an exception error that make it hard to consistently make a list from
    public List<LandmarkCard> listNearbylandmarks(String location) { //use geocoder to take address list and and return landmarkcard list with titles
        List<LandmarkCard> landmarksList = new ArrayList<LandmarkCard>();
        List<Address> addresses;
        Geocoder gc = new Geocoder(getContext());
        NearbyClient client = new NearbyClient();
        double mapRadius=4153.95;
        double lat=0;
        double longi=0;

        String location = DestinationFragment.getDestinationString()[1];//change to input to destinationstring later, can also remove input
        try {
            List<Address> addresses= gc.getFromLocationName(location, 5);
            if (addresses.size()>0){
                for (Address a:addresses){
                    //landmarksList.add(a.toString());
                    lat = a.getLatitude();
                    longi = a.getLongitude();
                }
            }
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
        WikiSite wiki = WikipediaApp.getInstance().getWikiSite();
        client.request(wiki, lat, longi, mapRadius,
                new NearbyClient.Callback() {
                    @Override
                    public void success(@NonNull Call<MwQueryResponse> call, @NonNull NearbyResult result) {
                        if (!isResumed()) {
                            return;
                        }
                        for (NearbyPage item : result.getList()) {
                            Log.d("NEARBY_TITLE", "mM: " + item.getTitle());
                            landmarksList.add(item.getTitle());//adds nothing
                        }
                    }

                    @Override
                    public void failure(@NonNull Call<MwQueryResponse> call,
                                        @NonNull Throwable caught) {
                        if (!isResumed()) {
                            return;
                        }
                        ThrowableUtil.AppError error = ThrowableUtil.getAppError(getActivity(), caught);
                        L.e(caught);
                    }
                });


*/
        return landmarksList;
    }

    private void fillList(List cardsList){//dummy list filler, make it into a function later
        List<String> landMarkList = listNearbyPlaces();
        //placesList.add("one");//test if list has elements
        for (int i=0; i<landMarkList.size(); i++){
            LandmarkCard card = new LandmarkCard(
                    landMarkList.get(i),
                    "this is a great location for tourists..."
            );
        }
    }

    @Override
    public void onClick(View v) {
        String message = "Your trip has been saved.";
        FeedbackUtil.showMessage(getActivity(), message);
    }
}
