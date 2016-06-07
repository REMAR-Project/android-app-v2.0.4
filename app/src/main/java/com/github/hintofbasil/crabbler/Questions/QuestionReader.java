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

    JSONArray questions;
    AppCompatActivity activity;

    public QuestionReader(AppCompatActivity activity) {
        this.activity = activity;
    }

    public JSONArray getJsonQuestions() throws IOException, JSONException {
        if(questions == null) {
            InputStream jsonInputStream = activity.getBaseContext().getResources().openRawResource(R.raw.questions);
            byte[] buffer = new byte[4096];
            int length = jsonInputStream.read(buffer);
            String jsonString = new String(buffer).substring(0, length);
            questions = new JSONArray(jsonString);
        }
        return questions;
    }

    public JSONObject getJsonQuestion(int id) throws IOException, JSONException {
        return getJsonQuestions().getJSONObject(id);
    }

    public int getQuestionCount() throws IOException, JSONException {
        int count = 0;
        for(int i=0;i<getJsonQuestions().length();i++) {
            JSONObject question = getJsonQuestions().getJSONObject(i);
            try {
                question.getInt("questionNumber");
                count++;
            } catch (JSONException e) {
            }
        }
        return count;
    }

    public int getRealQuestionCount() throws IOException, JSONException {
        return getJsonQuestions().length();
    }
}
