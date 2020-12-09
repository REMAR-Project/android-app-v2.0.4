package com.github.hintofbasil.crabbler.BackgroundDataPost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.github.hintofbasil.crabbler.Keys;
import com.github.hintofbasil.crabbler.Questions.QuestionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Used to add urls to the post queue list
 * Will automatically handle generating unique keys
 *
 */
public class DataPostFactory {

    SharedPreferences toSendPrefs;
    Context context;

    public static final String REGISTER = "REGISTER";
    public static final String LOGIN = "LOGIN";
    public static final String ANSWERS = "ANSWERS";

    public DataPostFactory(Context context) {
        this.toSendPrefs = context.getSharedPreferences(Keys.TO_SEND_PREFERENCES_KEY, Context.MODE_PRIVATE);
        this.context = context;
    }

    public void register() {
        addData(REGISTER, "");
    }

    public void login() {
        addData(LOGIN, "");
    }

    public void submitAnswers() {
        SharedPreferences settingsPrefs = context.getSharedPreferences(Keys.SETTINGS_PREFS_KEY, Context.MODE_PRIVATE);
        if(settingsPrefs.getString(Keys.ACCESS_TOKEN_KEY, null) == null) {
            login();
            register();
        }
        QuestionManager questionManager = QuestionManager.get();
        try {
            List<JSONObject> answers = questionManager.exportAnswers();
            for(JSONObject answer : answers) {
                JSONObject formatted = dataToString(answer);
                Log.i("DataPostFactory", "Posting: " + formatted);
                addData(ANSWERS, formatted.toString());
            }
        } catch (JSONException|IOException e) {
            Log.e("DataPostFactory", "Unable to parse answers\n" + Log.getStackTraceString(e));
        }
    }

    private JSONObject dataToString(JSONObject data) throws JSONException {
        JSONObject obj = new JSONObject();
        Iterator<String> keyIter = data.keys();
        while(keyIter.hasNext()) {
            String key = keyIter.next();
            JSONArray answer = (JSONArray) data.get(key);
            JSONArray newArray = new JSONArray();
            for (int i = 0; i < answer.length(); i++) {
                newArray.put(answer.get(i).toString());
            }
            obj.put(key, newArray);
        }
        return obj;
    }

    /**
     * Adds a URL to the list of data to be sent to the webserver
     *
     * @param helper The name of the helper class
     * @param data The data to be posted
     */
    private void addData(String helper, String data) {
        int id = 0;
        boolean flag = true;
        while(flag) {
            String idString = String.valueOf(id);
            if(toSendPrefs.contains(idString)) {
                id++;
            } else {
                // Unique id found
                toSendPrefs.edit().putString(idString, helper + ";" + data).apply();
                flag = false;
                Intent intent = new Intent(context, DataPostProcessService.class);
                context.startService(intent);
            }
        }
    }
}
