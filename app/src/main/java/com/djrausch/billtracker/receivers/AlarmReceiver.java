package com.djrausch.billtracker.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.djrausch.billtracker.BillTrackerApplication;
import com.djrausch.billtracker.R;
import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.util.NotificationUtil;

/**
 * Created by white on 5/29/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Find upcoming bills to show notificaiton for
        Bill b = BillTrackerApplication.getRealm().where(Bill.class).findFirst();
        NotificationUtil.makeBillNotification(context, b);


    }
}
