package com.djrausch.billtracker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djrausch.billtracker.BillTrackerApplication;
import com.djrausch.billtracker.R;
import com.djrausch.billtracker.events.BillClickedEvent;
import com.djrausch.billtracker.events.BillSwipedEvent;
import com.djrausch.billtracker.itemtouchhelpers.ItemTouchHelperAdapter;
import com.djrausch.billtracker.itemtouchhelpers.ItemTouchHelperViewHolder;
import com.djrausch.billtracker.models.Bill;

import org.greenrobot.eventbus.EventBus;

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
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bill bill = adapterData.get(position);

        holder.name.setText(bill.toString());
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        Date oldDueDate = adapterData.get(position).dueDate;

        Bill bill = adapterData.get(position);

        BillTrackerApplication.getRealm().beginTransaction();
        bill.dueDate = new Date();
        BillTrackerApplication.getRealm().commitTransaction();

        EventBus.getDefault().post(new BillSwipedEvent(oldDueDate, bill));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {
        public TextView name;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }
}
