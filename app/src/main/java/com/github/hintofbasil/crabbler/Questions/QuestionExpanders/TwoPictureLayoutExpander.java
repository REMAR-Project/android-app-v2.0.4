package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by will on 05/05/16.
 */
public class TwoPictureLayoutExpander extends Expander<Integer> {

    public TwoPictureLayoutExpander(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void expandLayout(JSONObject question) throws JSONException {
        activity.setContentView(R.layout.two_picture_choice_layout);
        TextView questionText = (TextView) activity.findViewById(R.id.question_text);
        TextView choiceOneTitle = (TextView) activity.findViewById(R.id.choice_one_title);
        TextView choiceTwoTitle = (TextView) activity.findViewById(R.id.choice_two_title);
        final LinearLayout questionOneButton = (LinearLayout) activity.findViewById(R.id.question_one_button);
        final LinearLayout questionTwoButton = (LinearLayout) activity.findViewById(R.id.question_two_button);
        ImageView choiceOneImage = (ImageView) activity.findViewById(R.id.choice_one_image);
        ImageView choiceTwoImage = (ImageView) activity.findViewById(R.id.choice_two_image);
        final int questionNo = question.getInt("questionNumber");

        choiceOneImage.setImageDrawable(getDrawable(question.getString("choiceOnePicture")));
        choiceTwoImage.setImageDrawable(getDrawable(question.getString("choiceTwoPicture")));

        questionText.setText(question.getString("questionText"));
        questionText.setMovementMethod(new ScrollingMovementMethod());

        choiceOneTitle.setText(question.getString("choiceOneTitle"));
        choiceTwoTitle.setText(question.getString("choiceTwoTitle"));

        questionOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    questionOneButton.setBackgroundResource(R.color.questionSelectedBackground);
                    saveAnswer(0, questionNo);
                    nextQuestion();
                } catch (IOException|JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        questionTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    questionTwoButton.setBackgroundResource(R.color.questionSelectedBackground);
                    saveAnswer(1, questionNo);
                    nextQuestion();
                } catch (IOException|JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
