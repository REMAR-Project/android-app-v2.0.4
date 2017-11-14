package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.Caldroid.CaldroidCustomFragment;
import com.github.hintofbasil.crabbler.Keys;
import com.github.hintofbasil.crabbler.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by will on 14/05/16.
 */
public class DateRangeSelectExpander extends Expander {

    private static final int REQUIRED_ANSWERS = 1;

    CaldroidCustomFragment caldroidFragment = new CaldroidCustomFragment();
    Date[] selectedDates;
    View[] selectedViews;

    List<Date> validDates = null;

    public DateRangeSelectExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_date_range);

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        TextView descriptionView = (TextView) activity.findViewById(R.id.description);

        //imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        //titleView.setText(getRichTextQuestionString("questionTitle"));
        descriptionView.setText(getRichTextQuestionString("description"));
        descriptionView.setTextColor(Color.RED);

        // Build calendar
        final CaldroidListener caldroidListener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {

                // Check date is valid
                if(!validDates.contains(date)) {
                    return;
                }
                //int block = getDateBlock(date);
                int block = 0;
                if(selectedDates[block] != null)
                {
                    caldroidFragment.setBackgroundDrawableForDate(activity.getResources().getDrawable(R.drawable.caldroid_cell_green), selectedDates[0]);
                }
                Log.d("daterangeselect","block " +block);
                if(date.equals(selectedDates[block])) {
                    try
                    {
                        selectedViews[block].setBackgroundResource(R.color.questionBackground);
                    } catch (Exception e)
                    {
                        caldroidFragment.setBackgroundDrawableForDate(activity.getResources().getDrawable(R.drawable.caldroid_cell_green), selectedDates[0]);
                        caldroidFragment.refreshView();
                    }
                    selectedDates[block] = null;

                    Log.d("DateRangeSelect", "test1");
                } else {
                    selectedDates[block] = date;
                    if(selectedViews[block] != null) {
                        selectedViews[block].setBackgroundResource(R.color.questionBackground);
                        Log.d("DateRangeSelect", "test2");
                    }
                    selectedViews[block] = view;
                    view.setBackgroundResource(R.color.questionSelectedBackground);
                    caldroidFragment.setBackgroundDrawableForDate(activity.getResources().getDrawable(R.color.questionSelectedBackground), selectedDates[0]);
                    Log.d("DateRangeSelect", "test3");
                    caldroidFragment.refreshView();
                }
                enableDisableNext();
            }
        };

        Bundle args = new Bundle();
        if (questionJson.has("month")) {
            int month = Integer.parseInt(getQuestionString("month"));
            args.putInt(CaldroidFragment.MONTH, month + 1); // + 1 because starts counting from 1
        }
        if (questionJson.has("year")) {
            int year = Integer.parseInt(getQuestionString("year"));
            args.putInt(CaldroidFragment.YEAR, TwoChoiceDateExpander.MIN_YEAR + year);
        }
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, false);
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, false);
        //args.putInt(Keys.CALDROID_BACKGROUND_RESOURCE, R.drawable.disable_cell);
        caldroidFragment.setArguments(args);
        caldroidFragment.setCaldroidListener(caldroidListener);

        // Disable invalid
        validDates = getValidDates(questionJson);
        Drawable validDateDrawable = activity.getResources().getDrawable(R.drawable.caldroid_cell_green);
        for(Date date : validDates) {
            caldroidFragment.setBackgroundDrawableForDate(validDateDrawable, date);
        }
        int blocks = getDateBlock(getMaxDate(validDates));
        selectedDates = new Date[blocks + 1];
        selectedViews = new View[blocks + 1];

        FragmentTransaction t = activity.getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_layout, caldroidFragment);
        t.commit();
    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {
        try {
            String s = (String) answer.get(0);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            Date date = simpleDateFormat.parse(s);
            selectedDates[0] = date;
            caldroidFragment.setBackgroundDrawableForDate(activity.getResources().getDrawable(R.color.questionSelectedBackground), selectedDates[0]);
        } catch (ParseException|JSONException e) {
            Log.e("DateRangeExpander", "Unable to create date from answer " + Log.getStackTraceString(e));
            return;
        }
    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        for(Date d : selectedDates) {
            if(d != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
                array.put(simpleDateFormat.format(d));
            }
        }
        return array;
    }

    private List<Date> getValidDates(JSONObject question) {
        List<Date> lst = new LinkedList<Date>();
        try {
            int filterOn = question.getInt("filterOnQuestion");
            if(filterOn<0) { // Allow referencing previous questions
                filterOn = realQuestionId + filterOn; // Plus because negative value given
            }
            JSONArray previousAnswer = questionManager.getAnswer(filterOn);
            for (int i=0; i<previousAnswer.length(); i++) {
                String s = previousAnswer.getString(i);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
                Date date = simpleDateFormat.parse(s);
                lst.add(date);
            }
        } catch (JSONException|ParseException|IOException e) {
            Log.d("DateRangeSelectExpander", "Unable to parse previous answer");
        }
        Collections.sort(lst);
        return lst;
    }

    private Date getMaxDate(List<Date> dates) {
        Date max = null;
        for (Date d : dates) {
            if (max == null || d.after(max)) {
                max = d;
            }
        }
        return max;
    }


    private int getDateBlock(Date date) {
        int i = 0;
        Date previous = null;
        for(Date d : validDates) {
            if(previous != null) {
                long days = getDifferenceDays(d, previous);
                if(days > 1) {
                    i++;
                }
            }
            if(date.equals(d)) {
                return i;
            }
            previous = d;
        }
        return -1;
    }

    private long getDifferenceDays(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
