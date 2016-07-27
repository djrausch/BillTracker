package com.djrausch.billtracker.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by white on 7/25/2016.
 */
public class NotificationID {
    private static final String ATOMIC_ID = "atomicId";
    private Context context;
    private AtomicInteger atomicInt;
    private SharedPreferences preferences;

    public NotificationID(Context context) {
        this.context = context;

        preferences = context.getSharedPreferences("bill_tracker_internal_settings", Context.MODE_PRIVATE);
        int a = preferences.getInt(ATOMIC_ID, 0);
        atomicInt = new AtomicInteger(a);
    }

    public int get() {
        int atomicIntNew = atomicInt.incrementAndGet();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ATOMIC_ID, atomicIntNew);
        editor.apply();
        return atomicIntNew;
    }
}
