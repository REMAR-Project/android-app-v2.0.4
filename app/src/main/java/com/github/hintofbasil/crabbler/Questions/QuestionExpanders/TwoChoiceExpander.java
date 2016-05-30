package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 13/05/16.
 */
public class TwoChoiceExpander extends Expander {

    CheckBox choiceOneCheckBox;
    CheckBox choiceTwoCheckBox;

    public TwoChoiceExpander(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void expandLayout(JSONObject question) throws JSONException {
        activity.setContentView(R.layout.expander_two_choice);
        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        choiceOneCheckBox = (CheckBox) activity.findViewById(R.id.choice_one);
        choiceTwoCheckBox = (CheckBox) activity.findViewById(R.id.choice_two);

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
    }

    @Override
    protected void setPreviousAnswer(String answer) {
        switch(answer) {
            case "0":
                choiceOneCheckBox.setChecked(true);
                break;
            case "1":
                choiceTwoCheckBox.setChecked(true);
                break;
            default:
                Log.d("TwoChoice", "No previous answer");
                break;
        }
    }

    @Override
    public String getSelectedAnswer() {
        if(choiceOneCheckBox.isChecked()) {
            return "0";
        } else if(choiceTwoCheckBox.isChecked()) {
            return "1";
        } else {
            return null;
        }
    }
}
