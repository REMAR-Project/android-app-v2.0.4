package com.github.hintofbasil.crabbler.Questions;

import android.content.Context;

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

    JSONArray cache;
    Context context;
    int[] loopsDone;

    public QuestionReader(Context context) {
        this.context = context;
    }

    private JSONArray readJSON() throws IOException, JSONException {
        if(cache == null) {
            InputStream jsonInputStream = context.getResources().openRawResource(R.raw.questions);
            byte[] buffer = new byte[4096];
            int length = jsonInputStream.read(buffer);
            String jsonString = new String(buffer).substring(0, length);
            cache = new JSONArray(jsonString);
        }
        return cache;
    }

    private JSONObject findQuestionInArray(int id, JSONArray array) throws JSONException, IOException {
        for(int i=0; i<array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            if(object.has("loop")) {
                JSONArray subArray = object.getJSONArray("questions");
                JSONObject subSearch = findQuestionInArray(id-i, subArray);
                id -= (subArray.length() - 1); // Remove subquestions from total
                if(subSearch!=null) {
                    return subSearch;
                }
            } else { // Must be a question
                if(i==id) {
                    return object;
                }
            }
        }
        return null;
    }

    public JSONObject getJsonQuestion(int id) throws IOException, JSONException {
        return findQuestionInArray(id, readJSON());
    }

    private int getQuestionCount(boolean withNumberOnly, JSONArray array) throws JSONException {
        int count = 0;
        for(int i=0; i<array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            if(object.has("loop")) {
                JSONArray subArray = object.getJSONArray("questions");
                count += getQuestionCount(withNumberOnly, subArray);
            } else { // Must be a question
                if(!withNumberOnly || object.has("questionNumber")) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getQuestionCount() throws IOException, JSONException {
        return getQuestionCount(true, readJSON());
    }

    public int getRealQuestionCount() throws IOException, JSONException {
        return getQuestionCount(false, readJSON());
    }
}
