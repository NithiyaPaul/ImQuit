package com.imquit.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.imquit.service.SaveMyAppsService;


public class MyBroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Service Stops", "Ohhhhhhh");
        context.startService(new Intent(context, SaveMyAppsService.class));
    }
}
