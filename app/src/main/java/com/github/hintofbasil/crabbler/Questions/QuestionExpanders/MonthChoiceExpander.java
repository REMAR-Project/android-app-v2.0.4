package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.ColorListAdapter;
import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

public class MonthChoiceExpander extends Expander {

    private static final int REQUIRED_ANSWERS = 2;
    public static final int MIN_YEAR = 2016;

    CheckBox choiceOneCheckBox;
    CheckBox choiceTwoCheckBox;
    ListView monthListView;
    ListView yearListView;
    ListView month2ListView;
    ListView year2ListView;

    int monthNo = -1;
    int yearNo = -1;

    ColorListAdapter<String> yearsAdapter;
    ColorListAdapter<String> years2Adapter;
    ColorListAdapter<String> monthsAdapter;
    ColorListAdapter<String> months2Adapter;

    int currentYear;
    int currentMonth;

    public MonthChoiceExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_month_choice);

        final ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        TextView questionView = (TextView) activity.findViewById(R.id.question_text);
        monthListView = (ListView) activity.findViewById(R.id.month_list_view);
        yearListView = (ListView) activity.findViewById(R.id.year_list_view);
        month2ListView = (ListView) activity.findViewById(R.id.month2_list_view);
        year2ListView = (ListView) activity.findViewById(R.id.year2_list_view);

        imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        titleView.setText(getRichTextQuestionString("questionTitle"));
        questionView.setText(getRichTextQuestionString("questionText"));

        currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        currentYear = Calendar.getInstance().get(Calendar.YEAR);


        monthListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if((position) > currentMonth)
                {
                    disableDisableNext();
                    return;
                }
                monthNo = position;
                month2ListView.setItemChecked(monthNo, true);
                // Colour is set as background on first selected.  Must override
                if(monthsAdapter != null) {
                    monthsAdapter.removeDefault();
                }
                enableDisableNext();
                yearListView.setVisibility(View.VISIBLE);
                year2ListView.setVisibility(View.GONE);
            }
        });

        month2ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(monthNo < currentMonth)
                {
                    monthListView.setItemChecked(monthNo, true);
                }
                else
                {
                    monthListView.setItemChecked(currentMonth, true);
                }
                monthNo = position;
                // Colour is set as background on first selected.  Must override
                if(months2Adapter != null) {
                    months2Adapter.removeDefault();
                }
                enableDisableNext();
                /*yearListView.setVisibility(View.VISIBLE);
                year2ListView.setVisibility(View.VISIBLE);

                if(yearNo >= currentYear-2016)
                {
                    year2ListView.setItemChecked(currentYear-2017, true);
                    yearNo = currentYear-2017;
                }
                else
                {
                    year2ListView.setItemChecked(yearNo, true);
                }*/

                if(monthNo <= currentMonth)
                {
                    yearListView.setVisibility(View.VISIBLE);
                    year2ListView.setVisibility(View.GONE);
                }
                else
                {
                    yearListView.setVisibility(View.GONE);
                    year2ListView.setVisibility(View.VISIBLE);
                }

            }
        });

        yearListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if((position + 2016) > currentYear) { //TODO remove magic number
                    disableDisableNext();
                    return;
                }
                yearNo = position;
                yearListView.setItemChecked(yearNo, true);
                year2ListView.setItemChecked(yearNo, true);
                view.setSelected(true);
                // Colour is set as background on first selected.  Must override
                if(yearsAdapter != null) {
                    yearsAdapter.removeDefault();
                }

                if(yearNo+2016 == currentYear)
                {
                    month2ListView.setVisibility(View.GONE);
                    monthListView.setVisibility(View.VISIBLE);
                    if(monthNo > currentMonth)
                    {
                        monthNo = currentMonth;
                    }
                    month2ListView.setItemChecked(monthNo, true);
                }
                else
                {
                    month2ListView.setVisibility(View.VISIBLE);
                    monthListView.setVisibility(View.GONE);
                    monthListView.setItemChecked(monthNo, true);
                }

                enableDisableNext();
            }
        });

        year2ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if((position + 2017) > currentYear) { //TODO remove magic number
                    disableDisableNext();
                    return;
                }
                yearNo = position;
                year2ListView.setItemChecked(yearNo, true);
                yearListView.setItemChecked(yearNo, true);
                view.setSelected(true);
                // Colour is set as background on first selected.  Must override
                if(years2Adapter != null) {
                    years2Adapter.removeDefault();
                }
                enableDisableNext();
            }
        });

        // Fix scrolling
        monthListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        yearListView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        month2ListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        year2ListView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        monthListView.setVisibility(View.GONE);
        year2ListView.setVisibility(View.GONE);

    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {
        /*try {
            switch (answer.getInt(0)) {
                case -1: // Indicates not filled in yet
                    break;
                default:
                    Log.d("TwoChoiceDate", "Invalid previous answer");
                    break;
            }
        } catch (JSONException e) {
            Log.i("TwoChoiceDateExpander", "Unable to parse answer (0)");
        }*/

        try {
            monthNo = answer.getInt(0);
        } catch (NumberFormatException|ArrayIndexOutOfBoundsException|JSONException e) {
            Log.d("TwoChoiceDate", "No previous month");
        }

        try {
            yearNo = answer.getInt(1);
        } catch (NumberFormatException|ArrayIndexOutOfBoundsException|JSONException e) {
            Log.d("TwoChoiceDate", "No previous year");
        }


        monthsAdapter = new ColorListAdapter<String>(
                    activity.getBaseContext(),
                    R.layout.list_background,
                    activity.getResources().getStringArray(R.array.months),
                    monthNo,
                    currentMonth);
        monthListView.setAdapter(monthsAdapter);

        months2Adapter = new ColorListAdapter<String>(
                    activity.getBaseContext(),
                    R.layout.list_background,
                    activity.getResources().getStringArray(R.array.months),
                    monthNo,
                    -1);
        month2ListView.setAdapter(months2Adapter);

        yearsAdapter = new ColorListAdapter<String>(
                activity.getBaseContext(),
                R.layout.list_background,
                activity.getResources().getStringArray(R.array.years),
                yearNo,
                currentYear - 2016); // 2016 due to min value in calendar
        yearListView.setAdapter(yearsAdapter);

        years2Adapter = new ColorListAdapter<String>(
                activity.getBaseContext(),
                R.layout.list_background,
                activity.getResources().getStringArray(R.array.years),
                yearNo,
                currentYear - 2017); // 2016 due to min value in calendar
        year2ListView.setAdapter(years2Adapter);
    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        array.put(monthNo);
        array.put(yearNo);
        return array;
    }
}
