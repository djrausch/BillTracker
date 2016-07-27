package com.djrausch.billtracker.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.util.BillNotificationManager;
import com.djrausch.billtracker.util.BillUtil;

import io.realm.RealmResults;

/**
 * Created by white on 7/25/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        RealmResults<Bill> bills = BillUtil.loadOneWeekBillsForNotification();
        new BillNotificationManager(context).makeBillNotification(bills);
    }
}
