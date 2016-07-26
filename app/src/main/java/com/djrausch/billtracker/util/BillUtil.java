package com.djrausch.billtracker.util;

import com.djrausch.billtracker.JBillTrackerApplication;
import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.models.BillPaid;

import org.joda.time.DateTime;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by white on 7/25/2016.
 */
public class BillUtil {
    public static void markBillPaid(final Bill bill) {
        JBillTrackerApplication.getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                bill.setDueDate(DateUtil.createNextDueDate(bill));
                bill.getPaidDates().add(new BillPaid(new Date()));
            }
        });
    }

    public static void undoMarkBillPaid(final Date date, final Bill bill) {
        JBillTrackerApplication.getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (bill.getPaidDates() != null) {
                    bill.setDueDate(date);
                    bill.getPaidDates().remove(bill.getPaidDates().size() - 1);
                }
            }
        });
    }

    public static RealmResults<Bill> loadBills() {
        return JBillTrackerApplication.getRealm().where(Bill.class).findAllSortedAsync("dueDate");
    }

    public static RealmResults<Bill> loadOneWeekBillsForNotification() {
        return JBillTrackerApplication.getRealm().where(Bill.class).between("dueDate", new DateTime().minusWeeks(1).toDate(), new DateTime().plusWeeks(1).plusDays(1).toDate()).findAll();
    }
}
