package com.djrausch.billtracker;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.bugsnag.android.Bugsnag;
import com.djrausch.billtracker.models.Migration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by white on 7/25/2016.
 */
public class JBillTrackerApplication extends Application {
    private static Context context;
    private boolean alarmSet;

    private static Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Bugsnag.init(context);
        if (!isAlarmSet()) {
            //TODO
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

    public void setAlarm() {
        SharedPreferences preferences = context.getSharedPreferences("bill_tracker_internal_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("alarm_set", true);
        editor.apply();
    }


    public boolean isAlarmSet() {
        SharedPreferences preferences = context.getSharedPreferences("bill_tracker_internal_settings", Context.MODE_PRIVATE);
        return preferences.getBoolean("alarm_set", false);
    }
}
