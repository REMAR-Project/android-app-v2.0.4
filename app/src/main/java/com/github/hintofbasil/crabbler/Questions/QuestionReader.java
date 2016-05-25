package com.github.hintofbasil.crabbler.Questions;

import android.support.v7.app.AppCompatActivity;

import com.github.hintofbasil.crabbler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by will on 05/05/16.
 */
public class QuestionReader {

    public JSONArray getJsonQuestions(AppCompatActivity activity) throws IOException, JSONException {
        InputStream jsonInputStream = activity.getBaseContext().getResources().openRawResource(R.raw.questions);
        byte[] buffer = new byte[4096];
        int length = jsonInputStream.read(buffer);
        String jsonString = new String(buffer).substring(0, length);
        return new JSONArray(jsonString);
    }

    public JSONObject getJsonQuestion(AppCompatActivity activity, int id) throws IOException, JSONException {
        return getJsonQuestions(activity).getJSONObject(id);
    }
}
