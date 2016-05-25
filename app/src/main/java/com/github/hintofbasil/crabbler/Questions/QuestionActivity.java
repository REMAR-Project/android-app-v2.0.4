package com.github.hintofbasil.crabbler.Questions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.DateRange;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.DateRangeSelect;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.Expander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.ListSelect;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.TwoChoice;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.TwoChoiceDate;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.TwoPictureLayoutExpander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.YesNoExtra;
import com.github.hintofbasil.crabbler.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class QuestionActivity extends AppCompatActivity {

    Expander expander;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        byte[] buffer = new byte[1024];
        try {
            int questionId = getIntent().getIntExtra(getString(R.string.question_id_key), 0);
            QuestionReader qr = new QuestionReader();
            JSONObject questionJson = qr.getJsonQuestion(this, questionId);

            switch(questionJson.getString("questionType")) {
                case "TwoPictureChoice":
                    expander = new TwoPictureLayoutExpander(this);
                    break;
                case "TwoChoiceDate":
                    expander = new TwoChoiceDate(this);
                    break;
                case "TwoChoice":
                    expander = new TwoChoice(this);
                    break;
                case "DateRange":
                    expander = new DateRange(this);
                    break;
                case "DateRangeSelect":
                    expander = new DateRangeSelect(this);
                    break;
                case "ListSelect":
                    expander = new ListSelect(this);
                    break;
                case "YesNoExtra":
                    expander = new YesNoExtra(this);
                    break;
                default:
                    Log.e("QuestionActivity", "Unknown question type.");
                    return;
            }
            expander.expandLayout(questionJson);

        } catch (IOException|JSONException e) {
            Log.e("QuestionActivity", "Error reading questions\n" + Log.getStackTraceString(e));
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(expander!=null) {
            expander.setPreviousAnswer();
        }
    }

    public void previousQuestion(View view) {
        expander.previousQuestion();
    }

    public void nextQuestion(View view) {
        expander.nextQuestion(0);
    }
}
