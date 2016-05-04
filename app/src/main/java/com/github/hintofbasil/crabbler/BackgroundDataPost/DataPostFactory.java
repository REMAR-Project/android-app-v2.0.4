package com.github.hintofbasil.crabbler.BackgroundDataPost;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.hintofbasil.crabbler.R;

/**
 * Used to add urls to the post queue list
 * Will automatically handle generating unique keys
 *
 */
public class DataPostFactory {

    SharedPreferences toSendPrefs;

    public DataPostFactory(Context context) {
        toSendPrefs = context.getSharedPreferences(context.getString(R.string.to_send_preferences_key), Context.MODE_PRIVATE);
    }

    /**
     * Adds a URL to the list of data to be sent to the webserver
     *
     * @param url The url to be sent
     */
    public void addUrl(String url) {
        int id = 0;
        boolean flag = true;
        while(flag) {
            String idString = String.valueOf(id);
            if(toSendPrefs.contains(idString)) {
                id++;
            } else {
                // Unique id found
                toSendPrefs.edit().putString(idString, url).apply();
                flag = false;
            }
        }
    }
}
