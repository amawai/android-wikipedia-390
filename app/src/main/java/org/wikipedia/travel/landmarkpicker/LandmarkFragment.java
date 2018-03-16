package org.wikipedia.travel.landmarkpicker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wikipedia.R;
import org.wikipedia.travel.destinationpicker.DestinationFragment;
import org.wikipedia.travel.trip.Trip;
import org.wikipedia.util.FeedbackUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mnhn3 on 2018-03-04.
 */

public class LandmarkFragment extends Fragment implements View.OnClickListener {

    private Unbinder unbinder;
    private RecyclerView.LayoutManager linearLayoutManager;
    private List<LandmarkCard> cardsList = new ArrayList<>();
    private String destinationName;

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

    /*
    //Currently has an exception error that make it hard to consistently make a list from
    public List<LandmarkCard> listNearbylandmarks(String location) { //use geocoder to take address list and and return landmarkcard list with titles
        List<LandmarkCard> landmarksList = new ArrayList<LandmarkCard>();
        List<Address> addresses;
        Geocoder gc = new Geocoder(getContext());

        try {
            addresses = gc.getFromLocationName(location, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < addresses.size(); i++) {
            landmarksList.get(i).setTitle(addresses.get(i).toString());
        }

        return landmarksList;
    }
    */

    private void fillList(List cardsList) {//dummy list filler, make it into a function later
        //landmarkholder for geocoder function
        String[] tokyo = {"Edo", "Tokyo", "Greater Tokyo Area", "Shinjuku Station", "Yoyogi Station", "Shinjuku Ni-chōme",
                "Tokyo subway sarin attack", "Kabukichō, Tokyo", "Tokyo Metropolitan Government Building", "Sangūbashi Station"};
        for (int i = 0; i < tokyo.length; i++) {
            LandmarkCard landmark = new LandmarkCard(
                    tokyo[i],
                    "this is a great location for tourists..."
            );
            cardsList.add(landmark);
        }
    }

    @Override
    public void onClick(View v) {
        String message = "Your trip has been saved.";
        FeedbackUtil.showMessage(getActivity(), message);
    }
}
