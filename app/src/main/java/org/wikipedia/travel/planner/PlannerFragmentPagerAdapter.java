package org.wikipedia.travel.planner;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.wikipedia.travel.database.Trip;
import org.wikipedia.travel.planner.date.TravelDatePickerFragment;
import org.wikipedia.travel.planner.destination.DestinationFragment;
import org.wikipedia.travel.planner.places.PlacesFragment;
import org.wikipedia.travel.planner.trips.TripFragment;

import java.util.ArrayList;

/**
 * Created by Artem on 2018-03-02.
 */

public class PlannerFragmentPagerAdapter extends FragmentPagerAdapter {
    private Fragment tripListFragment;
    private ArrayList<Fragment> tripPages;

    public PlannerFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        tripListFragment = TripFragment.newInstance(new ArrayList<>());
        tripPages = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return tripListFragment;
        }
        return tripPages.get(position-1);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return tripPages != null ? tripPages.size() + 1 : 1;
    }

    public void addFragment(Fragment fragment) {
        tripPages.add(fragment);
        notifyDataSetChanged();
    }

    public void setTripListFragment(Fragment fragment) {
        tripListFragment = fragment;
    }

    public TripFragment getTripListFragment() {
        return (TripFragment)tripListFragment;
    }

    public void setupTripPages(Trip trip) {
        tripPages =  new ArrayList<>();
        tripPages.add(new DestinationFragment());
        if(trip.getDestination() != null) {
            tripPages.add(new TravelDatePickerFragment());
        }
        if(trip.getTripDepartureDate() != null) {
            tripPages.add(new PlacesFragment());
        }
        notifyDataSetChanged();
    }
}
