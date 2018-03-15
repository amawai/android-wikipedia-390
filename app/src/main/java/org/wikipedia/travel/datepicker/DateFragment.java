package org.wikipedia.travel.datepicker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import org.wikipedia.R;
import org.wikipedia.activity.FragmentUtil;

import java.text.DateFormatSymbols;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DateFragment extends Fragment{

    private Unbinder unbinder;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @BindView(R.id.date_button_select) Button selectButton;
    @BindView(R.id.selected_date_view_text) TextView mDisplayDate;
    DatePickerDialog mDatePicker;

    public interface Callback {
        public void onDateChanged(int year, int month, int day);
    }

    public static DateFragment newInstance(int year, int month, int day) {
        Bundle args = new Bundle();
        args.putInt("YEAR", year);
        args.putInt("MONTH", month);
        args.putInt("DAY", day);

        DateFragment fragment = new DateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_travel_date_picker, container, false);

        unbinder = ButterKnife.bind(this, view);
        int year = getArguments().getInt("YEAR");
        int month = getArguments().getInt("MONTH");
        int day = getArguments().getInt("DAY");

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = getMonth(month) + " " + day + ", " + year;
                mDisplayDate.setText(date);
                if(getCallback() != null) {
                    getCallback().onDateChanged(year, month, day);
                }
            }
        };

        //setting up the select date window
        mDatePicker = new DatePickerDialog(this.getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day);
        mDatePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        selectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePicker.show();
            }
        });

        //setting the current date as the departure date
        String date = getMonth(month) + " " + day + ", " + year;
        mDisplayDate.setText(date);

        return view;
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    public Callback getCallback() { return FragmentUtil.getCallback(this, Callback.class); }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

}