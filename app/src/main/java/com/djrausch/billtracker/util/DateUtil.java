package com.djrausch.billtracker.util;

import android.util.Log;

import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.models.RepeatingItem;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by white on 7/25/2016.
 */
public class DateUtil {

    public static Date createNextDueDate(Bill bill) {
        DateTime dateTime = new DateTime(bill.getDueDate());

        Log.d("CreateNext", String.format("DateTime %s", dateTime.toString()));

        switch (bill.repeatingType) {
            case RepeatingItem.CODE_NEVER:
                return bill.dueDate;
            case RepeatingItem.CODE_DAILY:
                return dateTime.plusDays(1).toDate();
            case RepeatingItem.CODE_WEEKLY:
                return dateTime.plusWeeks(1).toDate();
            case RepeatingItem.CODE_MONTHLY:
                return dateTime.plusMonths(1).toDate();
            case RepeatingItem.CODE_BI_YEARLY:
                return dateTime.plusMonths(6).toDate();
            case RepeatingItem.CODE_YEARLY:
                return dateTime.plusYears(1).toDate();
            default:
                return bill.dueDate;
        }
    }
}
