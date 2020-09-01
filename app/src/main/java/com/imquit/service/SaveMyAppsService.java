package com.imquit.service;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.imquit.activity.BlockActivity;
import com.imquit.db.DbHelper;
import com.imquit.model.UsageModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SaveMyAppsService extends Service {

    public static SaveMyAppsService instance;
    String currentApp = "";
    String lastAppPN = "";
    boolean noDelay = false;
    DbHelper dbHelper;
    ArrayList<UsageModel> allData;
    public static void stop() {
        if (instance != null) {
            instance.stopSelf();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        scheduleMethod();
//        CURRENT_PACKAGE_NAME = getApplicationContext().getPackageName();
//        Log.e("Current PN", "" + CURRENT_PACKAGE_NAME);
        dbHelper = new DbHelper(getBaseContext());
        allData = dbHelper.getAllData();
        instance = this;

        return START_STICKY;
    }

    private void scheduleMethod() {
        // TODO Auto-generated method stub

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                // This method will check for the Running apps after every 100ms

                checkRunningApps();
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void checkRunningApps() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // intentionally using string value as Context.USAGE_STATS_SERVICE was
            // strangely only added in API 22 (LOLLIPOP_MR1)
            @SuppressWarnings("WrongConstant") UsageStatsManager usm = (UsageStatsManager) getSystemService("usagestats");
            long time = System.currentTimeMillis();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            long timeInMillis = cal.getTimeInMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, timeInMillis, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    //Log.e("1", currentApp);
                    long value = TimeUnit.MILLISECONDS.toMinutes(mySortedMap.get(mySortedMap.lastKey()).getTotalTimeInForeground());
                    //Log.e("2", TimeUnit.MILLISECONDS.toMinutes(mySortedMap.get(mySortedMap.lastKey()).getTotalTimeInForeground()) + "");
                    for (UsageModel usageModel : allData) {
                        //Log.e(usageModel.getPackageName(), usageModel.getTime());
                        if (currentApp.contains(usageModel.getPackageName()) &&  value  > Long.parseLong(usageModel.getTime())) {
                            Intent dialogIntent = new Intent(getBaseContext(), BlockActivity.class);
                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(dialogIntent);
                        }
                    }
                }
            }
        } else {
            ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            //Log.e("2", tasks.get(0).processName);
        }
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("onRemoved", "sdff");
        stopSelf();
        Intent intent = new Intent("com.imquit");
        sendBroadcast(intent);
    }

    public void onDestroy() {
        Log.e("onDestroy", "sdff");
        Intent intent = new Intent();
        intent.setAction("com.imquit.Custom"); sendBroadcast(intent);

    }
}