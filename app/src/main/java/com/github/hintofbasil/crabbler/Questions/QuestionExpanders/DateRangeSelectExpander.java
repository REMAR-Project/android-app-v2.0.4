package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

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
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by will on 14/05/16.
 */
public class DateRangeSelectExpander extends Expander {

    private static final int REQUIRED_ANSWERS = 1;

    CaldroidCustomFragment caldroidFragment = new CaldroidCustomFragment();
    Date selectedDate;

    public DateRangeSelectExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_date_range);

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        TextView descriptionView = (TextView) activity.findViewById(R.id.description);

        imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        titleView.setText(getQuestionString("questionTitle"));
        descriptionView.setText(getQuestionString("description"));

        // Build calendar
        final CaldroidListener caldroidListener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                selectedDate = date;
                view.setSelected(true);
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
        args.putInt(Keys.CALDROID_BACKGROUND_RESOURCE, R.drawable.caldroid_cell_date_select);
        caldroidFragment.setArguments(args);
        caldroidFragment.setCaldroidListener(caldroidListener);

        // Disable out of range
        List<Date> validDates = getValidDates(questionJson);
        caldroidFragment.setMinDate(getMinDate(validDates));
        caldroidFragment.setMaxDate(getMaxDate(validDates));

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
            selectedDate = date;
        } catch (ParseException|JSONException e) {
            Log.e("DateRangeExpander", "Unable to create date from answer " + Log.getStackTraceString(e));
            return;
        }
    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        if(selectedDate!=null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            array.put(simpleDateFormat.format(selectedDate));
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
