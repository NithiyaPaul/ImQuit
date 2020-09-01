package com.imquit.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.imquit.R;
import com.imquit.classes.MyBarDataSet;
import com.imquit.model.UsageModel;

import java.util.ArrayList;

public class AddictedAppActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<UsageModel> usageModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usageModels = HomeActivity.addictedApps;
        setContentView(R.layout.activity_addicted_app);
        setTitle("Addicted Apps");
        findViewById(R.id.addRestrictionButton).setOnClickListener(this);
        if (usageModels.size() > 0) {
            BarChart barChart = findViewById(R.id.addictedAppChart);
            ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
            ArrayList<String> labels = new ArrayList<String>();
            for (int i = 0; i < usageModels.size(); i++) {
                entries.add(new BarEntry(Float.parseFloat(usageModels.get(i).getTime()), i));
                labels.add(usageModels.get(i).getAppName());
            }
            MyBarDataSet set = new MyBarDataSet(entries, "");
            set.setColors(new int[]{ContextCompat.getColor(this, android.R.color.holo_green_light), ContextCompat.getColor(this, android.R.color.holo_orange_dark), ContextCompat.getColor(this, android.R.color.holo_red_dark)});
            BarData data = new BarData(labels, set);
            barChart.setData(data); // set the data and list of lables into chart
            barChart.setDescription("Time in minute");
            barChart.animateY(5000);
        }
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(this, AddRestrictionActivity.class));
    }
}
