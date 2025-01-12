package com.example.mobileprojectfinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ProjectDetailsActivity extends AppCompatActivity {

    private String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        TextView nameTextView = findViewById(R.id.project_name);
        TextView descriptionTextView = findViewById(R.id.project_description);
        TextView startDateTextView = findViewById(R.id.project_start_date);
        TextView endDateTextView = findViewById(R.id.project_end_date);
        ImageView projectImageView = findViewById(R.id.project_image);
        Button editButton = findViewById(R.id.edit_button);
        Button deleteButton = findViewById(R.id.delete_button);

        // استلام البيانات من الـ Intent
        projectId = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        String startDate = getIntent().getStringExtra("start_date");
        String endDate = getIntent().getStringExtra("end_date");
        String imageUrl = getIntent().getStringExtra("image_url");

        // عرض البيانات
        nameTextView.setText(name);
        descriptionTextView.setText(description);
        startDateTextView.setText("Start Date: " + startDate);
        endDateTextView.setText("End Date: " + endDate);
        Glide.with(this).load(imageUrl).into(projectImageView);

        // زر التعديل
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProjectActivity.class);
            intent.putExtra("id", projectId);
            intent.putExtra("name", name);
            intent.putExtra("description", description);
            intent.putExtra("start_date", startDate);
            intent.putExtra("end_date", endDate);
            intent.putExtra("image_url", imageUrl);
            startActivityForResult(intent, 1);
        });

        // زر الحذف
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    // عرض مربع الحوار لتأكيد الحذف
    private void showDeleteConfirmationDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this project?")
                .setPositiveButton("Yes", (dialog, which) -> executeDeleteProject())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    // تنفيذ عملية الحذف
    private void executeDeleteProject() {
        String url = "http://192.168.1.106/mobile/deleteProject.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "Project deleted successfully!", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                },
                error -> Toast.makeText(this, "Error deleting project: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", projectId); // إرسال معرف المشروع للحذف
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}
