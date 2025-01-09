package com.example.mobileprojectfinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.EmployeeViewHolder> {

    private List<Employee> employeeList;
    private List<Employee> filteredList;

    public EmployeesAdapter(List<Employee> employeeList) {
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
        holder.positionTextView.setText(employee.getPosition());
        holder.salaryTextView.setText("Salary: $" + employee.getSalary());
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
                        employee.getLastName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(employee);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, emailTextView, phoneTextView, positionTextView, salaryTextView;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.employee_name);
            emailTextView = itemView.findViewById(R.id.employee_email);
            phoneTextView = itemView.findViewById(R.id.employee_phone);
            positionTextView = itemView.findViewById(R.id.employee_position);
            salaryTextView = itemView.findViewById(R.id.employee_salary);
        }
    }
}
