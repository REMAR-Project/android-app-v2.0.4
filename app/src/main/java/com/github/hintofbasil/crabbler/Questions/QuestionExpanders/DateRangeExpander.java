package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;
import com.github.hintofbasil.crabbler.Caldroid.CaldroidCustomFragment;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
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
public class DateRangeExpander extends Expander {

    private static final int REQUIRED_ANSWERS = 1;

    private List<Date> selectedDates = new LinkedList<Date>();
    CaldroidCustomFragment caldroidFragment = new CaldroidCustomFragment();

    int month;
    int year;

    public DateRangeExpander(AppCompatActivity activity, JSONObject questionJson) {
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

        // Build calendar

        final CaldroidListener caldroidListener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                if(view.isActivated()) {
                    selectedDates.remove(date);
                    view.setActivated(false);
                } else {
                    selectedDates.add(date);
                    view.setActivated(true);
                }
                enableDisableNext();
            }
        };

        Bundle args = new Bundle();
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, false);
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, false);
        if (questionJson.has("month")) {
            month = Integer.parseInt(getQuestionString("month"));
            args.putInt(CaldroidFragment.MONTH, month + 1); // + 1 because starts counting from 1
        }
        if (questionJson.has("year")) {
            year = Integer.parseInt(getQuestionString("year"));
            args.putInt(CaldroidFragment.YEAR, TwoChoiceDateExpander.MIN_YEAR + year);
        }
        caldroidFragment.setArguments(args);
        caldroidFragment.setCaldroidListener(caldroidListener);

        FragmentTransaction t = activity.getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_layout, caldroidFragment);
        t.commit();
    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {
        Log.d("DateRangeExpander", answer.toString());
        for (int i=0; i<answer.length(); i++) {
            try {
                String s = (String) answer.get(i);
                // Requires new SimpleDateFormat for each parse.
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = simpleDateFormat.parse(s);
                int dateMonth =  Integer.parseInt((String) DateFormat.format("MM",  date));
                int dateYear = Integer.parseInt((String) DateFormat.format("yyyy",  date));
                selectedDates.add(date);
                caldroidFragment.setSelectedDate(date);
            } catch (ParseException|JSONException e) {
                Log.e("DateRangeExpander", "Unable to create date from answer " + Log.getStackTraceString(e));
                return;
            }
        }
    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (Date date: selectedDates) {
            String dateString = simpleDateFormat.format(date);
            array.put(dateString);
        }
        return array;
    }
}
