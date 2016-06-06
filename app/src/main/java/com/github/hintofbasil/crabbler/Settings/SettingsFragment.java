package com.github.hintofbasil.crabbler.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.github.hintofbasil.crabbler.BackgroundDataPost.DataPostAlarm;
import com.github.hintofbasil.crabbler.Keys;
import com.github.hintofbasil.crabbler.R;

/**
 * Created by will on 06/06/16.
 */
public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }



    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(Keys.PREF_POST_FREQUENCY)) {
            DataPostAlarm dataPostAlarm = new DataPostAlarm();
            dataPostAlarm.SetAlarm(context);
        }
    }
}
