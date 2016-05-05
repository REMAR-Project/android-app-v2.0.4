package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 05/05/16.
 */
public class TwoPictureLayoutExpander extends Expander {

    public TwoPictureLayoutExpander(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void expandLayout(JSONObject question) throws JSONException {
        activity.setContentView(R.layout.two_picture_choice_layout);
        TextView questionText = (TextView) activity.findViewById(R.id.question_text);
        TextView choiceOneTitle = (TextView) activity.findViewById(R.id.choice_one_title);
        TextView choiceTwoTitle = (TextView) activity.findViewById(R.id.choice_two_title);
        LinearLayout questionOneButton = (LinearLayout) activity.findViewById(R.id.question_one_button);
        LinearLayout questionTwoButton = (LinearLayout) activity.findViewById(R.id.question_two_button);

        questionText.setText(question.getString("questionText"));
        questionText.setMovementMethod(new ScrollingMovementMethod());

        choiceOneTitle.setText(question.getString("choiceOneTitle"));
        choiceTwoTitle.setText(question.getString("choiceTwoTitle"));

        questionOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnswer();
                nextQuestion();
            }
        });


    }

    @Override
    protected void saveAnswer() {

    }
}
