package com.djrausch.billtracker.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.djrausch.billtracker.R
import com.djrausch.billtracker.models.BillPaid
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import org.joda.time.DateTime

class PaidDateRecyclerViewAdapter(context: Context, dates: OrderedRealmCollection<BillPaid>) : RealmRecyclerViewAdapter<BillPaid, PaidDateRecyclerViewAdapter.ViewHolder>(context, dates, true) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var billPaid: BillPaid = data[position]

        holder.paid.text = "Paid " + DateTime(billPaid.date).toString("MMMM d")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_paid, parent, false)
        return PaidDateRecyclerViewAdapter.ViewHolder(v)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var paid: TextView

        init {
            paid = v.findViewById(R.id.bill_paid_text) as TextView
        }
    }

}
