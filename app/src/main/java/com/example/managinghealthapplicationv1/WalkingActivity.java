package com.example.managinghealthapplicationv1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managinghealthapplicationv1.ui.login.LoginActivity;
import com.example.managinghealthapplicationv1.ui.login.RegisterActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WalkingActivity extends AppCompatActivity implements SensorEventListener {

    private View decorView;

    private static final int REQUEST_STEP_PERMISSION = 1;

    SensorManager sensorManager;

    TextView tv_steps;

    boolean running = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent setting = new Intent(WalkingActivity.this, SettingsActivity.class);
                startActivity(setting);
                return true;
            case R.id.about:
                Intent info = new Intent(WalkingActivity.this, AboutActivity.class);
                startActivity(info);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_steps);

        this.setTitle("Managing Health Application");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0)
                    decorView.setSystemUiVisibility(hideSystemBars());
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, REQUEST_STEP_PERMISSION);
        }

        tv_steps = (TextView) findViewById(R.id.tv_steps);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
            Fragment selectedFragment = null;

            switch (Item.getItemId()){
                case R.id.nav_walk: selectedFragment = new StepsFragment();
                    break;
                case R.id.nav_calcounter: selectedFragment = new CalcounterFragment();
                    break;
                case R.id.nav_medical: selectedFragment = new MedicalIDFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }
        else{
            Toast.makeText(this, "Step sensor not found, starting legacy step counter", Toast.LENGTH_SHORT).show();
            Intent legacy = new Intent(WalkingActivity.this, MainActivity.class);
            startActivity(legacy);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        //sensorManager.unregisterListener(this); testing phase...
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(running){
            tv_steps.setText(String.valueOf(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private int hideSystemBars() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }
    }
