package com.djrausch.billtracker.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

import com.djrausch.billtracker.BillTrackerApplication
import com.djrausch.billtracker.models.Bill
import com.djrausch.billtracker.util.BillUtil
import com.djrausch.billtracker.util.NotificationUtil

/**
 * Created by white on 5/29/2016.
 */
class NotificationReceiver : BroadcastReceiver() {
    private val TAG = "NotificationReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("onReceive", intent.action)

        if (intent.extras != null) {
            val billUuid = intent.getStringExtra(NotificationUtil.KEY_BILL_UUID)
            val notificationId = intent.getIntExtra(NotificationUtil.KEY_NOTIF_ID, 0)

            val action = intent.action

            val b = BillTrackerApplication.getRealm().where(Bill::class.java).contains("uuid", billUuid).findFirst()

            if (action == NotificationUtil.ACTION_MARK_BILL_PAID) {
                Log.d("Receive - NotifId", notificationId.toString())

                Log.d(TAG, "onReceive: " + intent.getStringExtra(NotificationUtil.KEY_BILL_UUID)!!)

                BillUtil.markBillPaid(b)

                val mNotifyMgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                mNotifyMgr.cancel(notificationId)

            }
        }
    }
}
