package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TwoChoiceDateExpander extends Expander {

    CheckBox choiceOneCheckBox;
    CheckBox choiceTwoCheckBox;
    ListView monthListView;
    ListView yearListView;

    public TwoChoiceDateExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_two_choice_date);

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
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
            }
        });

        choiceTwoCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceOneCheckBox.setChecked(false);
            }
        });

        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<String>(
                activity.getBaseContext(),
                android.R.layout.simple_list_item_1,
                activity.getResources().getStringArray(R.array.months));
        monthListView.setAdapter(monthsAdapter);

        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<String>(
                activity.getBaseContext(),
                android.R.layout.simple_list_item_1,
                activity.getResources().getStringArray(R.array.years));
        yearListView.setAdapter(yearsAdapter);
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
            int month = answer.getInt(1);
            monthListView.setSelection(month);
        } catch (NumberFormatException|ArrayIndexOutOfBoundsException|JSONException e) {
            Log.d("TwoChoiceDate", "No previous month");
        }

        try {
            int month = answer.getInt(2);
            yearListView.setSelection(month);
        } catch (NumberFormatException|ArrayIndexOutOfBoundsException|JSONException e) {
            Log.d("TwoChoiceDate", "No previous year");
        }
    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        if(choiceOneCheckBox.isChecked()) {
            array.put(0);
        } else if(choiceTwoCheckBox.isChecked()) {
            array.put(1);
        }
        array.put(monthListView.getSelectedItemPosition());
        array.put(yearListView.getSelectedItemPosition());
        return array;
    }
}
