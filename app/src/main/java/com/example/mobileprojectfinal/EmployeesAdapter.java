package com.example.mobileprojectfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public  class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.EmployeeViewHolder> {

    private List<Employee> employeeList;
    private List<Employee> filteredList;
    private Context context;

    public EmployeesAdapter(Context context, List<Employee> employeeList) {
        this.context = context;
        this.employeeList = employeeList;
        this.filteredList = new ArrayList<>(employeeList);
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = filteredList.get(position);
        holder.nameTextView.setText(employee.getFirstName() + " " + employee.getLastName());
        holder.emailTextView.setText(employee.getEmail());
        holder.phoneTextView.setText(employee.getPhone());

        // التحقق من وجود صورة
        String imageUrl = employee.getProfileImage();
        if (imageUrl == null || imageUrl.isEmpty()) {
            holder.profileImageView.setImageResource(R.drawable.puser_icon);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load("http://192.168.1.106/mobile/" + imageUrl)
                    .into(holder.profileImageView);
        }
    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(employeeList);
        } else {
            for (Employee employee : employeeList) {
                if (employee.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                        employee.getLastName().toLowerCase().contains(query.toLowerCase()) ||
                        employee.getEmail().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(employee);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, emailTextView, phoneTextView;
        ImageView profileImageView;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.employee_name);
            emailTextView = itemView.findViewById(R.id.employee_email);
            phoneTextView = itemView.findViewById(R.id.employee_phone);
            profileImageView = itemView.findViewById(R.id.profile_image);
        }
    }
}
