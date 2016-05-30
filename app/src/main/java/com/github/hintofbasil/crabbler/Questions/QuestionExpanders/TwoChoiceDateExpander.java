package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONException;
import org.json.JSONObject;

public class TwoChoiceDateExpander extends Expander {

    CheckBox choiceOneCheckBox;
    CheckBox choiceTwoCheckBox;
    Spinner monthListSpinner;
    Spinner yearListSpinner;

    public TwoChoiceDateExpander(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void expandLayout(JSONObject question) throws JSONException {
        activity.setContentView(R.layout.expander_two_choice_date);

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        choiceOneCheckBox = (CheckBox) activity.findViewById(R.id.choice_one);
        choiceTwoCheckBox = (CheckBox) activity.findViewById(R.id.choice_two);
        monthListSpinner = (Spinner) activity.findViewById(R.id.month_list_view);
        yearListSpinner = (Spinner) activity.findViewById(R.id.year_list_view);

        imageView.setImageDrawable(getDrawable(question.getString("questionPicture")));
        titleView.setText(question.getString("questionTitle"));
        choiceOneCheckBox.setText(question.getString("choiceOneText"));
        choiceTwoCheckBox.setText(question.getString("choiceTwoText"));

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
        monthListSpinner.setAdapter(monthsAdapter);

        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<String>(
                activity.getBaseContext(),
                android.R.layout.simple_list_item_1,
                activity.getResources().getStringArray(R.array.years));
        yearListSpinner.setAdapter(yearsAdapter);
    }

    @Override
    protected void setPreviousAnswer(String answer) {
        String[] answers = answer.split(";");
        switch(answers[0]) {
            case "0":
                choiceOneCheckBox.setChecked(true);
                break;
            case "1":
                choiceTwoCheckBox.setChecked(true);
                break;
            default:
                Log.d("TwoChoiceDate", "No previous answer");
                break;
        }

        try {
            int month = Integer.parseInt(answers[1]);
            monthListSpinner.setSelection(month);
        } catch (NumberFormatException|ArrayIndexOutOfBoundsException e) {
            Log.d("TwoChoiceDate", "No previous month");
        }

        try {
            int month = Integer.parseInt(answers[2]);
            yearListSpinner.setSelection(month);
        } catch (NumberFormatException|ArrayIndexOutOfBoundsException e) {
            Log.d("TwoChoiceDate", "No previous year");
        }
    }

    @Override
    public String getSelectedAnswer() {
        StringBuilder sb = new StringBuilder();
        if(choiceOneCheckBox.isChecked()) {
            sb.append('0');
        } else if(choiceTwoCheckBox.isChecked()) {
            sb.append('1');
        }
        sb.append(';');
        sb.append(monthListSpinner.getSelectedItemPosition());
        sb.append(';');
        sb.append(yearListSpinner.getSelectedItemPosition());
        return sb.toString();
    }
}
