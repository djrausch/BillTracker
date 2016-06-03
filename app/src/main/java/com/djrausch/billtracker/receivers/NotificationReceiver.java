package com.djrausch.billtracker.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.djrausch.billtracker.BillTrackerApplication;
import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.util.BillUtil;
import com.djrausch.billtracker.util.NotificationUtil;

/**
 * Created by white on 5/29/2016.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("onReceive", intent.getAction());

        if (intent.getExtras() != null && intent.getExtras().containsKey(NotificationUtil.KEY_BILL_UUID)) {
            String billUuid = intent.getExtras().getString(NotificationUtil.KEY_BILL_UUID);
            int notificationId = intent.getExtras().getInt(NotificationUtil.KEY_NOTIF_ID);

            String action = intent.getAction();

            Bill b = BillTrackerApplication.getRealm().where(Bill.class).contains("uuid", billUuid).findFirst();

            if (action.equals(NotificationUtil.ACTION_MARK_BILL_PAID)) {
                Log.d("Receive - NotifId", String.valueOf(notificationId));

                Log.d(TAG, "onReceive: " + intent.getExtras().getString(NotificationUtil.KEY_BILL_UUID));

                BillUtil.markBillPaid(b);

                NotificationManager mNotifyMgr =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotifyMgr.cancel(notificationId);

            }
        }
    }
}
