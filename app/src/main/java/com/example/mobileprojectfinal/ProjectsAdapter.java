package com.example.mobileprojectfinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder> {

    private List<Project> projectList;

    public ProjectsAdapter(List<Project> projectList) {
        this.projectList = projectList;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projectList.get(position);
        holder.projectNameTextView.setText(project.getName());
        holder.projectDescriptionTextView.setText(project.getDescription());
        holder.projectStartDateTextView.setText("Start: " + project.getStartDate());
        holder.projectEndDateTextView.setText("End: " + project.getEndDate());

        // تحميل الصورة باستخدام Glide
        Glide.with(holder.itemView.getContext())
                .load(project.getImageUrl())
                .into(holder.projectImageView);
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView projectNameTextView, projectDescriptionTextView, projectStartDateTextView, projectEndDateTextView;
        ImageView projectImageView;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            projectNameTextView = itemView.findViewById(R.id.project_name);
            projectDescriptionTextView = itemView.findViewById(R.id.project_description);
            projectStartDateTextView = itemView.findViewById(R.id.project_start_date);
            projectEndDateTextView = itemView.findViewById(R.id.project_end_date);
            projectImageView = itemView.findViewById(R.id.project_image);
        }
    }
}
