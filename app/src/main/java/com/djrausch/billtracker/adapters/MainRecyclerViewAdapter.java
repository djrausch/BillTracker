package com.djrausch.billtracker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djrausch.billtracker.R;
import com.djrausch.billtracker.models.Bill;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class MainRecyclerViewAdapter extends RealmRecyclerViewAdapter<Bill, MainRecyclerViewAdapter.ViewHolder> {

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
        }
    }
}
