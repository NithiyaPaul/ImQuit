package com.imquit.activity;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.imquit.R;
import com.imquit.classes.CustomAdapter;
import com.imquit.db.DbHelper;
import com.imquit.model.UsageModel;
import com.imquit.service.SaveMyAppsService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static int max;
    public static int min;
    static ArrayList<UsageModel> usageModels;
    static int type;
    static ArrayList<UsageModel> addictedApps;
    ArrayList<UsageModel> allSelectedData;
    ListView listView;
    long time;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        allSelectedData = new DbHelper(HomeActivity.this).getAllSelectedData();
        Spinner spinner = findViewById(R.id.intervalSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.interval, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        startService(new Intent(this, SaveMyAppsService.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        type = i;
        if (i == 0) {
            max = 60;
            min = 45;
            Log.e("i", "value :" + i);
            checkRunningApps(UsageStatsManager.INTERVAL_DAILY);
        } else if (i == 1) {
            max = 60 * 7;
            min = 45 * 7;
            //time = 11;
            checkRunningApps(UsageStatsManager.INTERVAL_WEEKLY);
        } else if (i == 2) {
            max = 60 * 30;
            min = 45 * 30;
            time = 42;
            checkRunningApps(UsageStatsManager.INTERVAL_MONTHLY);

        } else if (i == 3) {
            max = 60 * 365;
            min = 45 * 365;
            time = 303;
            checkRunningApps(UsageStatsManager.INTERVAL_YEARLY);

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void checkRunningApps(int i) {
        usageModels = new ArrayList<>();
        addictedApps = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.SECOND, 0);
            List<UsageStats> appList = usm.queryUsageStats(i, cal.getTimeInMillis() , time);
            Log.e("time", cal.getTimeInMillis() + "");

            if (appList.size() == 0) {
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            }
            PackageManager pm = getApplicationContext().getPackageManager();
            ApplicationInfo ai;
            if (appList != null && appList.size() > 0) {
                for (UsageStats usageStats : appList) {

                    for (UsageModel usageModel : allSelectedData) {
                        if (usageModel.getPackageName().equals(usageStats.getPackageName())) {
                            UsageModel usageModel1 = new UsageModel();
                            usageModel1.setPackageName(usageStats.getPackageName());
                            Log.e(usageStats.getPackageName(), usageStats.getTotalTimeInForeground() + "");
                            try {
                                ai = pm.getApplicationInfo(usageStats.getPackageName(), 0);
                            } catch (final PackageManager.NameNotFoundException e) {
                                ai = null;
                            }
                            usageModel.setAppName((String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)"));
                            //Log.e("app name", (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)"));
                            try {
                                usageModel.setDrawable(getPackageManager().getApplicationIcon(usageStats.getPackageName()));
                            } catch (PackageManager.NameNotFoundException e) {
                                usageModel.setDrawable(getDrawable(R.mipmap.ic_launcher));
                                e.printStackTrace();
                            }
                            long l = TimeUnit.MILLISECONDS.toMinutes(usageStats.getTotalTimeInForeground()) + this.time ;
                            usageModel.setTime(l + "");
                            usageModels.add(usageModel);
                            if (Long.parseLong(usageModel.getTime()) > max) {
                                addictedApps.add(usageModel);
                            }
                        }
                    }

                }
            }
        } else {
            ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
        }
        listView = findViewById(R.id.usageListView);
        CustomAdapter customAdapter = new CustomAdapter(this, usageModels);
        listView.setAdapter(customAdapter);
        registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Generate Report");
        menu.add("Addicted Apps");
        menu.add("Select Apps");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Generate Report")) {
            if (HomeActivity.usageModels.size() > 0) {
                startActivity(new Intent(this, GraphActivity.class));
            } else {
                Toast.makeText(this, "No applications available", Toast.LENGTH_SHORT).show();
            }
        } else if (item.getTitle().equals("Addicted Apps")) {
            if (HomeActivity.addictedApps.size() > 0) {
                startActivity(new Intent(this, AddictedAppActivity.class));
            } else {
                Toast.makeText(this, "No addicted applications", Toast.LENGTH_SHORT).show();
            }
        } else if (item.getTitle().equals("Select Apps")) {
            startActivity(new Intent(this, AppSelectionActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Delete");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        startActivity(new Intent(this, DeleteMultipleActivity.class));
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        if (item.getTitle() == "Delete") {
//            DbHelper dbHelper = new DbHelper(this);
//            int i = dbHelper.deleteApp((int) info.id);
//            UsageModel usageModel = usageModels.get(info.position);
//            Log.e("adapter", info.id + "");
//            Log.e("adapter", usageModel.getPackageName());
//            Log.e("adapter", usageModel.getId()  + "");
//            dbHelper.deleteAppSeletct(usageModel.getPackageName());
//            if (i > 0) {
//                Toast.makeText(this, "App deleted", Toast.LENGTH_SHORT).show();
//                checkRunningApps(type);
//
//            } else {
//                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
//            }
//        }
        return true;
    }
}