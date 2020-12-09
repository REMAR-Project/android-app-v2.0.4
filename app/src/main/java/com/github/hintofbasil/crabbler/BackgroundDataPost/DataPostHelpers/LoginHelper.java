package com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostHelpers;

import android.content.SharedPreferences;
import android.util.Log;

import com.github.hintofbasil.crabbler.Keys;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 23/05/16.
 */
public class LoginHelper extends PostHelper {

    private static String DATA_FORMAT = "{\"username\":\"fisherman\",\"password\":\"%s\"}";

    private SharedPreferences prefs;

    public LoginHelper(SharedPreferences prefs) {
        super("http://crab.napier.ac.uk/api/0.1/auth");
        //super(null);
        this.prefs = prefs;
    }

    @Override
    protected String getData() {
        String phoneId = prefs.getString(Keys.PHONE_ID_KEY, null);
        if(phoneId == null) {
            Log.d("LoginHelper", "Unable to find phone id");
            return null;
        }
        return String.format(DATA_FORMAT, phoneId);
    }

    @Override
    public boolean successful() {
        if(responseData == null) {
            Log.d("LoginHelper", "No response");
            return false;
        }
        try {
            JSONObject responseJson = new JSONObject(responseData);
            String token = responseJson.getString("access_token");
            if(token == null) {
                Log.d("LoginHelper", "Invalid response");
                return false;
            }
            //TODO use contant
            prefs.edit().putString(Keys.ACCESS_TOKEN_KEY, token).commit();
            Log.i("LoginHelper", "Access token: " + token);
            return true;
        } catch (JSONException e) {
            Log.e("LoginHelper", "Invalid response\n" + Log.getStackTraceString(e));
            return false;
        }
    }
}
