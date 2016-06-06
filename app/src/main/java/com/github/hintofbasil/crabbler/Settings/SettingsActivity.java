package com.github.hintofbasil.crabbler.Settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by will on 06/06/16.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.setContext(getBaseContext());
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, settingsFragment)
                .commit();
    }
}
