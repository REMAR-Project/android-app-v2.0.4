package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class TwoChoiceDateExpander extends Expander {

    CheckBox choiceOneCheckBox;
    CheckBox choiceTwoCheckBox;
    ListView monthListView;
    ListView yearListView;

    int monthNo = -1;
    int yearNo = -1;

    ColorListAdapter<String> yearsAdapter;
    ColorListAdapter<String> monthsAdapter;

    public TwoChoiceDateExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_two_choice_date);

        final ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        choiceOneCheckBox = (CheckBox) activity.findViewById(R.id.choice_one);
        choiceTwoCheckBox = (CheckBox) activity.findViewById(R.id.choice_two);
        monthListView = (ListView) activity.findViewById(R.id.month_list_view);
        yearListView = (ListView) activity.findViewById(R.id.year_list_view);

        imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        titleView.setText(getQuestionString("questionTitle"));
        choiceOneCheckBox.setText(getQuestionString("choiceOneText"));
        choiceTwoCheckBox.setText(getQuestionString("choiceTwoText"));

        choiceOneCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceTwoCheckBox.setChecked(false);
                enableDisableNext();
            }
        });

        choiceTwoCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceOneCheckBox.setChecked(false);
                enableDisableNext();
            }
        });

        monthListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                monthNo = position;
                view.setSelected(true);
                // Colour is set as background on first selected.  Must override
                if(monthsAdapter != null) {
                    monthsAdapter.removeDefault();
                }
                enableDisableNext();
            }
        });

        yearListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yearNo = position;
                view.setSelected(true);
                // Colour is set as background on first selected.  Must override
                if(yearsAdapter != null) {
                    yearsAdapter.removeDefault();
                }
                enableDisableNext();
            }
        });
    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {
        try {
            switch (answer.getInt(0)) {
                case 0:
                    choiceOneCheckBox.setChecked(true);
                    break;
                case 1:
                    choiceTwoCheckBox.setChecked(true);
                    break;
                case -1: // Indicates not filled in yet
                    break;
                default:
                    Log.d("TwoChoiceDate", "Invalid previous answer");
                    break;
            }
        } catch (JSONException e) {
            Log.i("TwoChoiceDateExpander", "Unable to parse answer (0)");
        }

        try {
            monthNo = answer.getInt(1);
        } catch (NumberFormatException|ArrayIndexOutOfBoundsException|JSONException e) {
            Log.d("TwoChoiceDate", "No previous month");
        }

        try {
            yearNo = answer.getInt(2);
        } catch (NumberFormatException|ArrayIndexOutOfBoundsException|JSONException e) {
            Log.d("TwoChoiceDate", "No previous year");
        }


        monthsAdapter = new ColorListAdapter<String>(
                activity.getBaseContext(),
                R.layout.list_background,
                activity.getResources().getStringArray(R.array.months),
                monthNo);
        monthListView.setAdapter(monthsAdapter);

        yearsAdapter = new ColorListAdapter<String>(
                activity.getBaseContext(),
                R.layout.list_background,
                activity.getResources().getStringArray(R.array.years),
                yearNo);
        yearListView.setAdapter(yearsAdapter);
    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        if(choiceOneCheckBox.isChecked()) {
            array.put(0);
        } else if(choiceTwoCheckBox.isChecked()) {
            array.put(1);
        } else {
            array.put(-1);
        }
        array.put(monthNo);
        array.put(yearNo);
        return array;
    }
}
