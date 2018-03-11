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
import org.wikipedia.travel.landmarkpicker.LandmarkActivity;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DateFragment extends Fragment implements OnClickListener {

    private Unbinder unbinder;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Calendar cal = Calendar.getInstance();
    private int year = cal.get(Calendar.YEAR);
    private int month = cal.get(Calendar.MONTH) + 1;
    private int day = cal.get(Calendar.DAY_OF_MONTH);

    @BindView(R.id.date_button_select) Button selectButton;
    @BindView(R.id.date_button_next) FloatingActionButton nextButton;
    @BindView(R.id.selected_date_view_text) TextView mDisplayDate;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_travel_date_picker, container, false);

        unbinder = ButterKnife.bind(this, view);

        selectButton.setOnClickListener((OnClickListener) this);
        nextButton.setOnClickListener((OnClickListener) this);

        //setting the current date as the departure date
        String date = getMonth(month) + " " + day + ", " + year;
        mDisplayDate.setText(date);

        getAppCompatActivity().getSupportActionBar().setTitle("Departure Date");

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.date_button_next:
                getContext().startActivity(new Intent(LandmarkActivity.newIntent(getContext())));
                break;
            case R.id.date_button_select:
                dateSelector();
                break;

        }
    }

    public void dateSelector() {

            DatePickerDialog dialog;

            //setting onclicklistener to change the selected date after button selection
            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    month = month + 1;
                    String date = getMonth(month) + " " + day + ", " + year;
                    mDisplayDate.setText(date);
                }
            };

            //setting up the select date window
            dialog = new DatePickerDialog(this.getActivity(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener,
                    year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    private AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

}