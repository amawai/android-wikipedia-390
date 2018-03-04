package org.wikipedia.travel.planner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Artem on 2018-03-02.
 */

public class PlannerFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> pages = new ArrayList<Fragment>(); //TODO: Should this be here?

    public PlannerFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
