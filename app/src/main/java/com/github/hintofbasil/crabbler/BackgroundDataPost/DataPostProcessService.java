package com.github.hintofbasil.crabbler.BackgroundDataPost;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;

import com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostHelpers.LoginHelper;
import com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostHelpers.PostHelper;
import com.github.hintofbasil.crabbler.R;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by will on 03/05/16.
 */
public class DataPostProcessService extends IntentService {

    private SharedPreferences toSendPrefs;

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
            toSendPrefs = getSharedPreferences(getString(R.string.to_send_preferences_key), Context.MODE_PRIVATE);
        }
        //Check for internet connection
        if(cm.getActiveNetworkInfo() != null) {
            if(toSendPrefs.getAll() != null && !toSendPrefs.getAll().isEmpty()) {
                for (String detail : toSendPrefs.getAll().keySet()) {
                    try {
                        PostHelper helper = new LoginHelper();
                        helper.post();
                        if (helper.successful()) {
                            Log.i("DataPostProcessService", "Posted: " + "LoginHelper");
                            toSendPrefs.edit().remove(detail).apply();
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
