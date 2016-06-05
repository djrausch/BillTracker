package com.djrausch.billtracker.util

import android.content.Context
import android.content.SharedPreferences

import java.util.concurrent.atomic.AtomicInteger

class NotificationID(context: Context) {
    private var atomicInt: AtomicInteger? = null
    private val preferences: SharedPreferences

    init {
        preferences = context.getSharedPreferences(
                "bill_tracker_internal_settings", Context.MODE_PRIVATE)
        val a = preferences.getInt("atomicId", 0)
        atomicInt = AtomicInteger(a)
    }

    fun get(): Int {
        val atomicIntNew = atomicInt!!.incrementAndGet()
        val editor = preferences.edit()
        editor.putInt("atomicId", atomicIntNew)
        editor.apply()
        return atomicIntNew
    }
}