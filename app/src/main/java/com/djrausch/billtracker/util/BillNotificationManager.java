package com.djrausch.billtracker.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.djrausch.billtracker.R;
import com.djrausch.billtracker.models.Bill;

import org.joda.time.DateTime;

import io.realm.RealmResults;

/**
 * Created by white on 7/25/2016.
 */
public class BillNotificationManager {
    private Context context;

    public static final String ACTION_MARK_BILL_PAID = "com.djrausch.billtracker.ACTION_MARK_BILL_PAID";
    public static final String ACTION_PAY_BILL = "com.djrausch.billtracker.ACTION_PAY_BILL";
    public static final String KEY_BILL_UUID = "uuid";
    public static final String KEY_NOTIF_ID = "notificationId";
    public static final String GROUP_NAME = "bills";
    public static final String NOTIF_ENABLED_KEY = "notifications_enabled";

    private boolean enabled = true;

    SharedPreferences preferences;

    public BillNotificationManager(Context context) {
        this.context = context;

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        enabled = preferences.getBoolean(NOTIF_ENABLED_KEY, true);
    }

    public void makeBillNotification(RealmResults<Bill> bills) {
        if (enabled) {
            if (bills.size() > 1) {
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                for (Bill bill : bills) {
                    inboxStyle.addLine(bill.name + " " + context.getString(R.string.notification_due, new DateTime(bill.dueDate).toString("MMMM d")));
                }
                //Create summary notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(context.getString(R.string.notification_summary_title))
                        .setCategory(Notification.CATEGORY_REMINDER)
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setStyle(inboxStyle)
                        .setGroup(GROUP_NAME)
                        .setGroupSummary(true);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(new NotificationID(context).get(), builder.build());

                for (Bill bill : bills) {

                }
            }
        }
    }

    public void makeBillNotification(Bill bill) {
        if (enabled) {
            int notificationId = new NotificationID(context).get();

            //Mark Bill Paid Intents
            Intent markBillPaidIntent = new Intent(ACTION_MARK_BILL_PAID);
            markBillPaidIntent.putExtra(KEY_BILL_UUID, bill.getUuid());
            markBillPaidIntent.putExtra(KEY_NOTIF_ID, notificationId);

            PendingIntent markBillPaidPI = PendingIntent.getBroadcast(context, new NotificationID(context).get(), markBillPaidIntent, 0);
            //End Mark bill paid intents

            //Pay bill intents
            Intent payBillIntent = new Intent(Intent.ACTION_VIEW);
            payBillIntent.setData(Uri.parse(bill.getPayUrl()));

            PendingIntent payBillPI = PendingIntent.getActivity(context, new NotificationID(context).get(), payBillIntent, 0);
            //End pay bill intents

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(bill.getName())
                    .setCategory(Notification.CATEGORY_REMINDER)
                    .setColor(context.getResources().getColor(R.color.colorPrimary))
                    .setContentText(context.getString(R.string.notification_due, new DateTime(bill.dueDate).toString("MMMM d")))
                    .addAction(R.drawable.ic_notification, context.getString(R.string.notification_action_paid), markBillPaidPI)
                    .setGroup(GROUP_NAME);

            //Set ringtone
            String ringtone = preferences.getString("notifications_ringtone", "");
            if (!ringtone.equals("")) {
                builder.setSound(Uri.parse(ringtone));
            }
            //End set ringtone

            //Set Vibrate
            boolean vibrate = preferences.getBoolean("notifications_vibrate", true);
            if (vibrate) {
                long[] vib = {0, 300, 0};
                builder.setVibrate(vib);
            }
            //End Set Vibrate

            //Set LED
            boolean led = preferences.getBoolean("notifications_light", true);
            if (led) {
                builder.setLights(0xffffff, 2000, 2000);
            }
            //End set LED

            //Set pay url
            if (bill.getPayUrl() != null && !bill.getPayUrl().equals("")) {
                builder.addAction(R.drawable.ic_notification, context.getString(R.string.pay), payBillPI);
            }
            //End set pay url

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationId, builder.build());
        }
    }
}
