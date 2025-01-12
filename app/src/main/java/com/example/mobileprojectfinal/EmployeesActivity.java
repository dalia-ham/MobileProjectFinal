package com.example.mobileprojectfinal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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

public class EmployeesActivity extends AppCompatActivity {

    private RecyclerView employeesRecyclerView;
    private EmployeesAdapter employeesAdapter;
    private List<Employee> employeeList;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);

        // ربط المكونات
        employeesRecyclerView = findViewById(R.id.employees_recycler_view);
        searchBar = findViewById(R.id.search_bar);
        employeesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // إنشاء قائمة الموظفين
        employeeList = new ArrayList<>();
        employeesAdapter = new EmployeesAdapter(this, employeeList);
        employeesRecyclerView.setAdapter(employeesAdapter);

        // جلب البيانات من قاعدة البيانات
        fetchEmployeesFromDatabase();

        // البحث في القائمة
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                employeesAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void fetchEmployeesFromDatabase() {
        String url = "http://192.168.1.106/mobile/get_employees.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            JSONArray data = response.getJSONArray("data");
                            employeeList.clear(); // تنظيف القائمة قبل الإضافة
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject employeeObject = data.getJSONObject(i);

                                // جلب البيانات مع التحقق
                                int userId = employeeObject.getInt("user_id");
                                String firstName = employeeObject.optString("first_name", "N/A");
                                String lastName = employeeObject.optString("last_name", "N/A");
                                String email = employeeObject.optString("email", "No Email");
                                String phone = employeeObject.optString("phone", "No Phone");
                                String profileImage = employeeObject.optString("profile_image", null);

                                // إضافة الموظف إلى القائمة
                                employeeList.add(new Employee(userId, firstName, lastName, email, phone, profileImage));
                            }
                            employeesAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(EmployeesActivity.this, "No employees found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(EmployeesActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(EmployeesActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }


}
