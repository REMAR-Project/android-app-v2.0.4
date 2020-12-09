package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

    int count = 0;

    LinearLayout firstUnknown;
    LinearLayout firstDayLayout;
    LinearLayout firstNightLayout;
    LinearLayout firstDayNightLayout;

    int firstDayNightChoice = -1;

    public DayNightChoiceExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void expandLayout() throws JSONException {

        activity.setContentView(R.layout.expander_day_night_choice);

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        TextView description = (TextView) activity.findViewById(R.id.description);
        firstUnknown = (LinearLayout) activity.findViewById(R.id.first_day_night_unknown);
        firstDayLayout = (LinearLayout) activity.findViewById(R.id.first_day_view);
        firstNightLayout = (LinearLayout) activity.findViewById(R.id.first_night_view);
        firstDayNightLayout = (LinearLayout) activity.findViewById(R.id.first_day_night_view);

        imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        titleView.setText(getRichTextQuestionString("questionTitle"));
        description.setText(getRichTextQuestionString("description"));

        firstDayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayNightChoiceSelected(0);
                enableDisableNext();
            }
        });

        firstNightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayNightChoiceSelected(1);
                enableDisableNext();
            }
        });

        firstDayNightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayNightChoiceSelected(2);
                enableDisableNext();
            }
        });

        firstUnknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayNightChoiceSelected(3);
                enableDisableNext();
            }
        });

    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {
        try {
            int answerOne = answer.getInt(0);
            setDayNightChoiceSelected(answerOne);
        } catch (JSONException e) {
            Log.d("DayNightChoiceExpander", "No previous answer (0)");
        }
    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        array.put(firstDayNightChoice);
        return array;
    }

    private void setDayNightChoiceSelected(int choice) {
        firstDayLayout.setSelected(false);
        firstNightLayout.setSelected(false);
        firstDayNightLayout.setSelected(false);
        firstUnknown.setSelected(false);

        firstDayNightChoice = choice;
        switch(choice) {
            case 0:
                firstDayLayout.setSelected(true);
                break;
            case 1:
                firstNightLayout.setSelected(true);
                break;
            case 2:
                firstDayNightLayout.setSelected(true);
                break;
            case 3:
                firstUnknown.setSelected(true);
                break;
        }
    }
}
