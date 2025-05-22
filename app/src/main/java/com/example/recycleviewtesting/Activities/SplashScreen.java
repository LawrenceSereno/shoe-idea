package com.example.recycleviewtesting.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recycleviewtesting.R;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DURATION = 6000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Start MainActivity after 3 seconds
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreen.this, WelcomeActivity.class));
            finish();
        }, SPLASH_DURATION);
    }
}
