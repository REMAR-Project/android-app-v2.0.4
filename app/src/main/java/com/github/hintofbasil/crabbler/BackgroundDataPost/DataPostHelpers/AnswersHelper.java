package com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostHelpers;

import android.content.SharedPreferences;
import android.util.Log;

import com.github.hintofbasil.crabbler.Keys;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by will on 28/05/16.
 */
public class AnswersHelper extends PostHelper {

    private static String DATA_FORMAT = "{\"sightings\":[{\"valid\": true, \"seen_by\": \"%s\", \"answers\":[%s]}]}";

    private String questions;
    private SharedPreferences prefs;

    public AnswersHelper(SharedPreferences prefs, String questions) {
        super("http://crab.napier.ac.uk/api/0.2/sightings");
        this.questions = questions;
        this.prefs = prefs;
    }

    @Override
    protected String getData() {
        String phoneId = prefs.getString(Keys.PHONE_ID_KEY, null);
        if(phoneId == null) {
            Log.d("AnswersHelper", "Unable to find phone id");
            return null;
        }
        return String.format(DATA_FORMAT, phoneId, questions);
    }

    @Override
    public boolean successful() {
        if(responseData == null) {
            Log.d("AnswersHelper", "No response");
            return false;
        }
        try {
            JSONObject responseJson = new JSONObject(responseData);
            if(responseJson.getString("message").equals("Sightings uploaded successfully.")) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            Log.e("AnswersHelper", "Invalid JSON response\n" + Log.getStackTraceString(e));
            return false;
        }
    }

    @Override
    protected Map<String, String> getHeaders() {
        Map<String, String> map = super.getHeaders();
        String token = prefs.getString(Keys.ACCESS_TOKEN_KEY, null);
        if(token == null) {
            Log.d("AnswersHelper", "Missing access token");
            return null;
        }
        map.put("Authorization", "JWT " + token);
        return map;
    }
}
