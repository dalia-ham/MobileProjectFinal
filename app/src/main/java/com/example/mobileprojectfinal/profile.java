package com.example.mobileprojectfinal;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class profile extends AppCompatActivity {

    private TextView profileNameTextView, userEmailTextView;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileNameTextView = findViewById(R.id.profile_name);
        userEmailTextView = findViewById(R.id.user_email);
        profileImageView = findViewById(R.id.profile_image);

        int userId = getIntent().getIntExtra("user_id", 0);

        if (userId == 0) {
            Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d("User ID", "Received User ID: " + userId);

        fetchUserProfile(userId);
    }

    private void fetchUserProfile(int userId) {
        String url = "http://192.168.1.106/mobile/getUserProfile.php?user_id=" + userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("API Response", "Response: " + response.toString());

                            if (response.has("error")) {
                                Toast.makeText(profile.this, response.getString("error"), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            String firstName = response.optString("first_name", "Unknown");
                            String lastName = response.optString("last_name", "Unknown");
                            String email = response.optString("email", "Not provided");
                            String profileImage = response.optString("profile_image", "default.png");

                            profileNameTextView.setText(firstName + " " + lastName);
                            userEmailTextView.setText(email);

                            Picasso.get()
                                    .load("http://192.168.1.106/mobile/image/" + profileImage)
                                    .placeholder(R.drawable.user_icon)
                                    .error(R.drawable.puser_icon)
                                    .into(profileImageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(profile.this, "Error parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(profile.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}