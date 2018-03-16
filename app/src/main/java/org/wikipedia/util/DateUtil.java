package org.wikipedia.util;

import android.content.Context;
import android.support.annotation.NonNull;

import org.wikipedia.R;
import org.wikipedia.WikipediaApp;
import org.wikipedia.feed.model.UtcDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public final class DateUtil {

    public static SimpleDateFormat getIso8601DateFormatShort() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat;
    }

    public static SimpleDateFormat getIso8601DateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat;
    }

    public static String getFeedCardDateString(int age) {
        return getFeedCardDateString(new UtcDate(age).baseCalendar());
    }

    public static String getFeedCardDateString(@NonNull Calendar date) {
        return getShortDateString(date.getTime());
    }

    public static String getFeedCardDateString(@NonNull Date date) {
        return getShortDateString(date);
    }

    public static String getFeedCardShortDateString(@NonNull Calendar date) {
        return getExtraShortDateString(date.getTime());
    }

    public static String getMonthOnlyDateString(@NonNull Date date) {
        return new SimpleDateFormat("MMMM d", Locale.getDefault()).format(date);
    }

    public static String getMonthOnlyWithoutDayDateString(@NonNull Date date) {
        return new SimpleDateFormat("MMMM", Locale.getDefault()).format(date);
    }

    private static String getExtraShortDateString(@NonNull Date date) {
        return new SimpleDateFormat("MMM d", Locale.getDefault()).format(date);
    }

    public static String getShortDateString(@NonNull Date date) {
        // todo: consider allowing TWN date formats. It would be useful to have but might be
        //       difficult for translators to write correct format specifiers without being able to
        //       test them. We should investigate localization support in date libraries such as
        //       Joda-Time and how TWN solves this classic problem.
        DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(WikipediaApp.getInstance());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    public static String getDateWithWeekday(@NonNull Date date) {
        return new SimpleDateFormat("EEE, d MMM yyyy").format(date);
    }

    public static UtcDate getUtcRequestDateFor(int age) {
        return new UtcDate(age);
    }

    public static Calendar getDefaultDateFor(int age) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.add(Calendar.DATE, -age);
        return calendar;
    }

    public static Date getHttpLastModifiedDate(@NonNull String dateStr) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ROOT);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.parse(dateStr);
    }

    @NonNull public static String yearToStringWithEra(int year) {
        Calendar cal = new GregorianCalendar(year, 1, 1);
        return new SimpleDateFormat(year < 0 ? "y GG" : "y", Locale.getDefault()).format(cal.getTime());
    }


    @NonNull public static String getYearDifferenceString(int year) {
        Context context = WikipediaApp.getInstance().getApplicationContext();
        int diffInYears = Calendar.getInstance().get(Calendar.YEAR) - year;
        if (diffInYears == 0) {
            return context.getString(R.string.this_year);
        } else {
            return context.getResources().getQuantityString(R.plurals.diff_years, diffInYears, diffInYears);
        }
    }

    private DateUtil() {
    }
}
