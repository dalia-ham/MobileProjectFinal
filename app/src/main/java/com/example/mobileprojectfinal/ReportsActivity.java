package com.example.mobileprojectfinal;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReportsActivity extends AppCompatActivity {

    private TextView totalUsersTextView, totalEmployeesTextView, totalProjectsTextView;
    private RecyclerView reportRecyclerView;
    private EmployeesAdapter employeesAdapter;
    private List<Employee> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        // ربط المكونات
        totalUsersTextView = findViewById(R.id.total_users);
        totalEmployeesTextView = findViewById(R.id.total_employees);
        totalProjectsTextView = findViewById(R.id.total_projects);
        reportRecyclerView = findViewById(R.id.report_recycler_view);

        reportRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        employeeList = new ArrayList<>();
        employeesAdapter = new EmployeesAdapter(this, employeeList);
        reportRecyclerView.setAdapter(employeesAdapter);

        fetchReportData();
    }

    private void fetchReportData() {
        String url = "http://192.168.1.106/mobile/get_reports.php"; // رابط الـ API

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            // تحديث الإحصائيات
                            JSONObject stats = response.getJSONObject("stats");
                            totalUsersTextView.setText("Total Users: " + stats.getInt("total_users"));
                            totalEmployeesTextView.setText("Total Employees: " + stats.getInt("total_employees"));
                            totalProjectsTextView.setText("Total Projects: " + stats.getInt("total_projects"));

                            // تحديث القائمة
                            JSONArray employees = response.getJSONArray("employees");
                            employeeList.clear();
                            for (int i = 0; i < employees.length(); i++) {
                                JSONObject employeeObject = employees.getJSONObject(i);
                                int userId = employeeObject.getInt("user_id");
                                String firstName = employeeObject.getString("first_name");
                                String lastName = employeeObject.getString("last_name");
                                String email = employeeObject.getString("email");
                                String phone = employeeObject.getString("phone");
                                String profileImage = employeeObject.optString("profile_image", null);

                                employeeList.add(new Employee(userId, firstName, lastName, email, phone, profileImage));
                            }
                            employeesAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ReportsActivity.this, "Failed to load report data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ReportsActivity.this, "Error parsing report data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(ReportsActivity.this, "Error fetching report data: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
