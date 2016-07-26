package com.djrausch.billtracker.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.djrausch.billtracker.JBillTrackerApplication;
import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.util.BillNotificationManager;
import com.djrausch.billtracker.util.BillUtil;

/**
 * Created by white on 7/26/2016.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            String billUuid = intent.getStringExtra(BillNotificationManager.KEY_BILL_UUID);
            int notificationId = intent.getIntExtra(BillNotificationManager.KEY_NOTIF_ID, 0);

            String action = intent.getAction();

            Bill bill = JBillTrackerApplication.getRealm().where(Bill.class).contains("uuid", billUuid).findFirst();

            if (action.equals(BillNotificationManager.ACTION_MARK_BILL_PAID)) {
                BillUtil.markBillPaid(bill);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(notificationId);
            }
        }
    }
}
