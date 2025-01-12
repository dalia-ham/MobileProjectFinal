package com.example.mobileprojectfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddEmployeeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText firstName, lastName, email, phoneNumber, password, confirmPassword;
    private ImageView profileImage,back_button;
    private Button submitButton;
    private Bitmap selectedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        // Initialize the UI components
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phone_number);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        profileImage = findViewById(R.id.profile_image);
        submitButton = findViewById(R.id.submit_button);
        back_button =findViewById(R.id.back_button);
        // Handle profile image click
        profileImage.setOnClickListener(v -> openImageChooser());
        back_button.setOnClickListener(v -> finish());


        // Handle the submit button click
        submitButton.setOnClickListener(v -> {
            String firstNameStr = firstName.getText().toString().trim();
            String lastNameStr = lastName.getText().toString().trim();
            String emailStr = email.getText().toString().trim();
            String phoneStr = phoneNumber.getText().toString().trim();
            String passwordStr = password.getText().toString().trim();
            String confirmPasswordStr = confirmPassword.getText().toString().trim();

            if (validateInputs(firstNameStr, lastNameStr, emailStr, phoneStr, passwordStr, confirmPasswordStr)) {
                uploadData(firstNameStr, lastNameStr, emailStr, phoneStr, passwordStr);
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                profileImage.setImageBitmap(selectedImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateInputs(String firstName, String lastName, String email, String phone, String password, String confirmPassword) {
        if (firstName.isEmpty()) {
            this.firstName.setError("First name is required");
            return false;
        }
        if (lastName.isEmpty()) {
            this.lastName.setError("Last name is required");
            return false;
        }
        if (email.isEmpty()) {
            this.email.setError("Email is required");
            return false;
        }
        if (phone.isEmpty()) {
            this.phoneNumber.setError("Phone number is required");
            return false;
        }
        if (password.isEmpty()) {
            this.password.setError("Password is required");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            this.confirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    private void uploadData(String firstName, String lastName, String email, String phone, String password) {
        String url = "http://192.168.1.106/mobile/addEmployee.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        // اجعل المتغير encodedImage نهائي
        final String encodedImage;
        if (selectedImageBitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            encodedImage = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
        } else {
            encodedImage = ""; // إذا لم تكن هناك صورة مختارة
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        String message = jsonObject.getString("message");

                        Toast.makeText(AddEmployeeActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing server response.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to connect to the server.", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                // استخدم المتغير النهائي هنا
                Map<String, String> params = new HashMap<>();
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("email", email);
                params.put("phone", phone);
                params.put("password", password);
                params.put("image", encodedImage);
                return params;
            }
        };

        queue.add(stringRequest);
    }

}
