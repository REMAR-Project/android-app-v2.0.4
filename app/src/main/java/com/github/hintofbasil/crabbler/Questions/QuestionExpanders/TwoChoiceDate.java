package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONException;
import org.json.JSONObject;

public class TwoChoiceDate extends Expander {

    public TwoChoiceDate(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void expandLayout(JSONObject question) throws JSONException {
        activity.setContentView(R.layout.activity_two_choice_date);

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        CheckBox choiceOneCheckBox = (CheckBox) activity.findViewById(R.id.choice_one);
        CheckBox choiceTwoCheckBox = (CheckBox) activity.findViewById(R.id.choice_two);

        imageView.setImageDrawable(getDrawable(question.getString("questionPicture")));
        titleView.setText(question.getString("questionTitle"));
        choiceOneCheckBox.setText(question.getString("choiceOneText"));
        choiceTwoCheckBox.setText(question.getString("choiceTwoText"));
    }

    @Override
    protected void setPreviousAnswer(String answer) {

    }

    @Override
    public String getSelectedAnswer() {
        return null;
    }
}
