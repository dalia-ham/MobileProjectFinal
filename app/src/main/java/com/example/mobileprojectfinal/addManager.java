package com.example.mobileprojectfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class addManager extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText firstName, lastName, email, phone, password, confirmPassword;
    private ImageView profileImage, backButton;
    private TextView imageName;
    private Button submitButton;
    private RadioGroup genderGroup;
    private Bitmap selectedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manager);

        // ربط المكونات
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone_number);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        profileImage = findViewById(R.id.profile_image);
        imageName = findViewById(R.id.image_name);
        submitButton = findViewById(R.id.submit_button);
        backButton = findViewById(R.id.back_button);
        genderGroup = findViewById(R.id.gender_group);

        // عند الضغط على زر العودة للخلف
        backButton.setOnClickListener(v -> onBackPressed());

        // عند الضغط على الصورة لرفعها
        profileImage.setOnClickListener(v -> openImageChooser());

        // عند الضغط على زر الإرسال
        submitButton.setOnClickListener(v -> submitManagerData());
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
                imageName.setText("Image Selected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void submitManagerData() {
        String firstNameText = firstName.getText().toString().trim();
        String lastNameText = lastName.getText().toString().trim();
        String emailText = email.getText().toString().trim();
        String phoneNumberText = phone.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String confirmPasswordText = confirmPassword.getText().toString().trim();

        // الحصول على قيمة الجنس
        int selectedGenderId = genderGroup.getCheckedRadioButtonId();
        RadioButton selectedGender = findViewById(selectedGenderId);
        String genderText = selectedGender.getText().toString();

        if (!passwordText.equals(confirmPasswordText)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (firstNameText.isEmpty() || lastNameText.isEmpty() || emailText.isEmpty() || phoneNumberText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // رابط الـ API
        String url = "http://192.168.1.106/mobile/add_manager.php";

        // تحويل الصورة إلى Base64 فقط إذا كانت موجودة
        final String encodedImage;
        if (selectedImageBitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            encodedImage = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
        } else {
            encodedImage = "";  // في حال عدم اختيار صورة
        }

        // إرسال الطلب باستخدام Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(addManager.this, "Response: " + response, Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(addManager.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", firstNameText);
                params.put("last_name", lastNameText);
                params.put("email", emailText);
                params.put("phone", phoneNumberText);
                params.put("password", passwordText);
                params.put("gender", genderText); // إرسال الجنس
                params.put("profile_image", encodedImage); // إرسال الصورة كـ Base64 (إذا كانت موجودة)
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
