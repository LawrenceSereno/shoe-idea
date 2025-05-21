package com.example.recycleviewtesting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DURATION = 6000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView splashGif = findViewById(R.id.splash_gif);

        // Load loading.gif from raw folder
        Glide.with(this)
                .asGif()
                .load(R.raw.loading) // don't use ".gif" here
                .into(splashGif);

        // Start MainActivity after 3 seconds
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }, SPLASH_DURATION);
    }
}
