package com.djrausch.billtracker.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.atomic.AtomicInteger;

public class NotificationID {
    private AtomicInteger atomicInt = null;
    private SharedPreferences preferences;

    public NotificationID(Context context) {
        preferences = context.getSharedPreferences(
                "bill_tracker", Context.MODE_PRIVATE);
        int a = preferences.getInt("atomicId", 0);
        atomicInt = new AtomicInteger(a);
    }

    public int get() {
        int atomicIntNew = atomicInt.incrementAndGet();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("atomicId", atomicIntNew);
        editor.apply();
        return atomicIntNew;
    }
}