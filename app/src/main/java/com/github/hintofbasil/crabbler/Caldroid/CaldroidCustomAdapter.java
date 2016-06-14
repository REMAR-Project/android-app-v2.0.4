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

    public static final int[][] newMoonDates = new int[][]{
            new int[]{10,1},
            new int[]{8,2},
            new int[]{9,3},
            new int[]{7,4},
            new int[]{6,5},
            new int[]{5,6},
            new int[]{4,7},
            new int[]{2,8},
            new int[]{1,9},
            new int[]{1,10},
            new int[]{30,10},
            new int[]{29,11},
            new int[]{29,12}
    };

    public static final int[][] fullMoonDates = new int[][]{
            new int[]{24,1},
            new int[]{22,2},
            new int[]{23,3},
            new int[]{22,4},
            new int[]{21,5},
            new int[]{20,6},
            new int[]{19,7},
            new int[]{18,8},
            new int[]{16,9},
            new int[]{16,10},
            new int[]{14,11},
            new int[]{14,12}
    };

    public CaldroidCustomAdapter(Context context, int month, int year, Map<String, Object> caldroidData, Map<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
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
        }

        boolean shouldResetDiabledView = false;
        boolean shouldResetSelectedView = false;

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

            tv1.setTextColor(Color.BLACK);

        } else {
            shouldResetSelectedView = true;
        }

        if (shouldResetDiabledView && shouldResetSelectedView) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
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
            if (date[0] == dateTime.getDay() && date[1] == dateTime.getMonth()) {
                newMoon.setVisibility(View.VISIBLE);
            }
        }
        for(int[] date : fullMoonDates) {
            if (date[0] == dateTime.getDay() && date[1] == dateTime.getMonth()) {
                fullMoon.setVisibility(View.VISIBLE);
            }
        }

        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);

        // Set custom color if required
        setCustomResources(dateTime, cellView, tv1);


        cellView.setBackgroundResource(R.drawable.caldroid_cell_selected);
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
