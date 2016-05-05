package com.github.hintofbasil.crabbler.Questions.QuestionExpanders;

import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Interface to expand a layout using a question JSON object
 */
public interface Expander {

    public void expandLayout(AppCompatActivity activity, JSONObject question) throws JSONException;

}
