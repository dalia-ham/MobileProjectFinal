package com.example.mobileprojectfinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class adminDashboard extends AppCompatActivity {

    private TextView adminNameTextView, employeesCountTextView, projectsCountTextView, reportsCountTextView;
    private ImageView adminLogoImageView, settings, projects,employee,reports;
    private Button addManagerButton, addEmployeeButton, addProjectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // ربط عناصر الواجهة
        adminNameTextView = findViewById(R.id.admin_title);
        adminLogoImageView = findViewById(R.id.admin_logo);
        employeesCountTextView = findViewById(R.id.employees_count);
        projectsCountTextView = findViewById(R.id.projects_count);
        reportsCountTextView = findViewById(R.id.reports_count);

        addManagerButton = findViewById(R.id.add_manager_button);
        addEmployeeButton = findViewById(R.id.add_employee_button);
        addProjectButton = findViewById(R.id.add_project_button);
        settings = findViewById(R.id.setting);
        projects = findViewById(R.id.projects);
        employee = findViewById(R.id.employee);
        reports = findViewById(R.id.reports);


        // استلام البيانات من LoginActivity
        Intent intent = getIntent();
        String adminName = intent.getStringExtra("name");
        String adminImage = intent.getStringExtra("profile_image");

        // تحديث واجهة المستخدم بالاسم
        adminNameTextView.setText(adminName);

        // تحميل الصورة باستخدام Glide
        Glide.with(this)
                .load(adminImage) // رابط الصورة
                .placeholder(R.drawable.puser_icon) // صورة مؤقتة أثناء التحميل
                .error(R.drawable.puser_icon) // صورة تظهر في حال حدوث خطأ
                .into(adminLogoImageView);

        // جلب بيانات لوحة التحكم
        fetchDashboardData();

        // إضافة التنقل عند الضغط على الأزرار
        addManagerButton.setOnClickListener(v -> {
            Intent addManagerIntent = new Intent(adminDashboard.this, addManager.class);
            startActivity(addManagerIntent);
        });

        addEmployeeButton.setOnClickListener(v -> {
            Intent addEmployeeIntent = new Intent(adminDashboard.this, AddEmployeeActivity.class);
            startActivity(addEmployeeIntent);
        });

        projects.setOnClickListener(v -> {
            Intent showproj = new Intent(adminDashboard.this, ProjectsActivity.class);
            startActivity(showproj);
        });

        addProjectButton.setOnClickListener(v -> {
            Intent addProjectIntent = new Intent(adminDashboard.this, addProject.class);
            startActivity(addProjectIntent);
        });
        employee.setOnClickListener(v -> {
            Intent emp = new Intent(adminDashboard.this, EmployeesActivity.class);
            startActivity(emp);
        });
        reports.setOnClickListener(v -> {
            Intent report = new Intent(adminDashboard.this, ReportsActivity.class);
            startActivity(report);
        });



        settings.setOnClickListener(v -> {
            try {
                Intent settingsIntent = new Intent(adminDashboard.this, SettingActivity.class);
                startActivity(settingsIntent);
            } catch (Exception e) {
                Toast.makeText(adminDashboard.this, "Error starting settings: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        adminLogoImageView.setOnClickListener(v -> {
            Intent profile = new Intent(adminDashboard.this, ManagerProfile.class);
            startActivity(profile);
        });
    }

    private void fetchDashboardData() {
        String url = "http://192.168.1.106/mobile/admin.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        // استخراج البيانات من JSON
                        boolean success = response.getBoolean("success");
                        if (success) {
                            String profileImage = response.getString("profile_image");
                            int totalEmployees = response.getInt("total_employees");
                            int totalProjects = response.getInt("total_projects");
                            int totalReports = response.getInt("total_reports");

                            // تحديث واجهة المستخدم
                            employeesCountTextView.setText(String.valueOf(totalEmployees));
                            projectsCountTextView.setText(String.valueOf(totalProjects));
                            reportsCountTextView.setText(String.valueOf(totalReports));

                            // تحميل الصورة باستخدام Glide
                            Glide.with(this)
                                    .load(profileImage)
                                    .placeholder(R.drawable.puser_icon)
                                    .error(R.drawable.puser_icon)
                                    .into(adminLogoImageView);
                        } else {
                            Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        // إضافة الطلب إلى قائمة الطلبات
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

}
