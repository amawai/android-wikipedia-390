package org.wikipedia.travel.planner;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Artem on 2018-03-02.
 */

public class PlannerFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> pages = new ArrayList<>();

    public PlannerFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    public void addFragment(Fragment fragment) {
        pages.add(fragment);
        notifyDataSetChanged();
    }
}
