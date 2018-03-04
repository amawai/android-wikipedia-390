package org.wikipedia.travel;

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

import org.wikipedia.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

/**
 * Created by mnhn3 on 2018-03-04.
 */

public class PlacesFragment extends Fragment {
    private Unbinder unbinder;
    private RecyclerView recyclerView;
    //private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager linearLayoutManager;
    private List<PlacesCard> cardsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.travel_planner_places, container, false);//change xml to fragment

        recyclerView = (RecyclerView) view.findViewById(R.id.places_recycler_view);
        if (recyclerView!=null) {
            recyclerView.setHasFixedSize(true);
//            recyclerView.setLayoutManager(new LinearLayoutManager(PlacesToVisitActivity.getApplicationContext()));//check for error
            linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        fillList(cardsList);

        PlacesAdapter adapter = new PlacesAdapter(cardsList, getContext());
        recyclerView.setAdapter(adapter);

        unbinder = ButterKnife.bind(this, view);
        getAppCompatActivity().getSupportActionBar().setTitle(getString(R.string.view_travel_card_title));

        return view;
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    private void fillList(List cardsList){//dummy list filler, make it into a function later
        for (int i=0; i<=10; i++){
            PlacesCard place = new PlacesCard(
                    "location" +(i+1),
                    "this is a great location for tourists..."
            );
            cardsList.add(place);
        }
    }

}
