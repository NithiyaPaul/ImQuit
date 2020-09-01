package com.imquit.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.imquit.R;
import com.imquit.classes.MyBarDataSet;
import com.imquit.model.UsageModel;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {
    ArrayList<UsageModel> usageModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (HomeActivity.type == 0) {
            setTitle("Daily Report");
        } else if (HomeActivity.type == 1) {
            setTitle("Weekly Report");
        } else if (HomeActivity.type == 2) {
            setTitle("Monthly Report");
        } else if (HomeActivity.type == 0) {
            setTitle("Yearly Report");
        }
        usageModels = HomeActivity.usageModels;
        setContentView(R.layout.activity_graph);
        BarChart barChart = (BarChart) findViewById(R.id.chart);
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
