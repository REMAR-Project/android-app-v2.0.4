package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by will on 14/05/16.
 */
public class DateRangeSelect extends Expander {

    CaldroidFragment caldroidFragment = new CaldroidFragment();
    Date selectedDate;
    View previousView;

    public DateRangeSelect(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void expandLayout(JSONObject question) throws JSONException {
        activity.setContentView(R.layout.expander_date_range);

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);

        imageView.setImageDrawable(getDrawable(question.getString("questionPicture")));
        titleView.setText(question.getString("questionTitle"));

        // Build calendar

        final CaldroidListener caldroidListener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                // TODO remove previously selected highlighting
                if(selectedDate!=null) {
                    if(previousView!=null) {
                        previousView.setBackgroundResource(R.color.caldroid_white);
                    }
                }
                view.setBackgroundResource(R.color.questionSelectedBackground);
                selectedDate = date;
                previousView = view;
            }
        };

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, false);
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, false);
        caldroidFragment.setArguments(args);
        caldroidFragment.setCaldroidListener(caldroidListener);

        // Disable out of range
        List<Date> validDates = getValidDates(question);
        caldroidFragment.setMinDate(getMinDate(validDates));
        caldroidFragment.setMaxDate(getMaxDate(validDates));

        FragmentTransaction t = activity.getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_layout, caldroidFragment);
        t.commit();
    }

    @Override
    protected void setPreviousAnswer(String answer) {
        if(answer.equals("")) {
            //Return early if no previous answer
            return;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            Date date = simpleDateFormat.parse(answer);
            selectedDate = date;
            caldroidFragment.setSelectedDate(date);
            Drawable drawable = activity.getResources().getDrawable(R.drawable.caldroid_cell_previously_selected);
            caldroidFragment.setBackgroundDrawableForDate(drawable, date);
        } catch (ParseException e) {
            Log.e("DateRange", "Unable to create date from answer " + Log.getStackTraceString(e));
            return;
        }
    }

    @Override
    public String getSelectedAnswer() {
        if(selectedDate!=null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            return simpleDateFormat.format(selectedDate);
        }
        return "";
    }

    private List<Date> getValidDates(JSONObject question) {
        List<Date> lst = new LinkedList<Date>();
        try {
            String previousAnswer = getAnswer(question.getInt("filterOnQuestion"));
            if(previousAnswer.equals("")) {
                return lst;
            }
            for(String s : previousAnswer.split(";")) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
                Date date = simpleDateFormat.parse(s);
                lst.add(date);
            }
        } catch (IOException|JSONException|ParseException e) {
            e.printStackTrace();
        }
        return lst;
    }

    private Date getMinDate(List<Date> dates) {
        Date min = null;
        for(Date d : dates) {
            if(min==null || d.before(min)) {
                min = d;
            }
        }
        return min;
    }

    private Date getMaxDate(List<Date> dates) {
        Date max = null;
        for(Date d : dates) {
            if(max==null || d.after(max)) {
                max = d;
            }
        }
        return max;
    }
}
