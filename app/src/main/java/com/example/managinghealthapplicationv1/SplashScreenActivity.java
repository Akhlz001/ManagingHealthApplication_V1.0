package com.example.managinghealthapplicationv1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.managinghealthapplicationv1.ui.login.LoginActivity;
import com.example.managinghealthapplicationv1.ui.login.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // User is signed in
            // Start home activity
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        }
        else {
            EasySplashScreen configure = new EasySplashScreen(SplashScreenActivity.this)
                    .withFullScreen()
                    .withBackgroundColor(Color.parseColor("#efefef"))
                    .withFooterText("© 2019 - 2020 MHA All Rights Reserved")
                    .withLogo(R.drawable.logo);

            View easySplashScreen = configure.create();
            setContentView(easySplashScreen);
            // No user is signed in
            // start login activity
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, RegisterActivity.class));
                }
            }, 3000);   //3 seconds

        }

    }
}
