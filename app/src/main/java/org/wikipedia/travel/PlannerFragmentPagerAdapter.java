package org.wikipedia.travel;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import org.wikipedia.travel.datepicker.DateFragment;
import org.wikipedia.travel.destinationpicker.DestinationFragment;
import org.wikipedia.travel.landmarkpicker.LandmarkFragment;
import org.wikipedia.travel.trip.Trip;
import org.wikipedia.travel.trip.TripFragment;

import java.util.ArrayList;
import java.util.Date;

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
        String destinationName = trip.getDestination().getDestinationName();
        Date departureDate = trip.getTripDepartureDate();
        tripPages =  new ArrayList<>();
        tripPages.add(DestinationFragment.newInstance(destinationName));
        if(trip.getDestination() != null) {
            tripPages.add(DateFragment.newInstance(departureDate.getYear(), departureDate.getMonth(), departureDate.getDay()));
        }
        if(trip.getTripDepartureDate() != null) {
            tripPages.add(LandmarkFragment.newInstance(destinationName));
        }
        notifyDataSetChanged();
    }

    public void destroyTripPages() {
        tripPages = null;
        notifyDataSetChanged();
    }
}