package com.djrausch.billtracker.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.djrausch.billtracker.util.AlarmUtil;

/**
 * Created by white on 7/26/2016.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") || intent.getAction().equals("android.intent.action.MY_PACKAGE_REPLACED")) {
            AlarmUtil.setDailyAlarm(context);
        }
    }
}
