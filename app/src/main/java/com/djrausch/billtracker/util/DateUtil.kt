package com.djrausch.billtracker.util

import android.util.Log
import com.djrausch.billtracker.models.Bill
import com.djrausch.billtracker.models.RepeatingItem
import org.joda.time.DateTime
import java.util.*

object DateUtil {
    fun createNextDueDate(bill: Bill): Date {
        val dateTime = DateTime(bill.dueDate)
        Log.d("createNext", String.format("DateTime %s", dateTime.toString()))
        when (bill.repeatingType) {
            RepeatingItem.CODE_NEVER -> return bill.dueDate!!
            RepeatingItem.CODE_DAILY -> return dateTime.plusDays(1).toDate()
            RepeatingItem.CODE_WEEKLY -> return dateTime.plusWeeks(1).toDate()
            RepeatingItem.CODE_MONTHLY -> return dateTime.plusMonths(1).toDate()
            RepeatingItem.CODE_BI_YEARLY -> return dateTime.plusMonths(6).toDate()
            RepeatingItem.CODE_YEARLY -> return dateTime.plusYears(1).toDate()
            else -> return bill.dueDate!!
        }
    }
}
