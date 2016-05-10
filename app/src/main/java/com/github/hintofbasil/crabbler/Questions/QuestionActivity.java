package com.github.hintofbasil.crabbler.Questions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.Expander;
import com.github.hintofbasil.crabbler.Questions.QuestionExpanders.TwoPictureLayoutExpander;
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
}
