package org.wikipedia.travel;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import org.wikipedia.travel.datepicker.DateFragment;
import org.wikipedia.travel.destinationpicker.DestinationFragment;
import org.wikipedia.travel.trip.Trip;
import org.wikipedia.travel.trip.TripFragment;

import java.util.ArrayList;

/**
 * Created by Artem on 2018-03-02.
 */

public class PlannerFragmentPagerAdapter extends FragmentPagerAdapter {
    private Fragment tripListFragment;
    private ArrayList<Fragment> tripPages;

    public PlannerFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        tripListFragment = TripFragment.newInstance();
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
        tripPages.add(DestinationFragment.newInstance(trip.getDestination().getDestinationName())); //TODO: Populate destination
        if(trip.getDestination() != null) {
            tripPages.add(DateFragment.newInstance());
        }
        if(trip.getTripDepartureDate() != null) {
        //    tripPages.add(LandmarkFragment.newInstance());
        }
        notifyDataSetChanged();
    }

    public void destroyTripPages() {
        tripPages = null;
        notifyDataSetChanged();
    }
}