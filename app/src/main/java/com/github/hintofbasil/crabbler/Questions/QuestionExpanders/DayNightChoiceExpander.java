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
import org.w3c.dom.Text;

import java.io.IOException;

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

    CheckBox secondUnknown;
    LinearLayout secondDayLayout;
    LinearLayout secondNightLayout;
    LinearLayout secondDayNightLayout;

    int firstDayNightChoice = -1;
    int secondDayNightChoice = -1;

    public DayNightChoiceExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        //Get count //note: I really don't know why this is here. but we never need the 2nd one, so this will always return 1
        /*try {
            int countQuestionId = questionJson.getInt("quantityQuestion");
            JSONArray answers = getAnswer(countQuestionId);
            for(int i=0;i<answers.length();i++) {
                if(answers.getBoolean(i)) {
                    count++;
                }
            }
        } catch (IOException e) {
            Log.e("DayNightChoiceExpander", "No attribute quantityQuestion");
        }*/
        count = 1;
        if(count == 1) {
            activity.setContentView(R.layout.expander_day_night_choice);
        } else if(count == 2) {
            activity.setContentView(R.layout.expander_day_night_choice_double);
        }

        ImageView imageView = (ImageView) activity.findViewById(R.id.image);
        TextView titleView = (TextView) activity.findViewById(R.id.title);
        TextView description = (TextView) activity.findViewById(R.id.description);
        firstUnknown = (LinearLayout) activity.findViewById(R.id.first_day_night_unknown);
        firstDayLayout = (LinearLayout) activity.findViewById(R.id.first_day_view);
        firstNightLayout = (LinearLayout) activity.findViewById(R.id.first_night_view);
        firstDayNightLayout = (LinearLayout) activity.findViewById(R.id.first_day_night_view);
        if(count == 2) {
            secondDayLayout = (LinearLayout) activity.findViewById(R.id.second_day_view);
            secondNightLayout = (LinearLayout) activity.findViewById(R.id.second_night_view);
            secondDayNightLayout = (LinearLayout) activity.findViewById(R.id.second_day_night_view);
            secondUnknown = (CheckBox) activity.findViewById(R.id.second_day_night_unknown);

            secondDayLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDayNightChoiceTwo(0, R.color.questionSelectedBackground);
                    enableDisableNext();
                }
            });

            secondNightLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDayNightChoiceTwo(1, R.color.questionSelectedBackground);
                    enableDisableNext();
                }
            });

            secondDayNightLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDayNightChoiceTwo(2, R.color.questionSelectedBackground);
                    enableDisableNext();
                }
            });

            secondUnknown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDayNightChoiceTwo(3, R.color.questionSelectedBackground);
                    enableDisableNext();
                }
            });

            //Override template defaults
            secondUnknown.setTextColor(activity.getResources().getColor(android.R.color.tertiary_text_light));
        }

        imageView.setImageDrawable(getDrawable(getQuestionString("questionPicture")));
        titleView.setText(getRichTextQuestionString("questionTitle"));
        description.setText(getRichTextQuestionString("description"));

        firstDayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayNightChoiceOne(0, R.color.questionSelectedBackground);
                enableDisableNext();
            }
        });

        firstNightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayNightChoiceOne(1, R.color.questionSelectedBackground);
                enableDisableNext();
            }
        });

        firstDayNightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayNightChoiceOne(2, R.color.questionSelectedBackground);
                enableDisableNext();
            }
        });

        firstUnknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayNightChoiceOne(3, R.color.questionSelectedBackground);
                enableDisableNext();
            }
        });

        //Override template defaults
        //titleView.setTextSize(activity.getResources().getDimension(R.dimen.day_night_choice_title));
        //firstUnknown.setTextColor(activity.getResources().getColor(android.R.color.tertiary_text_light));
    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {
        try {
            int answerOne = answer.getInt(0);
            setDayNightChoiceOne(answerOne, R.color.questionSelectedBackground);
        } catch (JSONException e) {
            Log.d("DayNightChoiceExpander", "No previous answer (0)");
        }
        if(count == 2) {
            try {
                int answerTwo = answer.getInt(1);
                setDayNightChoiceTwo(answerTwo, R.color.questionSelectedBackground);
            } catch (JSONException e) {
                Log.d("DayNightChoiceExpander", "No previous answer (1)");
            }
        }
    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        array.put(firstDayNightChoice);
        if(count == 2) {
            array.put(secondDayNightChoice);
        }
        return array;
    }

    private void setDayNightChoiceOne(int choice, int colorId) {
        highlightLinearLayout(firstDayLayout, R.color.none);
        highlightLinearLayout(firstNightLayout, R.color.none);
        highlightLinearLayout(firstDayNightLayout, R.color.none);
        highlightLinearLayout(firstUnknown, R.color.none);
        //firstUnknown.setChecked(false);
        firstDayNightChoice = choice;
        switch(choice) {
            case 0:
                highlightLinearLayout(firstDayLayout, colorId);
                break;
            case 1:
                highlightLinearLayout(firstNightLayout, colorId);
                break;
            case 2:
                highlightLinearLayout(firstDayNightLayout, colorId);
                break;
            case 3:
                highlightLinearLayout(firstUnknown, colorId);
                break;
        }
    }

    private void setDayNightChoiceTwo(int choice, int colorId) {
        highlightLinearLayout(secondDayLayout, R.color.none);
        highlightLinearLayout(secondNightLayout, R.color.none);
        highlightLinearLayout(secondDayNightLayout, R.color.none);
        secondUnknown.setChecked(false);
        secondDayNightChoice = choice;
        switch(choice) {
            case 0:
                highlightLinearLayout(secondDayLayout, colorId);
                break;
            case 1:
                highlightLinearLayout(secondNightLayout, colorId);
                break;
            case 2:
                highlightLinearLayout(secondDayNightLayout, colorId);
                break;
            case 3:
                secondUnknown.setChecked(true);
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
