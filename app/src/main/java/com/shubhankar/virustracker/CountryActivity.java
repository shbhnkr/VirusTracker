package com.shubhankar.virustracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.Objects;

public class CountryActivity extends AppCompatActivity {
    TextView tvCountry,tvCases,tvRecovered,tvCritical,tvActive,tvTodayCases,tvTotalDeaths,tvTodayDeaths;
    SimpleArcLoader simpleArcLoader;
    ScrollView scrollView;
    PieChart pieChart;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        Intent intent = getIntent();
        pos = intent.getIntExtra("position",0);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Condition of " + CountryAdapter.filtered.get(pos).getCountry());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvCountry = findViewById(R.id.country_name);
        tvCases = findViewById(R.id.cCases);
        tvRecovered = findViewById(R.id.cRecovered);
        tvCritical = findViewById(R.id.cCritical);
        tvActive = findViewById(R.id.cActive);
        tvTodayCases = findViewById(R.id.cTodayCases);
        tvTotalDeaths = findViewById(R.id.cTotalDeaths);
        tvTodayDeaths = findViewById(R.id.cTodayDeaths);
        pieChart = findViewById(R.id.cpiechart);
        simpleArcLoader = findViewById(R.id.loaderCountry);
        scrollView = findViewById(R.id.scrollStatsCountry);

        load();

    }

    private void load() {
        tvCountry.setText(CountryAdapter.filtered.get(pos).getCountry());
        tvCases.setText(CountryAdapter.filtered.get(pos).getCases());
        tvRecovered.setText(CountryAdapter.filtered.get(pos).getRecovered());
        tvCritical.setText(CountryAdapter.filtered.get(pos).getCritical());
        tvActive.setText(CountryAdapter.filtered.get(pos).getActive());
        tvTodayCases.setText(CountryAdapter.filtered.get(pos).getTodayCases());
        tvTotalDeaths.setText(CountryAdapter.filtered.get(pos).getDeaths());
        tvTodayDeaths.setText(CountryAdapter.filtered.get(pos).getTodayDeaths());

        pieChart.addPieSlice(new PieModel("Cases",Integer.parseInt(tvCases.getText().toString()), Color.parseColor("#FFA726")));
        pieChart.addPieSlice(new PieModel("Recoverd",Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(new PieModel("Deaths",Integer.parseInt(tvTotalDeaths.getText().toString()), Color.parseColor("#EF5350")));
        pieChart.addPieSlice(new PieModel("Active",Integer.parseInt(tvActive.getText().toString()), Color.parseColor("#29B6F6")));
        pieChart.startAnimation();
        
        simpleArcLoader.stop();
        simpleArcLoader.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
