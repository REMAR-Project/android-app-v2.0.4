package com.github.musevarg.remar.BackgroundDataPost;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.github.musevarg.remar.BackgroundDataPost.DataPostAlarm;

/**
 * Created by will on 03/05/16.
 */
public class DataPostLaunchService extends Service {

    DataPostAlarm alarm = new DataPostAlarm();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarm.SetAlarm(getBaseContext());
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
