package com.imquit.activity;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.imquit.R;
import com.imquit.classes.AppSelectionAdapter;
import com.imquit.classes.CustomAdapter;
import com.imquit.db.DbHelper;
import com.imquit.model.UsageModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AppSelectionActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<UsageModel> usageModels;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_selection);
        checkRunningApps(UsageStatsManager.INTERVAL_YEARLY);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void checkRunningApps(int i) {
        usageModels = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.SECOND, 0);
            List<UsageStats> appList = usm.queryUsageStats(i, cal.getTimeInMillis(), time);
            Log.e("time", cal.getTimeInMillis() + "");

            if (appList.size() == 0) {
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            }

            ApplicationInfo ai;
            PackageManager pm = getApplicationContext().getPackageManager();
            List<ApplicationInfo> installedApplications = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            for (ApplicationInfo applicationInfo : installedApplications) {
                UsageModel usageModel = new UsageModel();
                usageModel.setPackageName(applicationInfo.packageName);
                try {
                    ai = pm.getApplicationInfo(applicationInfo.packageName, 0);
                } catch (final PackageManager.NameNotFoundException e) {
                    ai = null;
                }
                usageModel.setAppName((String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)"));
                //Log.e("app name", (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)"));
                try {
                    usageModel.setDrawable(getPackageManager().getApplicationIcon(applicationInfo.packageName));
                } catch (PackageManager.NameNotFoundException e) {
                    usageModel.setDrawable(getDrawable(R.mipmap.ic_launcher));
                    e.printStackTrace();
                }
                //usageModel.setTime(TimeUnit.MILLISECONDS.toMinutes(usageStats.getTotalTimeInForeground()) + "");
                usageModels.add(usageModel);
            }

//            if (appList != null && appList.size() > 0) {
//                for (UsageStats usageStats : appList) {
//                    UsageModel usageModel = new UsageModel();
//                    usageModel.setPackageName(usageStats.getPackageName());
//                    Log.e(usageStats.getPackageName(), usageStats.getTotalTimeInForeground() + "");
//                    try {
//                        ai = pm.getApplicationInfo(usageStats.getPackageName(), 0);
//                    } catch (final PackageManager.NameNotFoundException e) {
//                        ai = null;
//                    }
//                    usageModel.setAppName((String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)"));
//                    //Log.e("app name", (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)"));
//                    try {
//                        usageModel.setDrawable(getPackageManager().getApplicationIcon(usageStats.getPackageName()));
//                    } catch (PackageManager.NameNotFoundException e) {
//                        usageModel.setDrawable(getDrawable(R.mipmap.ic_launcher));
//                        e.printStackTrace();
//                    }
//                    usageModel.setTime(TimeUnit.MILLISECONDS.toMinutes(usageStats.getTotalTimeInForeground()) + "");
//                    usageModels.add(usageModel);
//
//                }
//            }
//        } else {
//            ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
//        }

            ArrayList<UsageModel> allSelectedData = new DbHelper(AppSelectionActivity.this).getAllSelectedData();
            for (UsageModel usageModel : allSelectedData) {
                for (int j = 0; j < usageModels.size(); j++) {
                    if (usageModel.getPackageName().equals(usageModels.get(j).getPackageName())) {
                        usageModels.remove(j);
                    }
                }
            }
            ListView listView = findViewById(R.id.appSelectListView);
            AppSelectionAdapter appSelectionAdapter = new AppSelectionAdapter(AppSelectionActivity.this, usageModels);
            listView.setAdapter(appSelectionAdapter);
            findViewById(R.id.saveAppButton).setOnClickListener(AppSelectionActivity.this);
        }

    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.saveAppButton) {
            for (String packageNames : AppSelectionAdapter.packageNames) {
                Log.e("package", packageNames);
                new DbHelper(AppSelectionActivity.this).insertSelectedAppData(packageNames);
            }
            startActivity(new Intent(this, HomeActivity.class));
        }
    }
}
