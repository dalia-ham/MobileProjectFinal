package com.example.mobileprojectfinal;

import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddEmployeeActivity extends AppCompatActivity {

    private EditText firstName, lastName, email, phoneNumber, password, confirmPassword;
    private ImageView profileImage;
    private TextView imageName;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);  // Make sure the XML layout is correct

        // Initialize the UI components
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phone_number);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        profileImage = findViewById(R.id.profile_image);
        imageName = findViewById(R.id.image_name);
        submitButton = findViewById(R.id.submit_button);

        // Handle the profile image selection (you can implement an image picker here)
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open gallery or camera to select profile image
                Toast.makeText(AddEmployeeActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle the submit button click
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String firstNameStr = firstName.getText().toString().trim();
                String lastNameStr = lastName.getText().toString().trim();
                String emailStr = email.getText().toString().trim();
                String phoneNumberStr = phoneNumber.getText().toString().trim();
                String passwordStr = password.getText().toString().trim();
                String confirmPasswordStr = confirmPassword.getText().toString().trim();

                // Validate input
                if (firstNameStr.isEmpty()) {
                    firstName.setError("First name is required");
                    return;
                }
                if (lastNameStr.isEmpty()) {
                    lastName.setError("Last name is required");
                    return;
                }
                if (emailStr.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
                    email.setError("Enter a valid email address");
                    return;
                }
                if (phoneNumberStr.isEmpty()) {
                    phoneNumber.setError("Phone number is required");
                    return;
                }
                if (passwordStr.isEmpty()) {
                    password.setError("Password is required");
                    return;
                }
                if (!passwordStr.equals(confirmPasswordStr)) {
                    confirmPassword.setError("Passwords do not match");
                    return;
                }

                // If all validations pass, show success message and proceed
                Toast.makeText(AddEmployeeActivity.this, "Employee added successfully", Toast.LENGTH_SHORT).show();

                // Proceed with saving the employee data to a database or backend
                // Example: Start another activity or perform database operation
                Intent intent = new Intent(AddEmployeeActivity.this, EmployeeDashboard.class);
                startActivity(intent);

                // Optionally clear the form fields after submission
                clearForm();
            }
        });
    }

    // Clear all form fields
    private void clearForm() {
        firstName.setText("");
        lastName.setText("");
        email.setText("");
        phoneNumber.setText("");
        password.setText("");
        confirmPassword.setText("");
        imageName.setText(R.string.no_image_selected);
    }

}