package com.djrausch.billtracker.util

import android.util.Log

import com.djrausch.billtracker.BillTrackerApplication
import com.djrausch.billtracker.models.Bill
import com.djrausch.billtracker.models.BillPaid

import org.joda.time.DateTime

import java.util.Date

import io.realm.RealmResults

object BillUtil {
    fun markBillPaid(bill: Bill) {
        BillTrackerApplication.getRealm().executeTransaction {
            bill.dueDate = DateUtil.createNextDueDate(bill)
            bill.paidDates?.add(BillPaid(Date()))
        }

        Log.d("BillPaid", bill.toString())
    }

    fun undoMarkBillPaid(oldDate: Date, bill: Bill) {

        BillTrackerApplication.getRealm().executeTransaction {
            bill.dueDate = oldDate
            bill.paidDates?.removeAt(bill.paidDates!!.size - 1)
        }
    }

    fun loadBills(): RealmResults<Bill> {
        return BillTrackerApplication.getRealm().where(Bill::class.java).findAllSortedAsync("dueDate")
    }

    fun loadOneWeekBillsForNotification(): RealmResults<Bill> {
        return BillTrackerApplication.getRealm().where(Bill::class.java).between("dueDate", DateTime().minusWeeks(1).toDate(), DateTime().plusWeeks(1).plusDays(1).toDate()).findAll()
    }
}
