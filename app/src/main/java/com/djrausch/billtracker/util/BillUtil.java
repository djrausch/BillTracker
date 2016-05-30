package com.djrausch.billtracker.util;

import android.util.Log;

import com.djrausch.billtracker.BillTrackerApplication;
import com.djrausch.billtracker.models.Bill;

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

    public static RealmResults<Bill> loadBills(){
        return BillTrackerApplication.getRealm().where(Bill.class).findAllSortedAsync("dueDate");
    }
}