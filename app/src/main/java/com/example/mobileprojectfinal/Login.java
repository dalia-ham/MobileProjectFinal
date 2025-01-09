package com.example.mobileprojectfinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private ProgressBar progressBar;
    private static final String TAG = "LoginActivity"; // للتمييز في السجلات

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ربط عناصر الواجهة
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);

        // مستمع زر تسجيل الدخول
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    Log.d(TAG, "Input Data - Email: " + email + ", Password: " + password);
                    loginUser(email, password);
                } else {
                    Toast.makeText(Login.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser(String email, String password) {
        String url = "http://192.168.1.106/mobile/login.php"; // استبدل بـ 10.0.2.2 إذا كنت تعمل على المحاكي

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    loginButton.setEnabled(true);
                    Log.d(TAG, "Server Response: " + response);

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            String role = jsonResponse.getString("role");
                            String name = jsonResponse.getString("name");
                            String profileImage = jsonResponse.getString("profile_image");

                            // التوجيه بناءً على الدور
                            Intent intent;
                            if (role.equalsIgnoreCase("Admin")) {
                                intent = new Intent(Login.this, adminDashboard.class);
                            } else if (role.equalsIgnoreCase("Manager")) {
                                intent = new Intent(Login.this, managerDashboard.class);
                            } else {
                                intent = new Intent(Login.this, EmployeeDashboard.class);
                            }

                            intent.putExtra("name", name);
                            intent.putExtra("profile_image", profileImage);
                            startActivity(intent);
                        } else {
                            String message = jsonResponse.getString("message");
                            Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Login.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    loginButton.setEnabled(true);
                    Log.e(TAG, "Volley Error: " + error.toString());
                    Toast.makeText(Login.this, "Login failed, please try again", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                Log.d(TAG, "Params Sent: " + params.toString());
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
