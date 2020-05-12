package com.shubhankar.virustracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView tvCases, tvRecovered, tvCritical, tvActive, tvTodayCases, tvTotalDeaths, tvTodayDeaths, tvAffectedCountries;
    private RequestQueue requestQueue;
    SimpleArcLoader simpleArcLoader;
    ScrollView scrollView;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCases = findViewById(R.id.tvCases);
        tvRecovered = findViewById(R.id.tvRecovered);
        tvCritical = findViewById(R.id.tvCritical);
        tvActive = findViewById(R.id.tvActive);
        tvTodayCases = findViewById(R.id.tvTodayCases);
        tvTotalDeaths = findViewById(R.id.tvTotalDeaths);
        tvTodayDeaths = findViewById(R.id.tvTodayDeaths);
        tvAffectedCountries = findViewById(R.id.tvAffectedCountries);
        simpleArcLoader = findViewById(R.id.loader);
        pieChart = findViewById(R.id.piechart);
        scrollView = findViewById(R.id.scrollStats);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        load();
    }
    private void load()
    {
        // Create a String request
        // using Volley Library

        String url = "https://corona.lmao.ninja/v2/all";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tvCases.setText(
                            response.getString(
                                    "cases"));
                    tvRecovered.setText(
                            response.getString(
                                    "recovered"));
                    tvCritical.setText(
                            response.getString(
                                    "critical"));
                    tvActive.setText(
                            response.getString(
                                    "active"));
                    tvTodayCases.setText(
                            response.getString(
                                    "todayCases"));
                    tvTotalDeaths.setText(
                            response.getString(
                                    "deaths"));
                    tvTodayDeaths.setText(
                            response.getString(
                                    "todayDeaths"));
                    tvAffectedCountries.setText(
                            response.getString(
                                    "affectedCountries"));
                    pieChart.addPieSlice(new PieModel("Cases",Integer.parseInt(tvCases.getText().toString()), Color.parseColor("#FFA726")));
                    pieChart.addPieSlice(new PieModel("Recoverd",Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#66BB6A")));
                    pieChart.addPieSlice(new PieModel("Deaths",Integer.parseInt(tvTotalDeaths.getText().toString()), Color.parseColor("#EF5350")));
                    pieChart.addPieSlice(new PieModel("Active",Integer.parseInt(tvActive.getText().toString()), Color.parseColor("#29B6F6")));
                    pieChart.startAnimation();

                    simpleArcLoader.stop();
                    simpleArcLoader.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                }
                catch (JSONException e) {
                    Log.e("c19", "json data error");
                    simpleArcLoader.stop();
                    simpleArcLoader.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(
                                MainActivity.this,
                                error.getMessage(),
                                Toast.LENGTH_SHORT)
                                .show();
                        simpleArcLoader.stop();
                        simpleArcLoader.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                    }
                });
        requestQueue.add(request);
    }

}

