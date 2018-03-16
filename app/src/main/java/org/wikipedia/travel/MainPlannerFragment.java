package org.wikipedia.travel;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;

import org.wikipedia.BackPressedHandler;
import org.wikipedia.R;
import org.wikipedia.concurrency.CallbackTask;
import org.wikipedia.travel.database.TripDbHelper;
import org.wikipedia.travel.datepicker.DateFragment;
import org.wikipedia.travel.destinationpicker.DestinationFragment;
import org.wikipedia.travel.trip.Trip;
import org.wikipedia.travel.trip.TripFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Artem on 2018-02-26.
 */

public class MainPlannerFragment extends Fragment implements BackPressedHandler, TripFragment.Callback,
        DestinationFragment.Callback, DateFragment.Callback {
    @BindView(R.id.fragment_travel_planner_view_pager) ViewPager viewPager;
    @BindView(R.id.planner_next) Button bNext;
    @BindView(R.id.planner_save) Button bSave;
    @BindView(R.id.planner_title) TextView tvTitle;
    PlannerFragmentPagerAdapter adapter;
    Trip openTrip;

    private Unbinder unbinder;
    private List<Trip> userTripsList = new ArrayList<>();

    public static MainPlannerFragment newInstance() {

        Bundle args = new Bundle();
        
        MainPlannerFragment fragment = new MainPlannerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /*
        Lifecycle methods
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_travel_planner_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        getAppCompatActivity().getSupportActionBar().setTitle(getString(R.string.view_travel_card_title));
        adapter = new PlannerFragmentPagerAdapter(getChildFragmentManager());
        adapter.setTripListFragment(TripFragment.newInstance());
        updateUserTripList();
        viewPager.setAdapter((PagerAdapter) adapter);
        setupButtonListeners();
        updateUserTripList();
        setPageTitle(viewPager.getCurrentItem());

        return view;
    }

    private void setupButtonListeners() {
        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPage();
            }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTrip(openTrip);
            }
        });
    }

    @Override

    public void onResume() {
        super.onResume();
        updateUserTripList();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    /*
        Child fragment callbacks
     */

    @Override
    public void onNewTrip() {
        newTrip();
    }

    @Override
    public void onOpenTrip(long id) {
        openTrip(id);
    }

    @Override
    public void onRequestTripListUpdate() {
        updateUserTripList();
    }

    @Override
    public void onPlaceSelected(Place destination) {
        openTrip.setDestinationName((String) destination.getName());
    }

    @Override
    public void onDateChanged(int year, int month, int day) {
        openTrip.setTripDepartureDate(year, month, day);
    }

    /*
            BackPressedListener
         */
    @Override
    public boolean onBackPressed() {
        if(viewPager.getCurrentItem() > 0) {
            prevPage();
            return true;
        }
        return false;
    }

    /*
        Data persistence
     */
    private void fetchUserTrips() {
        CallbackTask.execute(() -> TripDbHelper.instance().getAllLists(), new CallbackTask.DefaultCallback<List<Trip>>(){
            @Override
            public void success(List<Trip> result) {
                super.success(result);
            }
        });
    }

    private void saveTrip(Trip trip) {
        bSave.setActivated(false);
        //Do some stuff to save trip info
        //Return to first page
        CallbackTask.execute(() -> TripDbHelper.instance().updateList(trip),  new CallbackTask.DefaultCallback<Object>(){
            @Override
            public void success(Object o) {
                Toast.makeText(getActivity(),"Trip saved", Toast.LENGTH_SHORT).show();
                goToHomePage();
            }

            @Override
            public void failure(Throwable caught) {
                super.failure(caught);
                bSave.setActivated(true);
            }
        });
    }

    public void updateUserTripList() {
        CallbackTask.execute(() -> TripDbHelper.instance().getAllLists(),  new CallbackTask.DefaultCallback<List<Trip>>(){
            @Override
            public void success(List<Trip> list) {
                if (getActivity() == null) {
                    return;
                }
                userTripsList = list;
                adapter.getTripListFragment().setUserTripList(list);
            }
        });
    }

    /*
        Trip creation and management
     */
    public void newTrip() {
        Trip trip = new Trip("Dream trip", new Trip.Destination(""), new Date(2018, 03, 06));
        //TODO: Improve this for release 2
        trip.getDestination().setDestinationName("");
        userTripsList.add(trip);
        openTrip(trip.getId());
    }

    public void openTrip(long id) {
        Toast.makeText(getActivity(), "Opening trip: " + id, Toast.LENGTH_SHORT).show();
        openTrip = getTrip(id);
        adapter.setupTripPages(openTrip);
        nextPage();
    }

    public Trip getTrip(long id) {
        for(int i = 0; i<userTripsList.size(); i++) {
            if(userTripsList.get(i).getId() == id) {
                return userTripsList.get(i);
            }
        }
        return null;
    }

    private int getCurrentPage() {
        return viewPager.getCurrentItem();
    }

    private int getLastPage() {
        return adapter.getCount()-1;
    }

    private void goToPage(int page) {
        viewPager.setCurrentItem(page);
        int newPage = viewPager.getCurrentItem();
        setPageTitle(newPage);
        setButtonVisibility(page);
        if(page == 0) {
            updateUserTripList();
        }
    }

    private void goToHomePage() {
        goToPage(0);
        bSave.setActivated(true);
        //TODO: Prompt user to save unsaved changes
        adapter.destroyTripPages();
        openTrip = null;
    }

    private void nextPage() {
        goToPage(getCurrentPage()+1);
    }

    private void prevPage() {
        goToPage(getCurrentPage()-1);
    }

    /*
        Navigation
     */
    private void setPageTitle(int page) {
        switch(page) {
            case 0:
                tvTitle.setText("Trips");
                break;
            case 1:
                tvTitle.setText("Destination");
                break;
            case 2:
                tvTitle.setText("Departure Date");
                break;
            default:
                tvTitle.setText("Landmarks");
        }
    }



    private void setButtonVisibility(int page) {
        if(page == 0) {
            bNext.setVisibility(View.INVISIBLE);
        } else {
            bNext.setVisibility(View.VISIBLE);
        }
        if(getLastPage() == page && page > 2) {
            bSave.setVisibility(View.VISIBLE);
        } else {
            bSave.setVisibility(View.INVISIBLE);
        }
    }

    public void setDestination(String destinationName){
        if(openTrip.getDestination() == null) {
            openTrip.setDestination(new Trip.Destination(destinationName));
        } else {
            openTrip.getDestination().setDestinationName(destinationName);
        }
    }
}