package org.wikipedia.travel.landmarkpicker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.wikipedia.R;
import org.wikipedia.travel.destinationpicker.DestinationFragment;
import org.wikipedia.util.FeedbackUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mnhn3 on 2018-03-04.
 */

public class LandmarkFragment extends Fragment implements View.OnClickListener {

    private Button mbutton;
    private Unbinder unbinder;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager linearLayoutManager;
    private List<LandmarkCard> cardsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_places, container, false);//change xml to fragment
        mbutton = (Button) view.findViewById(R.id.placetrip_next);
        mbutton.setOnClickListener((View.OnClickListener) this);
        //sets destination string in xml
        setDestination(DestinationFragment.getDestinationString(), view);

        recyclerView = (RecyclerView) view.findViewById(R.id.places_recycler_view);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        fillList(cardsList);

        LandmarkAdapter adapter = new LandmarkAdapter(cardsList, getContext());
        recyclerView.setAdapter(adapter);

        unbinder = ButterKnife.bind(this, view);
        getAppCompatActivity().getSupportActionBar().setTitle(getString(R.string.view_travel_card_title));

        return view;
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    private void setDestination(String[] destinationString, View view) { //to be implemented with destination fragment
        //city textview editors are removed for city for now, since address includes it
        //TextView city = (TextView) view.findViewById(R.id.city);
        TextView country = (TextView) view.findViewById(R.id.country);
        //city.setText(destinationString[0]);
        country.setText(destinationString[1]);
    }

    /*
    //Currently has an exception error that make it hard to consistently make a list from
    public List<LandmarkCard> listNearbyPlaces(String location) { //use geocoder to take address list and and return placecard list with titles
        List<LandmarkCard> placesList = new ArrayList<LandmarkCard>();
        List<Address> addresses;
        Geocoder gc = new Geocoder(getContext());

        try {
            addresses = gc.getFromLocationName(location, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < addresses.size(); i++) {
            placesList.get(i).setTitle(addresses.get(i).toString());
        }

        return placesList;
    }
    */

    private void fillList(List cardsList) {//dummy list filler, make it into a function later
        //placeholder for geocoder function
        String[] tokyo = {"Edo", "Tokyo", "Greater Tokyo Area", "Shinjuku Station", "Yoyogi Station", "Shinjuku Ni-chōme",
                "Tokyo subway sarin attack", "Kabukichō, Tokyo", "Tokyo Metropolitan Government Building", "Sangūbashi Station"};
        for (int i = 0; i < tokyo.length; i++) {
            LandmarkCard place = new LandmarkCard(
                    tokyo[i],
                    "this is a great location for tourists..."
            );
            cardsList.add(place);
        }
    }

    @Override
    public void onClick(View v) {
        String message = "Your trip has been saved.";
        FeedbackUtil.showMessage(getActivity(), message);
    }

}
