package com.djrausch.billtracker.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.djrausch.billtracker.BillTrackerApplication
import com.djrausch.billtracker.models.Bill
import com.djrausch.billtracker.util.AlarmUtil
import com.djrausch.billtracker.util.BillUtil
import com.djrausch.billtracker.util.NotificationUtil

import org.joda.time.DateTime

import java.util.Date

import io.realm.RealmResults

/**
 * Created by white on 5/29/2016.
 */
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //Find upcoming bills to show notificaiton for
        //Bill b = BillTrackerApplication.getRealm().where(Bill.class).findFirst();
        //RealmResults<Bill> bills = BillTrackerApplication.getRealm().where(Bill.class).findAll();
        //Find bills in the next week.
        val bills1 = BillUtil.loadOneWeekBillsForNotification()
        NotificationUtil.makeBillNotification(context, bills1)

    }
}
