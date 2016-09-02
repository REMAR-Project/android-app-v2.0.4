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
            } catch (IOException|JSONException e) {
                Log.e("LaunchDataAmend", "Unable to initialise QuestionManager");
            }
        }
        final Intent intent = new Intent(this, QuestionActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}
