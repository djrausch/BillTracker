package com.djrausch.billtracker;

import android.app.Application;
import android.content.Context;

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
            RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
            realm = Realm.getInstance(realmConfig);
        }

        return realm;
    }
}
