package com.github.hintofbasil.crabbler;

import android.os.Bundle;

/**
 * Created by will on 06/06/16.
 */
public class PreferenceActivity extends android.preference.PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
