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

        projectsRecyclerView = findViewById(R.id.projects_recycler_view);
        searchBar = findViewById(R.id.search_bar);
        projectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        projectList = new ArrayList<>();
        filteredProjectList = new ArrayList<>();
        projectsAdapter = new ProjectsAdapter(this, filteredProjectList);
        projectsRecyclerView.setAdapter(projectsAdapter);

        fetchProjectsFromDatabase();

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
        String url = "http://192.168.1.106/mobile/projects.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        projectList.clear();
                        filteredProjectList.clear();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject projectObject = response.getJSONObject(i);

                            String name = projectObject.getString("name");
                            String description = projectObject.getString("description");
                            String startDate = projectObject.getString("start_date");
                            String endDate = projectObject.getString("end_date");
                            String imageUrl = projectObject.getString("image");

                            Project project = new Project(name, description, startDate, endDate, imageUrl);
                            projectList.add(project);
                        }
                        filteredProjectList.addAll(projectList);
                        projectsAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ProjectsActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(ProjectsActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

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
