package com.djrausch.billtracker.util;

import android.util.Log;

import com.djrausch.billtracker.enums.Repeating;
import com.djrausch.billtracker.models.Bill;

import org.joda.time.DateTime;

import java.util.Date;

public class DateUtil {
    public static Date createNextDueDate(Bill bill) {
        DateTime dateTime = new DateTime(bill.dueDate);
        Log.d("createNext", String.format("DateTime %s", dateTime.toString()));
        Repeating repeating = Repeating.values()[bill.repeatingType];
        Log.d("Repeating", repeating.name());
        switch (repeating) {
            case None:
                //Dont change the due date as it will never repeat
                return bill.dueDate;
            case Daily:
                //Add one day to due date.
                return dateTime.plusDays(1).toDate();
            case Weekly:
                return dateTime.plusWeeks(1).toDate();
            case Monthly:
                return dateTime.plusMonths(1).toDate();
            case Yearly:
                return dateTime.plusYears(1).toDate();
            default:
                return bill.dueDate;
        }
    }
}
