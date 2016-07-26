package com.djrausch.billtracker.util;

import com.djrausch.billtracker.BillTrackerApplication;
import com.djrausch.billtracker.models.Bill;

import java.util.Date;

import io.realm.RealmResults;

/**
 * Created by white on 7/25/2016.
 */
public class JBillUtil {
    public static void markBillPaid(Bill bill) {

    }

    public static void undoMarkBillPaid(Date date, Bill bill) {

    }

    public static RealmResults<Bill> loadBills(){
        return null;
    }

    public static RealmResults<Bill> loadOneWeekBillsForNotification(){
        return null;
    }
}
