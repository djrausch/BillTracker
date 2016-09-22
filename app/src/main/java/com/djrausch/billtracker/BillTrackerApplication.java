package com.djrausch.billtracker;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IntegerRes;
import android.util.Log;

import com.bugsnag.android.Bugsnag;
import com.djrausch.billtracker.models.Migration;
import com.djrausch.billtracker.util.AlarmUtil;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by white on 7/25/2016.
 */
public class BillTrackerApplication extends Application {
    private static Context context;
    private boolean alarmSet;

    private static Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Bugsnag.init(context);
        if (!isAlarmSet()) {
            AlarmUtil.setDailyAlarm(context);
        }
    }

    public static Realm getRealm() {
        if (realm == null) {
            RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context).schemaVersion(4)
                    .migration(new Migration())
                    .build();
            realm = Realm.getInstance(realmConfiguration);
        }

        return realm;
    }

    public static void setAlarmSet() {
        SharedPreferences preferences = context.getSharedPreferences("bill_tracker_internal_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("alarm_set", true);
        editor.apply();
    }


    public static boolean isAlarmSet() {
        SharedPreferences preferences = context.getSharedPreferences("bill_tracker_internal_settings", Context.MODE_PRIVATE);
        Log.d("Alarm", "Pref set");
        return preferences.getBoolean("alarm_set", false);
    }

    public static void setUserToken(String token) {
        SharedPreferences preferences = context.getSharedPreferences("bill_tracker_internal_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.user_token), token);
        editor.commit();
    }

    public static String getUserToken() {
        SharedPreferences preferences = context.getSharedPreferences("bill_tracker_internal_settings", Context.MODE_PRIVATE);
        return preferences.getString(context.getString(R.string.user_token), "");
    }

    public static boolean isBillPeekEnabled() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean("bill_peek_enabled", true);
    }

    public static int getBillPeekDays() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(sharedPref.getString("bill_peek_days", "7"));
    }

    public static void setSyncTime() {
        long mills = System.currentTimeMillis();
        SharedPreferences preferences = context.getSharedPreferences("bill_tracker_internal_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("sync_time", mills);
        editor.commit();
    }

    public static long getSyncTime() {
        SharedPreferences preferences = context.getSharedPreferences("bill_tracker_internal_settings", Context.MODE_PRIVATE);
        return preferences.getLong("sync_time", System.currentTimeMillis());
    }
}
