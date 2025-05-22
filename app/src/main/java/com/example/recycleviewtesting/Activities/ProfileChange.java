package com.example.recycleviewtesting.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cloudinary.utils.ObjectUtils;
import com.example.recycleviewtesting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ProfileChange extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_change);

        // ASK FOR PERMISSION
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }

        imageView = findViewById(R.id.imageView6);
        Button buttonPickImage = findViewById(R.id.buttonPickImage);

        CloudinaryApi.initializeCloudinary();

        buttonPickImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri); // Show image in ImageView

            String imagePath = getRealPathFromURI(imageUri);
            if (imagePath != null) {
                File imageFile = new File(imagePath);
                uploadImageToCloudinary(imageFile);
            } else {
                Toast.makeText(this, "Could not get image path", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            try {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(columnIndex);
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    private void uploadImageToCloudinary(File file) {
        new Thread(() -> {
            try {
                Map result = CloudinaryApi.cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
                String url = (String) result.get("secure_url");

                runOnUiThread(() -> {
                    // Original line:
                    // Toast.makeText(ProfileChange.this, "Uploaded to: " + url, Toast.LENGTH_LONG).show();

//                    Toast.makeText(ProfileChange.this, "Uploaded to Cloudinary!", Toast.LENGTH_LONG).show();

                    // Save the image URL to Firebase
                    saveImageUrlToFirebase(url);
                });

            } catch (Exception e) {
                Log.d("Upload failed: ", e.getMessage());
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(ProfileChange.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    private void saveImageUrlToFirebase(String imageUrl) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_LONG).show();
            return;
        }

        String uid = user.getUid();

        Map<String, Object> updates = new HashMap<>();
        updates.put("profileImage", imageUrl);

        db.collection("users").document(uid).set(updates, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Image URL saved to Firestore!", Toast.LENGTH_SHORT).show();

                    // (Optional) Also update Firebase Auth profile image
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(Uri.parse(imageUrl))
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(this, Profile.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to save URL: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
