package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by will on 14/05/16.
 */
public class DateRange extends Expander {

    private List<Date> selectedDates = new LinkedList<Date>();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    public DateRange(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void expandLayout(JSONObject question) throws JSONException {
        activity.setContentView(R.layout.activity_date_range);

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);

        imageView.setImageDrawable(getDrawable(question.getString("questionPicture")));
        titleView.setText(question.getString("questionTitle"));

        // Build calendar

        CaldroidListener caldroidListener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                if(selectedDates.contains(date)) {
                    selectedDates.remove(date);
                    view.setBackgroundResource(R.color.caldroid_white);
                } else {
                    selectedDates.add(date);
                    view.setBackgroundResource(R.color.questionSelectedBackground);
                }
            }
        };

        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, false);
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, false);
        caldroidFragment.setArguments(args);
        caldroidFragment.setCaldroidListener(caldroidListener);

        FragmentTransaction t = activity.getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_layout, caldroidFragment);
        t.commit();
    }

    @Override
    protected void setPreviousAnswer(String answer) {
        for (String s : answer.split(";")) {
            try {
                Date date = simpleDateFormat.parse(answer);
                selectedDates.add(date);
            } catch (ParseException e) {
                Log.e("DateRange", "Unable to create date from answer " + Log.getStackTraceString(e));
                return;
            }
        }
    }

    @Override
    public String getSelectedAnswer() {
        StringBuilder sb = new StringBuilder();
        for (Date date: selectedDates) {
            String dateString = simpleDateFormat.format(date);
            sb.append(dateString);
            sb.append(';');
        }
        // Remove trailing semicolon
        if(sb.length()>0)
            sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
