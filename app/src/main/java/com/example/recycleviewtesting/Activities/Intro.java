package com.example.recycleviewtesting.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Button;

import com.example.recycleviewtesting.Adapter.WalkthroughAdapter;
import com.example.recycleviewtesting.Item.WalkthroughItem;
import com.example.recycleviewtesting.R;

import java.util.ArrayList;
import java.util.List;

public class Intro extends AppCompatActivity {

    private ViewPager2 viewPager;
    private LinearLayout layoutIndicators;
    private Button buttonNext;
    private WalkthroughAdapter walkthroughAdapter;
    private List<WalkthroughItem> walkthroughItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        viewPager = findViewById(R.id.viewPager);
        layoutIndicators = findViewById(R.id.layoutIndicators);
        buttonNext = findViewById(R.id.buttonNext);

        setupWalkthroughItems();
        setupWalkthroughAdapter();
        setupIndicators();
        setCurrentIndicator(0);
        setupButtonNext();
    }

    private void setupWalkthroughItems() {
        walkthroughItems = new ArrayList<>();

        walkthroughItems.add(new WalkthroughItem(
                R.drawable.intro1, // Replace with your shoe images
                "We provide high quality products just for you"
        ));

        walkthroughItems.add(new WalkthroughItem(
                R.drawable.intro2,
                "Your satisfaction is our number one priority"
        ));

        walkthroughItems.add(new WalkthroughItem(
                R.drawable.intro3,
                "Let's fulfill your fashion needs with Shoea right now!"
        ));
    }

    private void setupWalkthroughAdapter() {
        walkthroughAdapter = new WalkthroughAdapter(walkthroughItems);
        viewPager.setAdapter(walkthroughAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
    }

    private void setupIndicators() {
        ImageView[] indicators = new ImageView[walkthroughAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutIndicators.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int index) {
        int childCount = layoutIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicators.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.indicator_inactive)
                );
            }
        }

        // Update button text for last slide
        if (index == walkthroughAdapter.getItemCount() - 1) {
            buttonNext.setText("Get Started");
        } else {
            buttonNext.setText("Next");
        }
    }

    private void setupButtonNext() {
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() + 1 < walkthroughAdapter.getItemCount()) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                } else {
                    // Navigate to main app or finish onboarding
                    startActivity(new Intent(Intro.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }
}