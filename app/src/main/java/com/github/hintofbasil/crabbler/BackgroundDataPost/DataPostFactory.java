package com.github.hintofbasil.crabbler.BackgroundDataPost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.github.hintofbasil.crabbler.Keys;

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

    public void submitAnswers(String answers) {
        // Register phone if not already registered
        SharedPreferences settingsPrefs = context.getSharedPreferences(Keys.SETTINGS_PREFS_KEY, Context.MODE_PRIVATE);
        if(settingsPrefs.getString(Keys.ACCESS_TOKEN_KEY, null) == null) {
            login();
            register();
        }
        answers = answers.replaceAll("\n", "");
        String[] split = answers.split(",");
        StringBuilder sb = new StringBuilder();
        for(String s : split) {
            sb.append('"');
            sb.append(s.toString());
            sb.append("\",");
        }
        addData(ANSWERS, sb.substring(0, sb.length()-1));
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
