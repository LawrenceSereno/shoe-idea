package com.example.recycleviewtesting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set click listeners
        findViewById(R.id.edit_profile_container).setOnClickListener(this);
        findViewById(R.id.address_container).setOnClickListener(this);
        findViewById(R.id.notification_container).setOnClickListener(this);
        findViewById(R.id.payment_container).setOnClickListener(this);
        findViewById(R.id.security_container).setOnClickListener(this);
        findViewById(R.id.language_container).setOnClickListener(this);
        findViewById(R.id.privacy_policy_container).setOnClickListener(this);
        findViewById(R.id.help_center_container).setOnClickListener(this);
        findViewById(R.id.log_out_container).setOnClickListener(this);

        // Optional: Setup your bottom navigation here if applicable
        // setupBottomNav(); // if you had BaseActivity or equivalent
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        int id = v.getId();
//
//        switch (id) {
//            case R.id.edit_profile_container:
//                intent = new Intent(this, EditProfileActivity.class);
//                break;
//            case R.id.address_container:
//                intent = new Intent(this, AddressActivity.class);
//                break;
//            case R.id.notification_container:
//                intent = new Intent(this, NotificationActivity.class);
//                break;
//            case R.id.payment_container:
//                intent = new Intent(this, PaymentActivity.class);
//                break;
//            case R.id.security_container:
//                intent = new Intent(this, SecurityActivity.class);
//                break;
//            case R.id.language_container:
//                intent = new Intent(this, LanguageActivity.class);
//                break;
//            case R.id.privacy_policy_container:
//                intent = new Intent(this, PrivacyPolicyActivity.class);
//                break;
//            case R.id.help_center_container:
//                intent = new Intent(this, HelpCenterActivity.class);
//                break;
//            case R.id.log_out_container:
//                logoutUser();
//                return;  // logout handled, no need to start activity
//            default:
//                Toast.makeText(this, "Feature not implemented yet", Toast.LENGTH_SHORT).show();
//                return;
//        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    private void logoutUser() {
        // Add real logout logic here: FirebaseAuth.getInstance().signOut(), clear prefs, etc.
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
