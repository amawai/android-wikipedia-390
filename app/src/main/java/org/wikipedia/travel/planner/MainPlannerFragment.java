package org.wikipedia.travel.planner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.wikipedia.BackPressedHandler;
import org.wikipedia.R;
import org.wikipedia.concurrency.CallbackTask;
import org.wikipedia.travel.database.Trip;
import org.wikipedia.travel.database.TripDbHelper;
import org.wikipedia.travel.planner.date.TravelDatePickerFragment;
import org.wikipedia.travel.planner.places.PlacesFragment;
import org.wikipedia.travel.planner.trips.TripFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Artem on 2018-02-26.
 */

public class MainPlannerFragment extends Fragment implements BackPressedHandler{
    @BindView(R.id.fragment_travel_planner_view_pager) ViewPager viewPager;
    @BindView(R.id.planner_next) Button nextButton;
    PlannerFragmentPagerAdapter adapter;

    private Unbinder unbinder;
    private List<Trip> userTripsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_travel_planner, container, false);
        unbinder = ButterKnife.bind(this, view);
        getAppCompatActivity().getSupportActionBar().setTitle(getString(R.string.view_travel_card_title));
        adapter = new PlannerFragmentPagerAdapter(getChildFragmentManager());
        adapter.setTripListFragment(TripFragment.newInstance(userTripsList));
        viewPager.setAdapter((PagerAdapter) adapter);
        updateUserTripList();

        setupButtonListener();
        return view;
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    @Override
    public boolean onBackPressed() {
        if(viewPager.getCurrentItem() > 0) {
            prevPage();
            return true;
        }
        return false;
    }

    private void setupButtonListener() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
            }
        });
    }

    private void nextPage() {
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
    }

    private void prevPage() {
        viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
    }

    private void updateUserTripList() {
        CallbackTask.execute(() -> TripDbHelper.instance().getAllLists(),  new CallbackTask.DefaultCallback<List<Trip>>(){
            @Override
            public void success(List<Trip> list) {
                if (getActivity() == null) {
                    return;
                }
                updateTripList(list);
            }
        });
    }

    private void updateTripList(List<Trip> trips) {
        userTripsList = trips;
        adapter.getTripListFragment().updateUserTripList(trips);
    }

    public void newTrip() {
        adapter.makeNewTrip();
        nextPage();
    }

    public void openTrip(long id) {
        Trip trip = userTripsList.get((int)id);
        adapter.setupTripPages(trip);
        nextPage();
    }
}
