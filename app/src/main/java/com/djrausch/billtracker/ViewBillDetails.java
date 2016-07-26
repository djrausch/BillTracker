package com.djrausch.billtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.djrausch.billtracker.adapters.PaidDateRecyclerViewAdapter;
import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.models.RepeatingItem;

import org.joda.time.DateTime;

import io.realm.RealmChangeListener;
import io.realm.RealmModel;

public class ViewBillDetails extends AppCompatActivity {

    private Bill bill;
    private String billUuid;

    PaidDateRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private TextView billName;
    private TextView repeat;
    private TextView due;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jview_bill_details);

        recyclerView = (RecyclerView) findViewById(R.id.bill_paid_recycler_view);
        billName = (TextView) findViewById(R.id.bill_name);
        repeat = (TextView) findViewById(R.id.bill_repeat);
        due = (TextView) findViewById(R.id.bill_next_due);

        loadBillAndListen();
    }

    private void loadBillAndListen() {
        billUuid = getIntent().getStringExtra("bill_uuid");

        bill = BillTrackerApplication.getRealm().where(Bill.class).contains("uuid", billUuid).findFirstAsync();
        bill.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (bill.paidDates != null) {
                    if (adapter == null) {
                        adapter = new PaidDateRecyclerViewAdapter(ViewBillDetails.this, bill.getPaidDates());
                        recyclerView.setLayoutManager(new LinearLayoutManager(ViewBillDetails.this));
                        recyclerView.setAdapter(adapter);
                    }
                }
                setUI();
            }
        });
    }

    private void setUI() {
        billName.setText(bill.getName());
        repeat.setText(getString(R.string.repeats_view_bill, RepeatingItem.convertCodeToString(this, bill.repeatingType)));
        due.setText(getString(R.string.next_due_date_view_bill, new DateTime(bill.dueDate).toString("MMMM d")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_bill_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            Intent i = new Intent(ViewBillDetails.this, AddOrEditBillActivity.class);
            i.putExtra("edit", true);
            i.putExtra("bill_uuid", billUuid);
            startActivityForResult(i, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (intent.getBooleanExtra("delete", false)) {
                    ViewBillDetails.this.finish();
                }
            }
        }
    }
}
