package com.github.hintofbasil.crabbler;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;

import com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostAlarm;

/**
 * Created by will on 06/06/16.
 */
public class PreferenceActivity extends android.preference.PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(Keys.PREF_POST_FREQUENCY)) {
            DataPostAlarm dataPostAlarm = new DataPostAlarm();
            dataPostAlarm.SetAlarm(getBaseContext());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
