package com.djrausch.billtracker.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.djrausch.billtracker.util.AlarmUtil

/**
 * Created by white on 5/29/2016.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED" || intent.action == "android.intent.action.MY_PACKAGE_REPLACED") {
            AlarmUtil.setDailyAlarm(context)
        }
    }
}
