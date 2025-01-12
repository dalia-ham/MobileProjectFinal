package com.example.mobileprojectfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class ManagerProfile extends AppCompatActivity {
    TextView name, email;
    ImageView profileImage;
    ListView profileListView;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_profile);

        // ربط العناصر بالواجهة
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        profileImage = findViewById(R.id.imageView2);
        profileListView = findViewById(R.id.ProfileListView);
        requestQueue = Volley.newRequestQueue(this);

        // استلام البيانات من Intent
        Intent intent = getIntent();
        String nameText = intent.getStringExtra("name");
        String profileImageUrl = intent.getStringExtra("profile_image");

        // تعيين البيانات المستلمة إلى الواجهات
        name.setText(nameText);
        Glide.with(ManagerProfile.this).load(profileImageUrl).into(profileImage);

        // استعلام لتحميل بيانات الملف الشخصي من الخادم (بناءً على user_id مثلاً)
        String url = "http://192.168.1.106/mobile/profile.php?user_id=1"; // تأكد من تعديل الرابط حسب الحاجة

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // استخراج البيانات من الاستجابة
                            JSONObject user = response.getJSONObject("user");
                            email.setText(user.getString("email"));
                            // يمكنك إضافة المزيد من الحقول هنا حسب البيانات المتوفرة في الاستجابة
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ManagerProfile.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(ManagerProfile.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
