package org.wikipedia.travel.database;

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

    @Override
    public int getYear() {
        return super.getYear()+1900;
    }

    @Override
    public void setYear(int year) {
        super.setYear(year-1900);
    }

    @Override
    public int getMonth() {
        return super.getMonth()+1;
    }

    @Override
    public void setMonth(int month) {
        super.setMonth(month-1);
    }
}
