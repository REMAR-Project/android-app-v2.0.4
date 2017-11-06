package com.github.hintofbasil.crabbler.Caldroid;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.Map;

import hirondelle.date4j.DateTime;

/**
 * Created by will on 13/06/16.
 */
public class CaldroidCustomAdapter extends CaldroidGridAdapter {

    // Dates taken from http://www.calendario-365.com.br/lua/fases-da-lua.html
    public static final int[][] newMoonDates = new int[][]{
            new int[]{9,1,2016},
            new int[]{8,2,2016},
            new int[]{8,3,2016},
            new int[]{7,4,2016},
            new int[]{6,5,2016},
            new int[]{5,6,2016},
            new int[]{4,7,2016},
            new int[]{2,8,2016},
            new int[]{1,9,2016},
            new int[]{1,10,2016},
            new int[]{30,10,2016},
            new int[]{30,11,2016},
            new int[]{29,12,2016},
            new int[]{27,1,2017},
            new int[]{26,2,2017},
            new int[]{27,3,2017},
            new int[]{26,4,2017},
            new int[]{25,5,2017},
            new int[]{23,6,2017},
            new int[]{23,7,2017},
            new int[]{21,8,2017},
            new int[]{20,9,2017},
            new int[]{19,10,2017},
            new int[]{18,11,2017},
            new int[]{18,12,2017},
            new int[]{17,1,2018},
            new int[]{15,2,2018},
            new int[]{17,3,2018},
            new int[]{15,4,2018},
            new int[]{15,5,2018},
            new int[]{13,6,2018},
            new int[]{12,7,2018},
            new int[]{11,8,2018},
            new int[]{9,9,2018},
            new int[]{9,10,2018},
            new int[]{7,11,2018},
            new int[]{7,12,2018}
    };

    public static final int[][] fullMoonDates = new int[][]{
            new int[]{23,1,2016},
            new int[]{22,2,2016},
            new int[]{23,3,2016},
            new int[]{22,4,2016},
            new int[]{21,5,2016},
            new int[]{20,6,2016},
            new int[]{19,7,2016},
            new int[]{18,8,2016},
            new int[]{16,9,2016},
            new int[]{16,10,2016},
            new int[]{14,11,2016},
            new int[]{13,12,2016},
            new int[]{12,1,2017},
            new int[]{10,2,2017},
            new int[]{12,3,2017},
            new int[]{11,4,2017},
            new int[]{10,5,2017},
            new int[]{9,6,2017},
            new int[]{9,7,2017},
            new int[]{7,8,2017},
            new int[]{6,9,2017},
            new int[]{5,10,2017},
            new int[]{4,11,2017},
            new int[]{3,12,2017},
            new int[]{2,1,2018},
            new int[]{31,1,2018},
            new int[]{1,3,2018},
            new int[]{31,3,2018},
            new int[]{29,4,2018},
            new int[]{29,5,2018},
            new int[]{28,6,2018},
            new int[]{27,7,2018},
            new int[]{26,8,2018},
            new int[]{24,9,2018},
            new int[]{24,10,2018},
            new int[]{23,11,2018},
            new int[]{22,12,2018}
    };

    private int resourceId;

    public CaldroidCustomAdapter(Context context,
                                 int month,
                                 int year,
                                 Map<String, Object> caldroidData,
                                 Map<String, Object> extraData,
                                 int resourceId) {
        super(context, month, year, caldroidData, extraData);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;

        // For reuse
        if (convertView == null) {
            cellView = inflater.inflate(R.layout.custom_caldroid_cell, null);
        }

        TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);
        View fullMoon = (View) cellView.findViewById(R.id.fullMoon);
        View newMoon = (View) cellView.findViewById(R.id.newMoon);

        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);
        Resources resources = context.getResources();

        // Set color of the dates in previous / next month

        if (dateTime.getMonth() != month) {
            tv1.setTextColor(resources
                    .getColor(com.caldroid.R.color.caldroid_darker_gray));
        } else {
            tv1.setTextColor(Color.BLACK);
        }

        boolean shouldResetDiabledView = false;

        // Customize for disabled dates and date outside min/max dates
        if ((minDateTime != null && dateTime.lt(minDateTime))
                || (maxDateTime != null && dateTime.gt(maxDateTime))
                || (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

            tv1.setTextColor(CaldroidFragment.disabledTextColor);
            if (CaldroidFragment.disabledBackgroundDrawable == -1) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
            } else {
                cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
            }

        } else {
            shouldResetDiabledView = true;
        }

        // Customize for selected dates
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
            cellView.setActivated(true);

        }

        if (shouldResetDiabledView) {
                cellView.setBackgroundResource(resourceId);
        }

        tv1.setText("" + dateTime.getDay());

        // Enable moon circles
        // TODO needs to be more accurate
        /*int phase = getMoonPhase(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay());
        Log.i("CaldroidCustomAdapter", "Moon phase: " + phase);
        if (phase == 0) {
            newMoon.setVisibility(View.VISIBLE);
        } else if (phase == 4) {
            fullMoon.setVisibility(View.VISIBLE);
        }*/

        for(int[] date : newMoonDates) {
            if (date[0] == dateTime.getDay() && date[1] == dateTime.getMonth() && date[2] == dateTime.getYear()) {
                newMoon.setVisibility(View.VISIBLE);
            }
        }
        for(int[] date : fullMoonDates) {
            if (date[0] == dateTime.getDay() && date[1] == dateTime.getMonth() && date[2] == dateTime.getYear()) {
                fullMoon.setVisibility(View.VISIBLE);
            }
        }

        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);

        // Set custom color if required
        setCustomResources(dateTime, cellView, tv1);

        return cellView;
    }

    private int getMoonPhase(int year, int month, int day) {
        int g, e;

        if (month == 1) {
            day--;
        } else if (month == 2) {
            day += 30;
        } else {
            day += 28 + (month -2) * 3059 / 100;

            // adjust for leap years
            if ((year & 3) == 0) {
                day++;
            }
            if ((year % 100) == 0) {
                day--;
            }
        }

        g = (year - 1900) % 19 + 1;
        e = (11 * g + 18) % 30;
        if ((e == 25 && g > 11) || e == 24) {
            e++;
        }
        return ((((e + day) * 6 + 11) % 177) / 22 & 7);
    }
}
