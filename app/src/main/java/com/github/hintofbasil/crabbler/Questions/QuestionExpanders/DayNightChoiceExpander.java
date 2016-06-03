package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 03/06/16.
 */
public class DayNightChoiceExpander extends Expander {

    public DayNightChoiceExpander(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void expandLayout(JSONObject question) throws JSONException {
        activity.setContentView(R.layout.expander_day_night_choice);

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
