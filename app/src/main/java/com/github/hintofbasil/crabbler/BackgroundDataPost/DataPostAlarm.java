package com.github.hintofbasil.crabbler.BackgroundDataPost;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;

import com.github.hintofbasil.crabbler.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class DataPostAlarm extends BroadcastReceiver {

    private static final long ALARM_DELAY = 1000 * 60 * 1; //Minimum 60 seconds

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, DataPostProcessService.class);
        context.startService(serviceIntent);
    }

    public void SetAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, DataPostAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        // Will only create one instance even on phone boot
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ALARM_DELAY, pi);
    }
}
