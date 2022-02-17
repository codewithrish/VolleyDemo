package com.codewithrish.volleydemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static String url = "https://gorest.co.in/public/v2/users";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        loadData();
    }

    private void loadData() {
        progressDialog.show();
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Pojo>>(){}.getType();
                        List<Pojo> data = gson.fromJson(response, listType);

                        RecyclerView recyclerView = findViewById(R.id.recyclerView);
                        MyAdapter adapter = new MyAdapter(data, MainActivity.this);
                        recyclerView.setAdapter(adapter);
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: ", error);
                        progressDialog.dismiss();
                    }
                });
        // MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        queue.add(stringRequest);
    }
}