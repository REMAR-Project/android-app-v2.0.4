package com.github.hintofbasil.crabbler.BackgroundDataPost;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.github.hintofbasil.crabbler.Keys;

public class DataPostAlarm extends BroadcastReceiver {

    private static final long ALARM_DELAY = 1000 * 60 * 1; //Minimum 60 seconds

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, DataPostProcessService.class);
        context.startService(serviceIntent);
    }

    public void SetAlarm(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        long delay = ALARM_DELAY;
        String delayString = sharedPref.getString(Keys.PREF_POST_FREQUENCY, null);
        if(delayString!=null) {
            delay = Long.parseLong(delayString);
        } else {
            Log.d("DataPostAlarm", "Unable to parse frequency setting " + delayString);
        }
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, DataPostAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        Log.i("DataPostAlarm", "Launching background poster.  Delay: " + delay);
        // Will only create one instance even on phone boot
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), delay, pi);
    }
}
