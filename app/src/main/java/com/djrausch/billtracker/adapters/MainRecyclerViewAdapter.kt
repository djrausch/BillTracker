package com.djrausch.billtracker.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.djrausch.billtracker.R
import com.djrausch.billtracker.events.BillSwipedEvent
import com.djrausch.billtracker.itemtouchhelpers.ItemTouchHelperAdapter
import com.djrausch.billtracker.itemtouchhelpers.ItemTouchHelperViewHolder
import com.djrausch.billtracker.models.Bill
import com.djrausch.billtracker.util.BillUtil
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import org.greenrobot.eventbus.EventBus
import org.joda.time.DateTime
import org.joda.time.Days

class MainRecyclerViewAdapter(context: Context, bills: OrderedRealmCollection<Bill>) : RealmRecyclerViewAdapter<Bill, MainRecyclerViewAdapter.ViewHolder>(context, bills, true), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_bill, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bill = data[position]

        holder.name.text = bill.name
        val dateTime = DateTime(bill.dueDate)
        val days = Days.daysBetween(DateTime(), dateTime).days
        holder.dueInDays.text = days.toString()
        holder.dueDate.text = dateTime.toString("MMMM d")
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        return false
    }

    override fun onItemDismiss(position: Int) {
        val oldDueDate = data[position].dueDate

        val bill = data[position]

        BillUtil.markBillPaid(bill)

        EventBus.getDefault().post(BillSwipedEvent(oldDueDate, bill))
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), ItemTouchHelperViewHolder {
        var name: TextView
        var dueInDays: TextView
        var dueDate: TextView

        init {
            name = v.findViewById(R.id.name) as TextView
            dueInDays = v.findViewById(R.id.due_in_days) as TextView
            dueDate = v.findViewById(R.id.due_date) as TextView
        }

        override fun onItemSelected() {

        }

        override fun onItemClear() {

        }
    }
}
