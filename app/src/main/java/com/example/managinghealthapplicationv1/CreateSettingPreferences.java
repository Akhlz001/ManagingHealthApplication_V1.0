package com.example.managinghealthapplicationv1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.managinghealthapplicationv1.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class CreateSettingPreferences extends PreferenceFragmentCompat implements PreferenceManager.OnPreferenceTreeClickListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();

        switch (key) {
            case "ChangeMedical":

                Intent myIntent = new Intent(CreateSettingPreferences.this.getActivity(), MedicalActivity.class);
                startActivity(myIntent);
                return true;

            case "logout":

                FirebaseAuth.getInstance().signOut();
                Intent logout = new Intent(CreateSettingPreferences.this.getActivity(), LoginActivity.class);
                startActivity(logout);
                return true;


        }
        return super.onOptionsItemSelected((MenuItem) preference);
    }
}
