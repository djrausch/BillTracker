package com.djrausch.billtracker.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

import com.djrausch.billtracker.BillTrackerApplication
import com.djrausch.billtracker.receivers.AlarmReceiver

import java.util.Calendar

/**
 * Created by white on 5/29/2016.
 */
object AlarmUtil {
    val ALARM_PI_ID = 100

    fun setDailyAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, ALARM_PI_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT)


        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 50)

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY, alarmIntent)

        BillTrackerApplication.setAlarmSet()
    }
}
