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

import java.util.List;
import java.util.Map;

import hirondelle.date4j.DateTime;

/**
 * Created by will on 13/06/16.
 */
public class CaldroidCustomAdapter extends CaldroidGridAdapter {

    // Dates taken from http://www.calendario-365.com.br/lua/fases-da-lua.html

    private int resourceId;
    private List<int[]> newMoons;
    private List<int[]> fullMoons;

    public CaldroidCustomAdapter(Context context,
                                 int month,
                                 int year,
                                 Map<String, Object> caldroidData,
                                 Map<String, Object> extraData,
                                 int resourceId, List<int[]> newMoons, List<int[]> fullMoon) {
        super(context, month, year, caldroidData, extraData);
        this.resourceId = resourceId;
        this.newMoons = newMoons;
        this.fullMoons = fullMoon;
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

        Float density = context.getResources().getDisplayMetrics().density;

        /* New Moon Date */
        for(int[] date : newMoons) {
            if (date[0] == dateTime.getDay() && date[1] == dateTime.getMonth() && date[2] == dateTime.getYear()) {
                    newMoon.setVisibility(View.VISIBLE);
            }
        }

        /* Full Moon Date */

        for(int[] date : fullMoons) {
            if (date[0] == dateTime.getDay() && date[1] == dateTime.getMonth() && date[2] == dateTime.getYear()) {
                fullMoon.setVisibility(View.VISIBLE);
            }
        }

        // Change moon size on smaller screens, to prevent dates to display weird
        if (density < 2.5)
        {
            tv1.setTextSize(9);
            ViewGroup.LayoutParams layoutParams = fullMoon.getLayoutParams();
            layoutParams.width = 7; layoutParams.height=7;
            fullMoon.setLayoutParams(layoutParams);
            newMoon.setLayoutParams(layoutParams);
            cellView.setPadding(0, 0, 0, 0);
        }

        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);

        // Set custom color if required
        setCustomResources(dateTime, cellView, tv1);

        return cellView;
    }

}
