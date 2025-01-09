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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProjectsActivity extends AppCompatActivity {

    private RecyclerView projectsRecyclerView;
    private ProjectsAdapter projectsAdapter;
    private List<Project> projectList;
    private List<Project> filteredProjectList;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        // ربط RecyclerView وخانة البحث
        projectsRecyclerView = findViewById(R.id.projects_recycler_view);
        searchBar = findViewById(R.id.search_bar);
        projectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // إنشاء قوائم المشاريع
        projectList = new ArrayList<>();
        filteredProjectList = new ArrayList<>();
        projectsAdapter = new ProjectsAdapter(filteredProjectList);
        projectsRecyclerView.setAdapter(projectsAdapter);

        // جلب المشاريع من قاعدة البيانات
        fetchProjectsFromDatabase();

        // إضافة مستمع للنص في خانة البحث
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProjects(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void fetchProjectsFromDatabase() {
        String url = "http://192.168.1.106/mobile/projects.php"; // رابط API الذي يعيد المشاريع

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            projectList.clear();
                            filteredProjectList.clear();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject projectObject = response.getJSONObject(i);

                                // جلب البيانات من JSON
                                String name = projectObject.getString("name");
                                String description = projectObject.getString("description");
                                String startDate = projectObject.getString("start_date");
                                String endDate = projectObject.getString("end_date");
                                String imageUrl = projectObject.getString("image");

                                // أضف المشروع إلى القائمة
                                Project project = new Project(name, description, startDate, endDate, imageUrl);
                                projectList.add(project);
                            }
                            // نسخ جميع المشاريع إلى القائمة المعروضة مبدئيًا
                            filteredProjectList.addAll(projectList);
                            projectsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProjectsActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProjectsActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // إضافة الطلب إلى قائمة الطلبات
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void filterProjects(String query) {
        filteredProjectList.clear();
        if (query.isEmpty()) {
            filteredProjectList.addAll(projectList);
        } else {
            for (Project project : projectList) {
                if (project.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredProjectList.add(project);
                }
            }
        }
        projectsAdapter.notifyDataSetChanged();
    }
}
