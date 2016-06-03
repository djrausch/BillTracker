package com.djrausch.billtracker.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.djrausch.billtracker.R;
import com.djrausch.billtracker.models.Bill;

import org.joda.time.DateTime;

import io.realm.RealmResults;

/**
 * Created by white on 5/29/2016.
 */
public class NotificationUtil {
    public static final String ACTION_MARK_BILL_PAID = "com.djrausch.billtracker.ACTION_MARK_BILL_PAID";
    public static final String ACTION_PAY_BILL = "com.djrausch.billtracker.ACTION_PAY_BILL";
    public static final String KEY_BILL_UUID = "uuid";
    public static final String KEY_NOTIF_ID = "notificationId";
    public static final String GROUP_NAME = "bills";


    public static void makeBillNotification(Context context, RealmResults<Bill> bills) {
        if (bills.size() > 1) {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            for (Bill bill : bills) {
                inboxStyle.addLine(bill.name + " " + context.getString(R.string.notification_due, new DateTime(bill.dueDate).toString("MMMM d")));
            }
            //Create Summary Notification
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle(context.getString(R.string.notification_summary_title))
                            .setCategory(Notification.CATEGORY_REMINDER)
                            .setColor(context.getResources().getColor(R.color.colorPrimary))
                            .setStyle(inboxStyle)
                            .setGroup(GROUP_NAME)
                            .setGroupSummary(true);

            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyMgr.notify(new NotificationID(context).get(), mBuilder.build());
        }
        for (Bill bill : bills) {
            makeBillNotification(context, bill);
        }
    }

    public static void makeBillNotification(Context context, Bill bill) {
        Log.d("Bill Notif", bill.toString());
        int notificationId = new NotificationID(context).get();
        Log.d("Make - NotifId", String.valueOf(notificationId));


        //Mark Bill paid Intents
        Intent markBillPaidIntent = new Intent(ACTION_MARK_BILL_PAID);
        markBillPaidIntent.putExtra(KEY_BILL_UUID, bill.uuid);
        markBillPaidIntent.putExtra(KEY_NOTIF_ID, notificationId);

        PendingIntent markBillPaidPI = PendingIntent.getBroadcast(context, new NotificationID(context).get(), markBillPaidIntent, 0);
        //End mark bill paid intents

        //Pay bill intent
        Intent payBillIntent = new Intent(Intent.ACTION_VIEW);
        payBillIntent.setData(Uri.parse(bill.payUrl));

        PendingIntent payBillPI = PendingIntent.getActivity(context, new NotificationID(context).get(), payBillIntent, 0);
        //End pay bill intent


        Log.d("PI", markBillPaidPI.toString());

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(bill.name)
                        .setCategory(Notification.CATEGORY_REMINDER)
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setContentText(context.getString(R.string.notification_due, new DateTime(bill.dueDate).toString("MMMM d")))
                        .addAction(R.drawable.ic_notification, context.getString(R.string.notification_action_paid), markBillPaidPI)
                        .setGroup(GROUP_NAME);

        if (bill.payUrl != null && !bill.payUrl.equals("")) {
            Log.d("PayUrl", bill.payUrl);
            mBuilder.addAction(R.drawable.ic_notification, context.getString(R.string.pay), payBillPI);
        }

        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(notificationId, mBuilder.build());

    }
}
