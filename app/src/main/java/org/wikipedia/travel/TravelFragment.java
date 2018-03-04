package org.wikipedia.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.wikipedia.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Artem on 2018-02-26.
 */

public class TravelFragment extends Fragment implements View.OnClickListener {
    private Unbinder unbinder;
    private FloatingActionButton nextButton;

    private Button dNextButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_travel_planner, container, false);

        dNextButton = (Button) view.findViewById(R.id.dNext);//currently the button on the 1st travel planner activity
        dNextButton.setOnClickListener(this);

        unbinder = ButterKnife.bind(this, view);
        getAppCompatActivity().getSupportActionBar().setTitle(getString(R.string.view_travel_card_title));

        return view;
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    @Override
    public void onClick(View v){
        Intent i = new Intent(getActivity(),PlacesToVisitActivity.class);
        startActivity(i);
        //getContext().startActivity(new Intent(PlacesToVisitActivity.newIntent(getContext())));
    }

}
