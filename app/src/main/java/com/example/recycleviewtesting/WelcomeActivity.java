package com.example.recycleviewtesting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class WelcomeActivity extends AppCompatActivity {

    private boolean hasNavigated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        ConstraintLayout welcomeLayout = findViewById(R.id.welcome_layout);

        welcomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasNavigated) {
                    hasNavigated = true;
                    Intent intent = new Intent(WelcomeActivity.this, Intro.class); // Change to your actual next activity
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
