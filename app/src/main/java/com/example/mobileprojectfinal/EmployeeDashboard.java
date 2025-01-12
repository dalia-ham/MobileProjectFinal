package com.example.mobileprojectfinal;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDashboard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmployeesAdapter employeeAdapter;
    private List<Employee> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);

        recyclerView = findViewById(R.id.recycler_view_today_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        employeeList = new ArrayList<>();

        fetchEmployeeData();
    }

    private void fetchEmployeeData() {
        String url = "http:/192.168.1.106/mobile/get_employees.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray dataArray = response.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject jsonObject = dataArray.getJSONObject(i);

                                    int userId = jsonObject.getInt("user_id");
                                    String firstName = jsonObject.getString("first_name");
                                    String lastName = jsonObject.getString("last_name");
                                    String email = jsonObject.getString("email");
                                    String phone = jsonObject.getString("phone");
                                    String profileImage = jsonObject.getString("profile_image");

                                    Employee employee = new Employee(userId, firstName, lastName, email, phone, profileImage);
                                    employeeList.add(employee);
                                }

                                // تمرير السياق والقائمة
                                employeeAdapter = new EmployeesAdapter(EmployeeDashboard.this, employeeList);
                                recyclerView.setAdapter(employeeAdapter);
                            } else {
                                Toast.makeText(EmployeeDashboard.this, "No employees found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EmployeeDashboard.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmployeeDashboard.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

}
