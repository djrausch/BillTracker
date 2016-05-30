package com.djrausch.billtracker.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.djrausch.billtracker.BillTrackerApplication;
import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.util.NotificationUtil;

import org.joda.time.DateTime;

import java.util.Date;

import io.realm.RealmResults;

/**
 * Created by white on 5/29/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Find upcoming bills to show notificaiton for
        //Bill b = BillTrackerApplication.getRealm().where(Bill.class).findFirst();
        RealmResults<Bill> bills = BillTrackerApplication.getRealm().where(Bill.class).findAll();
        //Find bills in the next week.
        RealmResults<Bill> bills1 = BillTrackerApplication.getRealm().where(Bill.class).between("dueDate", new Date(), new DateTime().plusWeeks(1).toDate()).findAll();
        NotificationUtil.makeBillNotification(context, bills1);

    }
}
