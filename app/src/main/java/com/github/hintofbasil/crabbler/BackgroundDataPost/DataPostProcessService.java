package com.github.hintofbasil.crabbler.BackgroundDataPost;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;

import com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostHelpers.AnswersHelper;
import com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostHelpers.LoginHelper;
import com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostHelpers.PostHelper;
import com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostHelpers.RegisterHelper;
import com.github.hintofbasil.crabbler.Keys;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by will on 03/05/16.
 */
public class DataPostProcessService extends IntentService {

    private SharedPreferences toSendPrefs;
    private SharedPreferences settingsPrefs;

    public DataPostProcessService() {
        super("DataPostProcessService");
    }

    public DataPostProcessService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        postData();
    }

    /**
     * Posts data saved in the post queue.
     * Will remove entry from the post queue on successful post.
     */
    private synchronized void postData() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //Remains null if set in constructor
        if(toSendPrefs==null) {
            toSendPrefs = getSharedPreferences(Keys.TO_SEND_PREFERENCES_KEY, Context.MODE_PRIVATE);
        }
        if(settingsPrefs==null) {
            settingsPrefs = getSharedPreferences(Keys.SETTINGS_PREFS_KEY, Context.MODE_PRIVATE);
        }
        //Check for internet connection
        if(cm.getActiveNetworkInfo() != null) {
            if(toSendPrefs.getAll() != null && !toSendPrefs.getAll().isEmpty()) {
                for (String detail : toSendPrefs.getAll().keySet()) {
                    try {
                        String[] split = toSendPrefs.getString(detail, null).split(";", -1); //Should never return null
                        PostHelper helper;
                        switch (split[0]) {
                            case DataPostFactory.REGISTER:
                                helper = new RegisterHelper(settingsPrefs, getBaseContext());
                                break;
                            case DataPostFactory.LOGIN:
                                helper = new LoginHelper(settingsPrefs);
                                break;
                            case DataPostFactory.ANSWERS:
                                helper = new AnswersHelper(settingsPrefs, split[1]);
                                break;
                            default:
                                Log.e("DataPostProcessService", "Unknown Helper: " + split[0]);
                                return;
                        }
                        helper.post();
                        if (helper.successful()) {
                            Log.i("DataPostProcessService", "Posted: " + split[0]);
                            toSendPrefs.edit().remove(detail).commit();
                        }
                    } catch (MalformedURLException e) {
                        Log.e("DataPostProcessService", "Malformed URL.");
                    } catch (IOException e) {
                        Log.d("DataPostProcessService", Log.getStackTraceString(e));

                    }
                }
            } else {
                Log.i("DataPostProcessService", "No data to send.");
            }
        } else {
            Log.i("DataPostProcessService", "No network connection available");
        }
    }
}
