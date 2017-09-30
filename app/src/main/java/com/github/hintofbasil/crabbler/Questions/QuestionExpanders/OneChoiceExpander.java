package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
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
 * Created by will on 05/05/16.
 */
public class OneChoiceExpander extends Expander {

    private static final int REQUIRED_ANSWERS = 1;

    private int currentAnswer = -1;
    LinearLayout questionOneButton;

    public OneChoiceExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_one_choice);
        TextView questionText = (TextView) activity.findViewById(R.id.question_text);
        TextView choiceOneTitle = (TextView) activity.findViewById(R.id.choice_one_title);
        questionOneButton = (LinearLayout) activity.findViewById(R.id.question_one_button);
        ImageView choiceOneImage = (ImageView) activity.findViewById(R.id.choice_one_image);

        choiceOneImage.setImageDrawable(getDrawable(getQuestionString("choiceOnePicture")));

        questionText.setText(getRichTextQuestionString("questionText"));
        questionText.setMovementMethod(new ScrollingMovementMethod());

        choiceOneTitle.setText(getRichTextQuestionString("choiceOneTitle"));

        questionOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAnswer = 0;
                questionOneButton.setBackgroundResource(R.color.questionSelectedBackground);
                enableDisableNext();
                //nextQuestion(200);
            }
        });

        }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {
        try {
            Integer i = answer.getInt(0);
            switch(i) {
                case 0:
                    questionOneButton.setBackgroundResource(R.color.questionSelectedBackground);
                    break;
                default:
                    Log.d("TwoPictureLayoutExpande", "Invalid previous answer");
            }
            currentAnswer = i;
        } catch (JSONException e) {
            Log.d("QuestionActivity", "No previous answer");
            return;
        }
    }

    @Override
    public JSONArray getSelectedAnswer() {
        JSONArray array = new JSONArray();
        array.put(currentAnswer);
        return array;
    }
}
