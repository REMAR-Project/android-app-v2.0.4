package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.graphics.Paint;
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
public class TwoPictureLayoutExpander extends Expander {

    private static final int REQUIRED_ANSWERS = 1;

    private int currentAnswer = -1;
    LinearLayout questionOneButton;
    LinearLayout questionTwoButton;
    ImageView choiceOneImage;
    ImageView choiceTwoImage;

    public TwoPictureLayoutExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson, REQUIRED_ANSWERS);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_two_picture_choice);
        TextView questionText = (TextView) activity.findViewById(R.id.question_text);
        TextView choiceOneTitle = (TextView) activity.findViewById(R.id.choice_one_title);
        TextView choiceTwoTitle = (TextView) activity.findViewById(R.id.choice_two_title);
        questionOneButton = (LinearLayout) activity.findViewById(R.id.question_one_button);
        questionTwoButton = (LinearLayout) activity.findViewById(R.id.question_two_button);
        choiceOneImage = (ImageView) activity.findViewById(R.id.choice_one_image);
        choiceTwoImage = (ImageView) activity.findViewById(R.id.choice_two_image);
        TextView backText = (TextView) activity.findViewById(R.id.backText);
        TextView forwardText = (TextView) activity.findViewById(R.id.forwardText);

        choiceOneImage.setImageDrawable(getDrawable(getQuestionString("choiceOnePicture")));
        choiceTwoImage.setImageDrawable(getDrawable(getQuestionString("choiceTwoPicture")));

        questionText.setText(getRichTextQuestionString("questionText"));
        questionText.setMovementMethod(new ScrollingMovementMethod());

        choiceOneTitle.setText(getRichTextQuestionString("choiceOneTitle"));
        choiceTwoTitle.setText(getRichTextQuestionString("choiceTwoTitle"));

        backText.setText(getRichTextQuestionString("backText"));
        forwardText.setText(getRichTextQuestionString("forwardText"));

        questionOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAnswer = 0;
                questionTwoButton.setBackgroundResource(R.color.questionBackground);
                questionOneButton.setBackgroundResource(R.color.questionSelectedBackground);
                try
                {
                    choiceOneImage.setImageDrawable(getDrawable(getQuestionString("choiceOnePicTwo")));
                    choiceTwoImage.setImageDrawable(getDrawable(getQuestionString("choiceTwoPicture")));
                } catch (JSONException e) {}

                enableDisableNext();
                //nextQuestion(200);
            }
        });

        questionTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAnswer = 1;
                questionOneButton.setBackgroundResource(R.color.questionBackground);
                questionTwoButton.setBackgroundResource(R.color.questionSelectedBackground);
                try
                {
                    choiceOneImage.setImageDrawable(getDrawable(getQuestionString("choiceOnePicture")));
                    choiceTwoImage.setImageDrawable(getDrawable(getQuestionString("choiceTwoPicTwo")));
                } catch (JSONException e) {}

                enableDisableNext();
                //nextQuestion(200);
            }
        });    }

    @Override
    protected void setPreviousAnswer(JSONArray answer) {
        try {
            Integer i = answer.getInt(0);
            switch(i) {
                case 0:
                    questionOneButton.setBackgroundResource(R.color.questionSelectedBackground);
                    choiceOneImage.setImageDrawable(getDrawable(getQuestionString("choiceOnePicTwo")));
                    break;
                case 1:
                    questionTwoButton.setBackgroundResource(R.color.questionSelectedBackground);
                    choiceTwoImage.setImageDrawable(getDrawable(getQuestionString("choiceTwoPicTwo")));
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
