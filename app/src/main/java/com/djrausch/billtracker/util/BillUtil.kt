package com.djrausch.billtracker.util

import android.util.Log

import com.djrausch.billtracker.BillTrackerApplication
import com.djrausch.billtracker.models.Bill

import org.joda.time.DateTime

import java.util.Date

import io.realm.RealmResults

object BillUtil {
    fun markBillPaid(bill: Bill) {
        BillTrackerApplication.getRealm().beginTransaction()
        bill.dueDate = DateUtil.createNextDueDate(bill)
        BillTrackerApplication.getRealm().commitTransaction()

        Log.d("BillPaid", bill.toString())
    }

    fun undoMarkBillPaid(oldDate: Date, bill: Bill) {
        BillTrackerApplication.getRealm().beginTransaction()
        bill.dueDate = oldDate
        BillTrackerApplication.getRealm().commitTransaction()
    }

    fun loadBills(): RealmResults<Bill> {
        return BillTrackerApplication.getRealm().where(Bill::class.java).findAllSortedAsync("dueDate")
    }

    fun loadOneWeekBillsForNotification(): RealmResults<Bill> {
        return BillTrackerApplication.getRealm().where(Bill::class.java).between("dueDate", DateTime().minusWeeks(1).toDate(), DateTime().plusWeeks(1).plusDays(1).toDate()).findAll()
    }
}
