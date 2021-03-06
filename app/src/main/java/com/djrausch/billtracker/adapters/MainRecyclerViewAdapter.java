package com.djrausch.billtracker.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.djrausch.billtracker.R;
import com.djrausch.billtracker.events.BillSwipedEvent;
import com.djrausch.billtracker.itemtouchhelpers.ItemTouchHelperAdapter;
import com.djrausch.billtracker.itemtouchhelpers.ItemTouchHelperViewHolder;
import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.models.BillPaid;
import com.djrausch.billtracker.util.BillUtil;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.NumberFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by white on 7/25/2016.
 */
public class MainRecyclerViewAdapter extends RealmRecyclerViewAdapter<Bill, MainRecyclerViewAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    public MainRecyclerViewAdapter(Context context, OrderedRealmCollection<Bill> data) {
        super(context, data, true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bill, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bill bill = getData().get(position);

        holder.name.setText(bill.getName());
        DateTime dateTime = new DateTime(bill.getDueDate());
        int days = Days.daysBetween(new DateTime(), dateTime).getDays();

        if (bill.amountDue > 0) {
            String amountDue = NumberFormat.getCurrencyInstance().format(bill.amountDue / 100d);
            holder.amountAndDueDate.setText(String.format("%s on %s", amountDue, dateTime.toString("MMMM d")));
        } else {
            holder.amountAndDueDate.setText(dateTime.toString("MMMM d"));
        }

        if (days < 0) {
            holder.card.setCardBackgroundColor(Color.parseColor("#E57373"));
            int daysAgo = Math.abs(days);
            holder.dueTopLabel.setText(context.getString(R.string.due));
            holder.dueInDays.setText(String.valueOf(daysAgo));
            holder.dueBottomLabel.setText(daysAgo > 1 ? context.getString(R.string.days_ago) : context.getString(R.string.day_ago));
        } else {
            holder.card.setCardBackgroundColor(Color.WHITE);
            holder.dueTopLabel.setText(context.getString(R.string.due_in));
            holder.dueInDays.setText(String.valueOf(days));
            holder.dueBottomLabel.setText(days == 1 ? context.getString(R.string.day) : context.getString(R.string.days));
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        Date oldDate = getData().get(position).getDueDate();

        Bill bill = getData().get(position);
        BillPaid billPaid = BillUtil.markBillPaid(bill);

        EventBus.getDefault().post(new BillSwipedEvent(oldDate, bill, billPaid));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        @BindView(R.id.card_view)
        public CardView card;

        @BindView(R.id.name)
        public TextView name;

        @BindView(R.id.due_in_days)
        public TextView dueInDays;

        @BindView(R.id.amount_and_due_date)
        public TextView amountAndDueDate;

        @BindView(R.id.due_top_label)
        public TextView dueTopLabel;

        @BindView(R.id.due_bottom_label)
        public TextView dueBottomLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }
}
