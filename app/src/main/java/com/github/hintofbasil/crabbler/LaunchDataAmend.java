package com.github.hintofbasil.crabbler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.hintofbasil.crabbler.Questions.QuestionActivity;
import com.github.hintofbasil.crabbler.Questions.QuestionManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class LaunchDataAmend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(QuestionManager.get() == null) {
            try {
                QuestionManager.init(this);
                QuestionManager questionManager = QuestionManager.get();
                questionManager.saveAnswer(0, new JSONArray());
                questionManager.saveAnswer(1, new JSONArray());
                questionManager.saveAnswer(2, new JSONArray());
                questionManager.saveAnswer(3, new JSONArray());
                questionManager.saveAnswer(4, new JSONArray());
                questionManager.saveAnswer(5, new JSONArray());
                questionManager.saveAnswer(6, new JSONArray());
                questionManager.saveAnswer(7, new JSONArray());
                questionManager.saveAnswer(8, new JSONArray());
                questionManager.saveAnswer(9, new JSONArray());
                questionManager.saveAnswer(10, new JSONArray());
                questionManager.saveAnswer(11, new JSONArray());
                questionManager.saveAnswer(12, new JSONArray());
                questionManager.saveAnswer(13, new JSONArray());
                questionManager.saveAnswer(14, new JSONArray());
                questionManager.saveAnswer(15, new JSONArray());
                questionManager.saveAnswer(16, new JSONArray());
                questionManager.saveAnswer(17, new JSONArray());
                questionManager.saveAnswer(18, new JSONArray());
            } catch (IOException|JSONException e) {
                Log.e("LaunchDataAmend", "Unable to initialise QuestionManager");
            }
        }
        final Intent intent = new Intent(this, QuestionActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}
