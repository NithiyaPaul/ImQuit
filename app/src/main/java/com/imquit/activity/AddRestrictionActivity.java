package com.imquit.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.imquit.R;
import com.imquit.classes.Book;
import com.imquit.db.DbHelper;
import com.imquit.model.UsageModel;
import com.imquit.service.SaveMyAppsService;

import java.util.ArrayList;

public class AddRestrictionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    ArrayList<UsageModel> usageModels;
    EditText timeEditText;
    String packageName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usageModels = HomeActivity.addictedApps;
        setContentView(R.layout.activity_add_restriction);
        Spinner spinner = findViewById(R.id.appSpinner);
        ArrayList<String> appNames = new ArrayList<>();
        for (UsageModel usageModel : usageModels) {
            appNames.add(usageModel.getAppName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, appNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        findViewById(R.id.addButton).setOnClickListener(this);
        timeEditText = findViewById(R.id.timeEditText);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        packageName = usageModels.get(i).getPackageName();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addButton) {
//            Book book = new Book( packageName, timeEditText.getText().toString());
//            book.save();
            new DbHelper(this).insertData( packageName, timeEditText.getText().toString());
            startService(new Intent(this, SaveMyAppsService.class));
        }
    }
}
