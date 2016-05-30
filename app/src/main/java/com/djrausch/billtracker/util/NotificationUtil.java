package com.djrausch.billtracker.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.djrausch.billtracker.R;
import com.djrausch.billtracker.models.Bill;

import org.joda.time.DateTime;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by white on 5/29/2016.
 */
public class NotificationUtil {
    public static final String ACTION_MARK_BILL_PAID = "com.djrausch.billtracker.ACTION_MARK_BILL_PAID";
    public static final String KEY_BILL_UUID = "uuid";
    public static final String KEY_NOTIF_ID = "notificationId";
    public static final String GROUP_NAME = "bills";


    public static void makeBillNotification(Context context, Bill bill) {
        Log.d("Bill Notif", bill.toString());
        int notificationId = new NotificationID(context).get();
        Log.d("Make - NotifId", String.valueOf(notificationId));

        Intent i = new Intent(ACTION_MARK_BILL_PAID);
        i.putExtra(KEY_BILL_UUID, bill.uuid);
        i.putExtra(KEY_NOTIF_ID, notificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, new NotificationID(context).get(), i, 0);

        Log.d("PI", pendingIntent.toString());

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(bill.name)
                        .setCategory(Notification.CATEGORY_REMINDER)
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setContentText(context.getString(R.string.notification_due,new DateTime(bill.dueDate).toString("MMMM d")))
                        .addAction(R.drawable.ic_notification, context.getString(R.string.paid), pendingIntent)
                        .setGroup(GROUP_NAME);

        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(notificationId, mBuilder.build());
    }
}
