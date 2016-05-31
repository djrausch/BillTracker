package com.djrausch.billtracker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djrausch.billtracker.R;
import com.djrausch.billtracker.events.BillSwipedEvent;
import com.djrausch.billtracker.itemtouchhelpers.ItemTouchHelperAdapter;
import com.djrausch.billtracker.itemtouchhelpers.ItemTouchHelperViewHolder;
import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.util.BillUtil;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Date;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class MainRecyclerViewAdapter extends RealmRecyclerViewAdapter<Bill, MainRecyclerViewAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    public MainRecyclerViewAdapter(Context context, OrderedRealmCollection<Bill> bills) {
        super(context, bills);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_bill, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bill bill = adapterData.get(position);

        holder.name.setText(bill.name);
        DateTime dateTime = new DateTime(bill.dueDate);
        int days = Days.daysBetween(new DateTime(), dateTime).getDays();
        holder.dueInDays.setText(String.valueOf(days));
        holder.dueDate.setText(dateTime.toString("MMMM d"));
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        Date oldDueDate = adapterData.get(position).dueDate;

        Bill bill = adapterData.get(position);

        BillUtil.markBillPaid(bill);

        EventBus.getDefault().post(new BillSwipedEvent(oldDueDate, bill));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {
        public TextView name;
        public TextView dueInDays;
        public TextView dueDate;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            dueInDays = (TextView) v.findViewById(R.id.due_in_days);
            dueDate = (TextView) v.findViewById(R.id.due_date);
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }
}
