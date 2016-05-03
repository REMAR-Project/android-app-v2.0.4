package com.github.hintofbasil.crabbler.BackgroundDataPost;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostAlarm;

/**
 * Created by will on 03/05/16.
 */
public class DataPostAutoStart extends BroadcastReceiver {

    DataPostAlarm alarm = new DataPostAlarm();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            alarm.SetAlarm(context);
        }
    }
}
