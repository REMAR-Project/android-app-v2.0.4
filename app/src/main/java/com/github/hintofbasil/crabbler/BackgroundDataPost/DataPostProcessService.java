package com.github.hintofbasil.crabbler.BackgroundDataPost;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.hintofbasil.crabbler.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
                        URL u = new URL("http://40180582.pythonanywhere.com/static/login");
                        HttpURLConnection h = (HttpURLConnection) u.openConnection();
                        //TODO handle POST
                        if (h.getResponseCode() == 200) {
                            InputStream inputStream = new BufferedInputStream(h.getInputStream());
                            byte[] bytes = new byte[1024];
                            int length = inputStream.read(bytes);
                            String response = new String(bytes, "UTF-8").substring(0, length);
                            //TODO handle response
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
