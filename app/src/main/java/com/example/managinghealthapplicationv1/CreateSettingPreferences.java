package com.example.managinghealthapplicationv1;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

public class CreateSettingPreferences extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);
    }
}
