package com.djrausch.billtracker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djrausch.billtracker.R;
import com.djrausch.billtracker.models.BillPaid;

import org.joda.time.DateTime;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by white on 7/25/2016.
 */
public class PaidDateRecyclerViewAdapter extends RealmRecyclerViewAdapter<BillPaid, PaidDateRecyclerViewAdapter.ViewHolder> {


    public PaidDateRecyclerViewAdapter(Context context, OrderedRealmCollection<BillPaid> data) {
        super(context, data, true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_paid, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BillPaid billPaid = getData().get(position);

        holder.paid.setText(String.format("Paid %s", new DateTime(billPaid.date).toString("MMMM d")));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView paid;

        public ViewHolder(View itemView) {
            super(itemView);
            paid = (TextView) itemView.findViewById(R.id.bill_paid_text);
        }
    }
}
