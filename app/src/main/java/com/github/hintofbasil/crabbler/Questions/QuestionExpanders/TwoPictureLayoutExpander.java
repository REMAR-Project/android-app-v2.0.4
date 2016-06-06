package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 05/05/16.
 */
public class TwoPictureLayoutExpander extends Expander {

    private int currentAnswer = -1;
    LinearLayout questionOneButton;
    LinearLayout questionTwoButton;

    public TwoPictureLayoutExpander(AppCompatActivity activity, JSONObject questionJson) {
        super(activity, questionJson);
    }

    @Override
    public void expandLayout() throws JSONException {
        activity.setContentView(R.layout.expander_two_picture_choice);
        TextView questionText = (TextView) activity.findViewById(R.id.question_text);
        TextView choiceOneTitle = (TextView) activity.findViewById(R.id.choice_one_title);
        TextView choiceTwoTitle = (TextView) activity.findViewById(R.id.choice_two_title);
        questionOneButton = (LinearLayout) activity.findViewById(R.id.question_one_button);
        questionTwoButton = (LinearLayout) activity.findViewById(R.id.question_two_button);
        ImageView choiceOneImage = (ImageView) activity.findViewById(R.id.choice_one_image);
        ImageView choiceTwoImage = (ImageView) activity.findViewById(R.id.choice_two_image);

        choiceOneImage.setImageDrawable(getDrawable(getQuestionString("choiceOnePicture")));
        choiceTwoImage.setImageDrawable(getDrawable(getQuestionString("choiceTwoPicture")));

        questionText.setText(getQuestionString("questionText"));
        questionText.setMovementMethod(new ScrollingMovementMethod());

        choiceOneTitle.setText(getQuestionString("choiceOneTitle"));
        choiceTwoTitle.setText(getQuestionString("choiceTwoTitle"));

        questionOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAnswer = 0;
                questionTwoButton.setBackgroundResource(R.color.questionBackground);
                questionOneButton.setBackgroundResource(R.color.questionSelectedBackground);
                nextQuestion(1000);
            }
        });

        questionTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAnswer = 1;
                questionOneButton.setBackgroundResource(R.color.questionBackground);
                questionTwoButton.setBackgroundResource(R.color.questionSelectedBackground);
                nextQuestion(1000);
            }
        });
    }

    @Override
    protected void setPreviousAnswer(String answer) {
        try {
            int intAnswer = Integer.valueOf(answer);
            if(intAnswer==0) {
                questionOneButton.setBackgroundResource(R.color.questionPreviouslySelectedBackground);
            } else {
                questionTwoButton.setBackgroundResource(R.color.questionPreviouslySelectedBackground);
            }
            currentAnswer = intAnswer;
        } catch (NumberFormatException e) {
            Log.d("QuestionActivity", "No previous answer");
            return;
        }
    }

    @Override
    public String getSelectedAnswer() {
        if(currentAnswer!=-1) {
            return String.valueOf(currentAnswer);
        } else {
            return null;
        }
    }
}
