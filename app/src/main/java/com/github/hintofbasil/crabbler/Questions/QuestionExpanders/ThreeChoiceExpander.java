package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import androidx.appcompat.app.AppCompatActivity;
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
public class ThreeChoiceExpander extends Expander {

    private static final int REQUIRED_ANSWERS = 1;

    CheckBox choiceOneCheckBox;
    CheckBox choiceTwoCheckBox;
    CheckBox choiceThreeCheckBox;
    CheckBox choiceUnknownCheckBox;
    boolean allowMultiple = false;

    public ThreeChoiceExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_three_choice);
        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        TextView questionView = (TextView) activity.findViewById(R.id.question_text);
        choiceOneCheckBox = (CheckBox) activity.findViewById(R.id.choice_one);
        choiceTwoCheckBox = (CheckBox) activity.findViewById(R.id.choice_two);
        choiceThreeCheckBox = (CheckBox) activity.findViewById(R.id.choice_three);
        choiceUnknownCheckBox = (CheckBox) activity.findViewById(R.id.choice_unknown);

        imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        titleView.setText(getRichTextQuestionString("questionTitle"));
        questionView.setText(getRichTextQuestionString("questionText"));
        choiceOneCheckBox.setText(getRichTextQuestionString("choiceOneText"));
        choiceTwoCheckBox.setText(getRichTextQuestionString("choiceTwoText"));
        choiceThreeCheckBox.setText(getRichTextQuestionString("choiceThreeText"));

        try {
            allowMultiple = questionJson.getBoolean("allowMultiple");
        } catch (JSONException e) {}

        try {
            if(questionJson.getBoolean("dontknow")) {
                choiceUnknownCheckBox.setVisibility(View.VISIBLE);
                choiceUnknownCheckBox.setText(getRichTextQuestionString("choiceUnknownText"));
            }
        } catch (JSONException e) {}

        choiceOneCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!allowMultiple) {
                    choiceTwoCheckBox.setChecked(false);
                    choiceThreeCheckBox.setChecked(false);
                    choiceUnknownCheckBox.setChecked(false);
                }
                enableDisableNext();
            }
        });

        choiceTwoCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!allowMultiple) {
                    choiceOneCheckBox.setChecked(false);
                    choiceThreeCheckBox.setChecked(false);
                    choiceUnknownCheckBox.setChecked(false);
                }
                enableDisableNext();
            }
        });

        choiceThreeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!allowMultiple) {
                    choiceOneCheckBox.setChecked(false);
                    choiceTwoCheckBox.setChecked(false);
                    choiceUnknownCheckBox.setChecked(false);
                }
                enableDisableNext();
            }
        });

        choiceUnknownCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!allowMultiple) {
                    choiceOneCheckBox.setChecked(false);
                    choiceTwoCheckBox.setChecked(false);
                    choiceThreeCheckBox.setChecked(false);
                }
                enableDisableNext();
            }
        });
    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {
        try {
            choiceOneCheckBox.setChecked(answer.getBoolean(0));
            choiceTwoCheckBox.setChecked(answer.getBoolean(1));
            choiceThreeCheckBox.setChecked(answer.getBoolean(2));
            choiceUnknownCheckBox.setChecked(answer.getBoolean(3));
        } catch (JSONException e) {
            Log.i("TwoChoiceExpander", "Unable to parse answer");
        }
    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        array.put(choiceOneCheckBox.isChecked());
        array.put(choiceTwoCheckBox.isChecked());
        array.put(choiceThreeCheckBox.isChecked());
        array.put(choiceUnknownCheckBox.isChecked());
        return array;
    }
}
