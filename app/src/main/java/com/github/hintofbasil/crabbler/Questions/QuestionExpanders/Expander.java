package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.github.hintofbasil.crabbler.Questions.QuestionActivity;
import com.github.hintofbasil.crabbler.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Interface to expand a layout using a question JSON object
 */
public abstract class Expander {

    AppCompatActivity activity;

    public Expander(AppCompatActivity activity) {
        this.activity = activity;
    }

    public abstract void expandLayout(JSONObject question) throws JSONException;

    protected abstract void saveAnswer();

    protected void nextQuestion() {
        int question = activity.getIntent().getIntExtra(activity.getString(R.string.question_id_key), 0);
        //TODO handle last question
        Intent intent = new Intent(activity, QuestionActivity.class);
        intent.putExtra(activity.getString(R.string.question_id_key), question+1);
        activity.startActivity(intent);
        activity.finish();
    }

}
