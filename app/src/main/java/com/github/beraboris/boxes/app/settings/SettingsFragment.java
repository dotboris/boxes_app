package com.github.beraboris.boxes.app.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.github.beraboris.boxes.app.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
