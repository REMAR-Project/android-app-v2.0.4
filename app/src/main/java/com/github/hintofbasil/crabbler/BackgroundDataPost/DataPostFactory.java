package com.github.hintofbasil.crabbler.BackgroundDataPost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.github.hintofbasil.crabbler.Keys;
import com.github.hintofbasil.crabbler.R;

/**
 * Used to add urls to the post queue list
 * Will automatically handle generating unique keys
 *
 */
public class DataPostFactory {

    SharedPreferences toSendPrefs;
    Context context;

    public DataPostFactory(Context context) {
        this.toSendPrefs = context.getSharedPreferences(Keys.TO_SEND_PREFERENCES_KEY, Context.MODE_PRIVATE);
        this.context = context;
    }

    public void register() {
        //TODO use constant
        addData("Register", "");
    }

    public void login() {
        //TODO use constant
        addData("Login", "");
    }

    public void submitAnswers(String answers) {
        // Register phone if not already registered
        SharedPreferences settingsPrefs = context.getSharedPreferences(Keys.SETTINGS_PREFS_KEY, Context.MODE_PRIVATE);
        if(settingsPrefs.getString("ACCESS_TOKEN_KEY", null) == null) {
            login();
            register();
        }

        //TODO use constant
        addData("Answers", answers);
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
