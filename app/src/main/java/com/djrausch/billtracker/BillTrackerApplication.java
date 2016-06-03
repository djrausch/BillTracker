package com.djrausch.billtracker;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.djrausch.billtracker.models.Migration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BillTrackerApplication extends Application {
    private static Realm realm;
    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    /*public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }*/

    public static Realm getRealm() {
        if (realm == null) {
            RealmConfiguration realmConfig = new RealmConfiguration.Builder(context)
                    .schemaVersion(1) // Must be bumped when the schema changes
                    .migration(new Migration()) // Migration to run instead of throwing an exception
                    .build();

            realm = Realm.getInstance(realmConfig);
        }

        return realm;
    }

    public static void setAlarmSet() {
        SharedPreferences preferences = context.getSharedPreferences(
                "bill_tracker", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("alarm_set", true);
        editor.apply();
    }

    public static boolean isAlarmSet() {
        SharedPreferences preferences = context.getSharedPreferences(
                "bill_tracker", Context.MODE_PRIVATE);
        return preferences.getBoolean("alarm_set", false);
    }
}
