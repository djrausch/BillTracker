package com.djrausch.billtracker;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
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
            RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context).schemaVersion(2)
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
        Log.d("Alarm","Pref set");
        return preferences.getBoolean("alarm_set", false);
    }
}
