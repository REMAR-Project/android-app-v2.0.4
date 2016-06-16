package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 13/05/16.
 */
public class TwoChoiceExpander extends Expander {

    CheckBox choiceOneCheckBox;
    CheckBox choiceTwoCheckBox;

    public TwoChoiceExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_two_choice);
        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        choiceOneCheckBox = (CheckBox) activity.findViewById(R.id.choice_one);
        choiceTwoCheckBox = (CheckBox) activity.findViewById(R.id.choice_two);

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
                case -1:
                    break;
                default:
                    Log.d("TwoChoiceExpander", "Invalid previous answer");
                    break;
            }
        } catch (JSONException e) {
            Log.i("TwoChoiceExpander", "Unable to parse answer");
        }
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
        return array;
    }
}
