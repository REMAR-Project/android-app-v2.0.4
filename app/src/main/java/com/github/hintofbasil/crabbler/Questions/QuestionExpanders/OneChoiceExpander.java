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
public class OneChoiceExpander extends Expander {

    private static final int REQUIRED_ANSWERS = 1;

    CheckBox choiceOneCheckBox;
    boolean allowMultiple = false;

    public OneChoiceExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_one_choice);
        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        choiceOneCheckBox = (CheckBox) activity.findViewById(R.id.choice_one);

        imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        titleView.setText(getRichTextQuestionString("questionTitle"));
        choiceOneCheckBox.setText(getRichTextQuestionString("choiceOneText"));

        try {
            allowMultiple = questionJson.getBoolean("allowMultiple");
        } catch (JSONException e) {}

        choiceOneCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableDisableNext();
            }
        });
    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {
        try {
            choiceOneCheckBox.setChecked(answer.getBoolean(0));
        } catch (JSONException e) {
            Log.i("OneChoiceExpander", "Unable to parse answer");
        }
    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        array.put(choiceOneCheckBox.isChecked());
        return array;
    }
}
