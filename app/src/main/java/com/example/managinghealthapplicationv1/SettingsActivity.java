package com.example.managinghealthapplicationv1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.framelayout_actionbar);

        if(findViewById(R.id.fragment_actionbarmenu) !=null)
        {
            if(savedInstanceState!=null)
                return;

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_actionbarmenu, new CreateSettingPreferences()).commit();
        }


    }
}
