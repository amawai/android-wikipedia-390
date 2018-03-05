package org.wikipedia.travel.planner;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import org.wikipedia.R;
import org.wikipedia.model.EnumCode;
import org.wikipedia.model.EnumCodeMap;

/**
 * Created by Artem on 2018-03-02.
 * Here we can store the different types of views. Enum serves as a factory.
 */

public enum PlannerView implements EnumCode {
    TRIPS(R.string.planner_view_trips) {
        public Fragment newInstance() {
            //Just as an example for now
            return null;
        }
    };

    private final int header;
    private static final EnumCodeMap<PlannerView> MAP = new EnumCodeMap<>(PlannerView.class);

    PlannerView(int header) {
        this.header = header;
    }

    public static PlannerView of(int code) {
        return MAP.get(code);
    }

    @Override
    public int code() {
        return ordinal();
    }

    @NonNull
    public abstract Fragment newInstance();
}