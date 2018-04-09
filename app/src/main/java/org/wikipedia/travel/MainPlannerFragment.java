package org.wikipedia.travel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
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
import org.wikipedia.activity.FragmentUtil;
import org.wikipedia.concurrency.CallbackTask;
import org.wikipedia.history.HistoryEntry;
import org.wikipedia.page.PageActivity;
import org.wikipedia.page.PageTitle;
import org.wikipedia.concurrency.CallbackTask;
import org.wikipedia.travel.database.DestinationDbHelper;
import org.wikipedia.travel.database.TripDbHelper;
import org.wikipedia.travel.datepicker.DateFragment;
import org.wikipedia.travel.destinationpicker.DestinationFragment;
import org.wikipedia.travel.landmarkpicker.LandmarkCard;
import org.wikipedia.travel.landmarkpicker.LandmarkFragment;
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
        DestinationFragment.Callback, DateFragment.Callback, LandmarkFragment.Callback{
    public interface Callback {
        void onLoadPage(PageTitle title, HistoryEntry entry);
    }
    @BindView(R.id.fragment_travel_planner_view_pager) ViewPager viewPager;
    @BindView(R.id.planner_next) Button bNext;
    @BindView(R.id.planner_save) Button bSave;
    @BindView(R.id.planner_title) TextView tvTitle;

    private PlannerFragmentPagerAdapter adapter;
    private Trip openTrip;
    private Unbinder unbinder;
    private List<Trip> userTripsList = new ArrayList<>();
    private List<Trip> userDestinationList = new ArrayList<>();

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
        viewPager.setAdapter((PagerAdapter) adapter);
        adapter.setTripListFragment(TripFragment.newInstance());
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

    public void onLoadPage(PageTitle title, HistoryEntry entry) {
        startActivity(PageActivity.newIntentForNewTab(getContext(), entry, entry.getTitle()));
    }

    @Override
    public void onNewTrip() {
        CallbackTask.execute(() -> TripDbHelper.instance().createList(), new CallbackTask.DefaultCallback<Trip>(){
            @Override
            public void success(Trip trip) {
                userTripsList.add(trip);
                openTrip(trip.getId());
            }

            @Override
            public void failure(Throwable caught) {
                Toast.makeText(getActivity(), "Failed to create a new trip", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onOpenTrip(long id) {
        openTrip(id);
    }

    public Trip onGetTrip(long id) {
        return getTrip(id);
    }

    @Override
    public void onRequestTripListUpdate() {
        updateUserTripList();
    }

    @Override
    public void onRequestDestinationListUpdate() {
        updateUserDestinationList();
    }


    @Override
    public void onDeleteTrip(long id) {
        CallbackTask.execute(() -> TripDbHelper.instance().deleteList(getTrip(id)), new CallbackTask.DefaultCallback<Object>() {
            @Override
            public void success(Object result) {
                updateUserTripList();
            }

            @Override
            public void failure(Throwable caught) {
                Toast.makeText(getActivity(), "Failed to delete the trip", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onShareTrip(int index) {
        Context shareTripContext = getContext();
        Intent intent = ShareCompat.IntentBuilder.from((Activity) shareTripContext)
                .setType("text/plain")
                .setText("I will be travelling to " + userTripsList.get(index).getDestination().getDestinationName() + " on " + userTripsList.get(index).getTripDepartureDate().toString())
                .getIntent();
        startActivity(intent, null);
    }

    @Override
    public void onPlaceSelected(Place destination) {
        openTrip.setDestinationName((String) destination.getAddress());
        saveDestination();
    }


    @Override
    public void onDateChanged(int year, int month, int day) {
        openTrip.setTripDepartureDate(year, month, day);
    }

    //saves landmarks into trips, called in onclick
    public void onSave(List<LandmarkCard> saveList) {
        for (LandmarkCard card : saveList) {
            openTrip.getDestination().getDestinationPlacesToVisit().add(card);
        }
    }

    @Override
    public Date onRequestOpenDate() {
        return openTrip.getTripDepartureDate();
    }

    @Override
    public String onRequestOpenDestinationName() {
        return openTrip.getDestination().getDestinationName();
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
    private void saveTrip(Trip trip) {
        bSave.setActivated(false);
        CallbackTask.execute(() -> TripDbHelper.instance().updateList(trip),  new CallbackTask.DefaultCallback<Object>(){
            @Override
            public void success(Object o) {
                super.success(o);
                Toast.makeText(getActivity(),"Trip has been saved", Toast.LENGTH_SHORT).show();
                //There is no need to go the lists page automatically
                //goToHomePage();
            }

            @Override
            public void failure(Throwable caught) {
                super.failure(caught);
                bSave.setActivated(true);
            }
        });
    }

    private void saveDestination() {
        CallbackTask.execute(() -> DestinationDbHelper.getInstance().createList(openTrip.getDestination()),  new CallbackTask.DefaultCallback<Object>(){
            @Override
            public void success(Object o) {
                super.success(o);
                userDestinationList.add(openTrip);
                Toast.makeText(getActivity(),"Destination has been saved", Toast.LENGTH_SHORT).show();
                //There is no need to go the lists page automatically
                //goToHomePage();
            }

            @Override
            public void failure(Throwable caught) {
                super.failure(caught);
            }
        });
    }

    public void updateUserTripList() {
        CallbackTask.execute(() -> TripDbHelper.instance().getAllLists(), new CallbackTask.DefaultCallback<List<Trip>>() {
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

    public void updateUserDestinationList() {
        CallbackTask.execute(() -> DestinationDbHelper.getInstance().getAllLists(), new CallbackTask.DefaultCallback<List<Trip>>() {
            @Override
            public void success(List<Trip> list) {
                if (getActivity() == null) {
                    return;
                }
                userDestinationList = list;
                adapter.getDestinationListFragment().setUserDestinationList(list);
            }
        });
    }

    public void openTrip(long id) {
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
            adapter.destroyTripPages();
        }
    }

    private void goToHomePage() {
        goToPage(0);
        bSave.setActivated(true);
        //TODO: Prompt user to save unsaved changes
        openTrip = null;
    }

    private void nextPage() {
        goToPage(getCurrentPage()+1);
    }

    private void prevPage() {
        goToPage(getCurrentPage()-1);
    }

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
            // Hide all buttons on the first page
            bNext.setVisibility(View.INVISIBLE);
            bSave.setVisibility(View.INVISIBLE);
        } else if(getLastPage() == page && page > 2) {
            // Show only the save button on the last page
            bSave.setVisibility(View.VISIBLE);
            bNext.setVisibility(View.INVISIBLE);
        } else {
            // Show only the next button on any other page
            bNext.setVisibility(View.VISIBLE);
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
    @Nullable
    private Callback getCallback() {
        return FragmentUtil.getCallback(this, Callback.class);
    }
}