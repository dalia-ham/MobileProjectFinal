package com.example.mobileprojectfinal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProjectActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText nameEditText, descriptionEditText, startDateEditText, endDateEditText;
    private ImageView projectImageView;
    private Button saveButton, changeImageButton;
    private Bitmap selectedImageBitmap;
    private String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);

        // ربط العناصر من XML
        nameEditText = findViewById(R.id.edit_project_name);
        descriptionEditText = findViewById(R.id.edit_project_description);
        startDateEditText = findViewById(R.id.edit_project_start_date);
        endDateEditText = findViewById(R.id.edit_project_end_date);
        projectImageView = findViewById(R.id.edit_project_image);
        saveButton = findViewById(R.id.save_button);
        changeImageButton = findViewById(R.id.change_image_button);

        // استلام البيانات من Intent
        projectId = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        String startDate = getIntent().getStringExtra("start_date");
        String endDate = getIntent().getStringExtra("end_date");
        String imageUrl = getIntent().getStringExtra("image_url");

        // عرض البيانات الحالية
        nameEditText.setText(name);
        descriptionEditText.setText(description);
        startDateEditText.setText(startDate);
        endDateEditText.setText(endDate);
        Glide.with(this).load(imageUrl).into(projectImageView);

        // فتح معرض الصور عند الضغط على تغيير الصورة
        changeImageButton.setOnClickListener(v -> openImageChooser());

        // حفظ التعديلات
        saveButton.setOnClickListener(v -> saveChanges());
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                projectImageView.setImageBitmap(selectedImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveChanges() {
        String url = "http://192.168.1.106/mobile/editProject.php"; // رابط API للتعديل

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "Project updated successfully!", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK); // إرجاع النتيجة
                    finish();
                },
                error -> Toast.makeText(this, "Error updating project: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", projectId);
                params.put("name", nameEditText.getText().toString().trim());
                params.put("description", descriptionEditText.getText().toString().trim());
                params.put("start_date", startDateEditText.getText().toString().trim());
                params.put("end_date", endDateEditText.getText().toString().trim());

                if (selectedImageBitmap != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                    byte[] imageBytes = byteArrayOutputStream.toByteArray();
                    String encodedImage = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
                    params.put("image", encodedImage);
                }

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
