package com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostHelpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import com.github.hintofbasil.crabbler.Keys;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by will on 27/05/16.
 */
public class RegisterHelper extends PostHelper {

    private static String DATA_FORMAT = "{\"phone\":{\"phone_id\":\"%s\",\"role\":\"fisherman\"}}";

    SharedPreferences prefs;
    Context context;

    public RegisterHelper(SharedPreferences prefs, Context context) {
        super("http://crab.napier.ac.uk/api/0.2/users");
        //super(null);
        this.prefs = prefs;
        this.context = context;
    }

    @Override
    protected String getData() {
        return String.format(DATA_FORMAT, getUniquePhoneId());
    }

    @Override
    public boolean successful() {
        if(responseData == null) {
            Log.d("RegisterHelper", "No response");
            return false;
        }
        try {
            JSONObject responseJson = new JSONObject(responseData);
            String id = responseJson.getString("phone_id");
            //TODO use contant
            prefs.edit().putString(Keys.PHONE_ID_KEY, id).apply();
            Log.i("RegisterHelper", "Phone id: " + id);
            return true;
        } catch (JSONException e) {
            Log.e("RegisterHelper", "Invalid response\n" + Log.getStackTraceString(e));
            return false;
        }
    }

    /**
     * Gets the unique phone id
     * @return Unique phone id. "0" if unique id not found.
     */
    private String getUniquePhoneId() {
        String id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if(id!=null) {
            Log.d("RegisterHelper", "Unique id: " + id);
            return id;
        } else {
            Log.d("RegisterHelper", "No unique id found.  Using 0");
            return "0";
        }
    }
}
