package com.djrausch.billtracker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

import com.djrausch.billtracker.models.Migration

import io.realm.Realm
import io.realm.RealmConfiguration

class BillTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        private var _realm: Realm? = null
        private var context: Context? = null

        /*public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }*/

        fun getRealm(): Realm {
            if (_realm == null) {
                val realmConfig = RealmConfiguration.Builder(context!!).schemaVersion(1) // Must be bumped when the schema changes
                        .migration(Migration()) // Migration to run instead of throwing an exception
                        .build()

                _realm = Realm.getInstance(realmConfig)
            }

            return _realm!!
        }

        fun setAlarmSet() {
            val preferences = context!!.getSharedPreferences(
                    "bill_tracker", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putBoolean("alarm_set", true)
            editor.apply()
        }

        val isAlarmSet: Boolean
            get() {
                val preferences = context!!.getSharedPreferences(
                        "bill_tracker", Context.MODE_PRIVATE)
                return preferences.getBoolean("alarm_set", false)
            }
    }
}
