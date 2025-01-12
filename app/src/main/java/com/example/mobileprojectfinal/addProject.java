package com.example.mobileprojectfinal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class addProject extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText projectName, projectDescription, startDate, endDate, managerId;
    private Spinner projectStatusSpinner;
    private Button submitButton;
    private ImageView backButton, projectImage;
    private String selectedStatus = "";
    private Bitmap selectedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        projectName = findViewById(R.id.project_name);
        projectDescription = findViewById(R.id.project_description);
        startDate = findViewById(R.id.start_date);
        endDate = findViewById(R.id.end_date);
        managerId = findViewById(R.id.manager_id);
        projectStatusSpinner = findViewById(R.id.project_status_spinner);
        submitButton = findViewById(R.id.submit_button);
        backButton = findViewById(R.id.back_button);
        projectImage = findViewById(R.id.project_image);

        setupStatusSpinner();

        backButton.setOnClickListener(v -> finish());

        startDate.setOnClickListener(v -> showDatePickerDialog(startDate));
        endDate.setOnClickListener(v -> showDatePickerDialog(endDate));

        projectImage.setOnClickListener(v -> openImageChooser());

        submitButton.setOnClickListener(v -> {
            String name = projectName.getText().toString().trim();
            String description = projectDescription.getText().toString().trim();
            String start = startDate.getText().toString().trim();
            String end = endDate.getText().toString().trim();
            String manager = managerId.getText().toString().trim();

            if (validateInputs(name, description, start, end, manager, selectedStatus)) {
                addProjectToDatabase(name, description, start, end, manager, selectedStatus);
            }
        });
    }

    private void setupStatusSpinner() {
        String[] statuses = {"Active", "UnActive"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projectStatusSpinner.setAdapter(adapter);

        projectStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = statuses[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedStatus = "";
            }
        });
    }

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                    editText.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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
                projectImage.setImageBitmap(selectedImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateInputs(String name, String description, String start, String end, String manager, String status) {
        if (name.isEmpty()) {
            projectName.setError("Project name is required");
            return false;
        }
        if (description.isEmpty()) {
            projectDescription.setError("Project description is required");
            return false;
        }
        if (start.isEmpty()) {
            startDate.setError("Start date is required");
            return false;
        }
        if (end.isEmpty()) {
            endDate.setError("End date is required");
            return false;
        }
        if (manager.isEmpty()) {
            managerId.setError("Manager ID is required");
            return false;
        }
        if (status.isEmpty()) {
            Toast.makeText(this, "Please select a project status", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addProjectToDatabase(String name, String description, String start, String end, String manager, String status) {
        String url = "http://192.168.1.106/mobile/add_project.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        String encodedImage = "";
        if (selectedImageBitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            encodedImage = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
        }

        String finalEncodedImage = encodedImage;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");
                        Toast.makeText(addProject.this, message, Toast.LENGTH_SHORT).show();

                        if (success) {
                            clearForm();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(addProject.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(addProject.this, "Failed to connect to server", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("description", description);
                params.put("start_date", start);
                params.put("end_date", end);
                params.put("manager_id", manager);
                params.put("status", status);
                params.put("image", finalEncodedImage);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void clearForm() {
        projectName.setText("");
        projectDescription.setText("");
        startDate.setText("");
        endDate.setText("");
        managerId.setText("");
        projectStatusSpinner.setSelection(0);
        projectImage.setImageResource(R.drawable.puser_icon); // إعادة تعيين الصورة الافتراضية
    }
}
