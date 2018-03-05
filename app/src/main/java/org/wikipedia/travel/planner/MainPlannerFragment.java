package org.wikipedia.travel.planner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import org.wikipedia.BackPressedHandler;
import org.wikipedia.R;
import org.wikipedia.travel.planner.PlannerFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Artem on 2018-02-26.
 */

public class MainPlannerFragment extends Fragment implements BackPressedHandler{
    @BindView(R.id.fragment_travel_planner_view_pager) ViewPager viewPager;
    @BindView(R.id.planner_next) Button nextButton;
    // This should hold a user's info, I suppose

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_travel_planner, container, false);
        unbinder = ButterKnife.bind(this, view);
        getAppCompatActivity().getSupportActionBar().setTitle(getString(R.string.view_travel_card_title));
        PlannerFragmentPagerAdapter adapter = new PlannerFragmentPagerAdapter(getChildFragmentManager());
        adapter.addFragment(TripsFragment.newInstance());
        adapter.addFragment(DateFragment.newInstance());
        viewPager.setAdapter((PagerAdapter) adapter);

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
}
