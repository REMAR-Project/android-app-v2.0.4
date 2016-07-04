package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 03/06/16.
 */
public class DayNightChoiceExpander extends Expander {

    private static final int REQUIRED_ANSWERS = 1;

    CheckBox unknown;
    LinearLayout dayLayout;
    LinearLayout nightLayout;
    LinearLayout dayNightLayout;

    int dayNightChoice = -1;

    public DayNightChoiceExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_day_night_choice);

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        unknown = (CheckBox) activity.findViewById(R.id.day_night_unknown);
        dayLayout = (LinearLayout) activity.findViewById(R.id.day_view);
        nightLayout = (LinearLayout) activity.findViewById(R.id.night_view);
        dayNightLayout = (LinearLayout) activity.findViewById(R.id.day_night_view);

        imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        titleView.setText(getRichTextQuestionString("questionTitle"));

        dayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayNightChoice(0, R.color.questionSelectedBackground);
                enableDisableNext();
            }
        });

        nightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayNightChoice(1, R.color.questionSelectedBackground);
                enableDisableNext();
            }
        });

        dayNightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayNightChoice(2, R.color.questionSelectedBackground);
                enableDisableNext();
            }
        });

        unknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayNightChoice(3, R.color.questionSelectedBackground);
                enableDisableNext();
            }
        });

        //Override template defaults
        titleView.setTextSize(activity.getResources().getDimension(R.dimen.day_night_choice_title));
        unknown.setTextColor(activity.getResources().getColor(android.R.color.tertiary_text_light));
    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {
        try {
            int answerOne = answer.getInt(0);
            setDayNightChoice(answerOne, R.color.questionSelectedBackground);
        } catch (JSONException e) {
            Log.d("DayNightChoiceExpander", "No previous answer (1)");
        }
    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        array.put(dayNightChoice);
        return array;
    }

    private void setDayNightChoice(int choice, int colorId) {
        highlightLinearLayout(dayLayout, R.color.none);
        highlightLinearLayout(nightLayout, R.color.none);
        highlightLinearLayout(dayNightLayout, R.color.none);
        unknown.setChecked(false);
        dayNightChoice = choice;
        switch(choice) {
            case 0:
                highlightLinearLayout(dayLayout, colorId);
                break;
            case 1:
                highlightLinearLayout(nightLayout, colorId);
                break;
            case 2:
                highlightLinearLayout(dayNightLayout, colorId);
                break;
            case 3:
                unknown.setChecked(true);
                break;
        }
    }

    private void highlightLinearLayout(LinearLayout layout, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layout.setBackgroundColor(activity.getColor(colorId));
        } else {
            layout.setBackgroundColor(activity.getResources().getColor(colorId));
        }
    }
}
