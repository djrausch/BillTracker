package com.djrausch.billtracker.util

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.util.Log

import com.djrausch.billtracker.R
import com.djrausch.billtracker.models.Bill

import org.joda.time.DateTime

import io.realm.RealmResults

/**
 * Created by white on 5/29/2016.
 */
object NotificationUtil {
    val ACTION_MARK_BILL_PAID = "com.djrausch.billtracker.ACTION_MARK_BILL_PAID"
    val ACTION_PAY_BILL = "com.djrausch.billtracker.ACTION_PAY_BILL"
    val KEY_BILL_UUID = "uuid"
    val KEY_NOTIF_ID = "notificationId"
    val GROUP_NAME = "bills"


    fun makeBillNotification(context: Context, bills: RealmResults<Bill>) {
        if (bills.size > 1) {
            val inboxStyle = NotificationCompat.InboxStyle()
            for (bill in bills) {
                inboxStyle.addLine(bill.name + " " + context.getString(R.string.notification_due, DateTime(bill.dueDate).toString("MMMM d")))
            }
            //Create Summary Notification
            val mBuilder = NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_notification).setContentTitle(context.getString(R.string.notification_summary_title)).setCategory(Notification.CATEGORY_REMINDER).setColor(context.resources.getColor(R.color.colorPrimary)).setStyle(inboxStyle).setGroup(GROUP_NAME).setGroupSummary(true)

            val mNotifyMgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotifyMgr.notify(NotificationID(context).get(), mBuilder.build())
        }
        for (bill in bills) {
            makeBillNotification(context, bill)
        }
    }

    fun makeBillNotification(context: Context, bill: Bill) {
        Log.d("Bill Notif", bill.toString())
        val notificationId = NotificationID(context).get()
        Log.d("Make - NotifId", notificationId.toString())


        //Mark Bill paid Intents
        val markBillPaidIntent = Intent(ACTION_MARK_BILL_PAID)
        markBillPaidIntent.putExtra(KEY_BILL_UUID, bill.uuid)
        markBillPaidIntent.putExtra(KEY_NOTIF_ID, notificationId)

        val markBillPaidPI = PendingIntent.getBroadcast(context, NotificationID(context).get(), markBillPaidIntent, 0)
        //End mark bill paid intents

        //Pay bill intent
        val payBillIntent = Intent(Intent.ACTION_VIEW)
        payBillIntent.data = Uri.parse(bill.payUrl)

        val payBillPI = PendingIntent.getActivity(context, NotificationID(context).get(), payBillIntent, 0)
        //End pay bill intent


        Log.d("PI", markBillPaidPI.toString())

        val mBuilder = NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_notification).setContentTitle(bill.name).setCategory(Notification.CATEGORY_REMINDER).setColor(context.resources.getColor(R.color.colorPrimary)).setContentText(context.getString(R.string.notification_due, DateTime(bill.dueDate).toString("MMMM d"))).addAction(R.drawable.ic_notification, context.getString(R.string.notification_action_paid), markBillPaidPI).setGroup(GROUP_NAME)

        if (bill.payUrl != null && bill.payUrl != "") {
            Log.d("PayUrl", bill.payUrl)
            mBuilder.addAction(R.drawable.ic_notification, context.getString(R.string.pay), payBillPI)
        }

        val mNotifyMgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotifyMgr.notify(notificationId, mBuilder.build())

    }
}
