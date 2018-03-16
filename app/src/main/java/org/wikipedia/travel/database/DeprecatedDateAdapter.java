package org.wikipedia.travel.database;

import org.wikipedia.util.DateUtil;

import java.util.Date;

/**
 * Created by Artem on 2018-03-14.
 */

public class DeprecatedDateAdapter extends Date {

    public DeprecatedDateAdapter(int year, int month, int date) {
        this.setYear(year);
        this.setMonth(month);
        this.setDate(date);
    }

    public DeprecatedDateAdapter(long longDate) {
        super(longDate);
    }

    public DeprecatedDateAdapter() {
        super();
    }

    @Override
    public int getYear() {
        return super.getYear()+1900;
    }

    @Override
    public void setYear(int year) {
        super.setYear(year-1900);
    }
}
