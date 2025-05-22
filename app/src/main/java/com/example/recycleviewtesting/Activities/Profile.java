package com.example.recycleviewtesting.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide; // ✅ Glide import for image loading
import com.example.recycleviewtesting.Address.MyAddressActivity;
import com.example.recycleviewtesting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private TextView userNameText;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Setup views
        userNameText = findViewById(R.id.user_name);
        profileImage = findViewById(R.id.profile_image);

        // ✅ Open ProfileChange when profile image is clicked
        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Profile.this, ProfileChange.class);
            startActivity(intent);
        });

        // ✅ Get Firebase user info and load display name + profile photo
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String displayName = user.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                userNameText.setText(displayName);
            } else {
                userNameText.setText("User");
            }

            // ✅ Load profile photo using Glide if photo URL exists
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .placeholder(R.drawable.check) // Replace with your placeholder image
                        .error(R.drawable.close)
                        .into(profileImage);
            }
        } else {
            userNameText.setText("Guest");
        }

        // Set click listeners for menu items
        findViewById(R.id.edit_profile_container).setOnClickListener(this);
        findViewById(R.id.address_container).setOnClickListener(this);
        findViewById(R.id.payment_container).setOnClickListener(this);
        findViewById(R.id.security_container).setOnClickListener(this);
        findViewById(R.id.log_out_container).setOnClickListener(this);
    }

    // ✅ Reload profile image when coming back from ProfileChange
    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .placeholder(R.drawable.check)
                    .error(R.drawable.close)
                    .into(profileImage);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // Show message only, comment out actual activity calls
        if (id == R.id.edit_profile_container) {
            Toast.makeText(this, "Edit Profile: Coming soon", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Change_usercrid.class);
            startActivity(intent);
        } else if (id == R.id.address_container) {
            Toast.makeText(this, "Address: Coming soon", Toast.LENGTH_SHORT).show();
         Intent intent = new Intent(this, MyAddressActivity.class);
            startActivity(intent);
//        } else if (id == R.id.notification_container) {
//            Toast.makeText(this, "Notifications: Coming soon", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(this, NotificationActivity.class);
            // startActivity(intent);
        } else if (id == R.id.payment_container) {
//            Toast.makeText(this, "Payments: Coming soon", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(this, PurchaseHistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.security_container) {
            Toast.makeText(this, "Security: Coming soon", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(this, SecurityActivity.class);
            // startActivity(intent);
//        } else if (id == R.id.language_container) {
//            Toast.makeText(this, "Language Settings: Coming soon", Toast.LENGTH_SHORT).show();
//            // Intent intent = new Intent(this, LanguageActivity.class);
//            // startActivity(intent);
//        } else if (id == R.id.privacy_policy_container) {
//            Toast.makeText(this, "Privacy Policy: Coming soon", Toast.LENGTH_SHORT).show();
//            // Intent intent = new Intent(this, PrivacyPolicyActivity.class);
//            // startActivity(intent);
//        } else if (id == R.id.help_center_container) {
//            Toast.makeText(this, "Help Center: Coming soon", Toast.LENGTH_SHORT).show();
//            // Intent intent = new Intent(this, HelpCenterActivity.class);
//            // startActivity(intent);
        } else if (id == R.id.log_out_container) {
            logoutUser();
        } else {
            Toast.makeText(this, "Feature not implemented yet", Toast.LENGTH_SHORT).show();
        }
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
