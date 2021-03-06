package com.djrausch.billtracker.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.djrausch.billtracker.BillTrackerApplication;
import com.djrausch.billtracker.receivers.AlarmReceiver;

import java.util.Calendar;

/**
 * Created by white on 7/26/2016.
 */
public class AlarmUtil {
    public static final int ALARM_PI_ID = 100;

    public static void setDailyAlarm(Context context) {
        Log.d("Alarm", "Setting Daily Alarm");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, ALARM_PI_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 50);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

        BillTrackerApplication.setAlarmSet();
    }
}
