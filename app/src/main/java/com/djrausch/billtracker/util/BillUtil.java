package com.djrausch.billtracker.util;

import android.util.Log;

import com.djrausch.billtracker.BillTrackerApplication;
import com.djrausch.billtracker.models.Bill;

import org.joda.time.DateTime;

import java.util.Date;

import io.realm.RealmResults;

public class BillUtil {
    public static void markBillPaid(Bill bill) {
        BillTrackerApplication.getRealm().beginTransaction();
        bill.dueDate = DateUtil.createNextDueDate(bill);
        BillTrackerApplication.getRealm().commitTransaction();

        Log.d("BillPaid", bill.toString());
    }

    public static void undoMarkBillPaid(Date oldDate, Bill bill) {
        BillTrackerApplication.getRealm().beginTransaction();
        bill.dueDate = oldDate;
        BillTrackerApplication.getRealm().commitTransaction();
    }

    public static RealmResults<Bill> loadBills() {
        return BillTrackerApplication.getRealm().where(Bill.class).findAllSortedAsync("dueDate");
    }

    public static RealmResults<Bill> loadOneWeekBillsForNotification() {
        return BillTrackerApplication.getRealm().where(Bill.class).between("dueDate", new DateTime().minusWeeks(1).toDate(), new DateTime().plusWeeks(1).plusDays(1).toDate()).findAll();
    }
}
