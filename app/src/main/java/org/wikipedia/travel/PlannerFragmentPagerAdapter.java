package org.wikipedia.travel;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;


import org.wikipedia.travel.datepicker.DateFragment;
import org.wikipedia.travel.destinationpicker.DestinationFragment;
import org.wikipedia.travel.landmarkpicker.LandmarkFragment;
import org.wikipedia.travel.trip.Trip;
import org.wikipedia.travel.trip.TripFragment;

import java.util.Date;

/**
 * Created by Artem on 2018-03-02.
 */

public class PlannerFragmentPagerAdapter extends FragmentStatePagerAdapter {
    FragmentManager fm;
    private TripFragment tripListFragment;
    private DestinationFragment destinationFragment;
    private DateFragment dateFragment;
    private LandmarkFragment landmarksFragment;
    private int numPages;

    public PlannerFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
        setTripListFragment(TripFragment.newInstance());
        numPages = 1;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return tripListFragment;
            case 1:
                return destinationFragment;
            case 2:
                return dateFragment;
            case 3:
                return landmarksFragment;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return numPages;
    }

    public void setTripListFragment(TripFragment fragment) {
        tripListFragment = fragment;
        notifyDataSetChanged();
    }

    public TripFragment getTripListFragment() {
        return (TripFragment)tripListFragment;
    }

    public DestinationFragment getDestinationListFragment() {
        return destinationFragment;
    }

    public void addDestinationFragment(String destination) {
        destinationFragment = DestinationFragment.newInstance(destination);
        numPages++;
        notifyDataSetChanged();
    }

    public void destroyDestinationFragment() {
        destinationFragment = null;
        numPages--;
        notifyDataSetChanged();
    }

    public void addDateFragment(Trip trip) {
        Date departureDate = trip.getTripDepartureDate();
        if(departureDate == null) {
            return;
        }
        dateFragment = DateFragment.newInstance(departureDate.getYear(), departureDate.getMonth(), departureDate.getDate());
        numPages++;
        notifyDataSetChanged();
    }

    public void destroyDateFragment() {
        dateFragment = null;
        numPages--;
        notifyDataSetChanged();
    }

    private void addLandmarksFragment(String destinationName) {
        landmarksFragment = LandmarkFragment.newInstance(destinationName);
        numPages++;
        notifyDataSetChanged();
    }

    public void destroyLandmarksFragment() {
        landmarksFragment = null;
        numPages--;
        notifyDataSetChanged();
    }

    public void setupTripPages(Trip trip) {
        String destinationName = trip.getDestination().getDestinationName();
        addDestinationFragment(destinationName);
        if(trip.getDestination() != null) {
            addDateFragment(trip);
        }
        if(trip.getTripDepartureDate() != null) {
            addLandmarksFragment(destinationName);
        }
    }

    public void destroyTripPages() {
        destroyLandmarksFragment();
        destroyDateFragment();
        destroyDestinationFragment();
    }
}