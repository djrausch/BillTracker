package com.djrausch.billtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djrausch.billtracker.adapters.PaidDateRecyclerViewAdapter;
import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.models.RepeatingItem;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class ViewBillDetails extends AppCompatActivity {

    private String billUuid;

    PaidDateRecyclerViewAdapter adapter;
    @BindView(R.id.bill_paid_recycler_view)
    public RecyclerView recyclerView;
    @BindView(R.id.bill_name)
    public TextView billName;
    @BindView(R.id.bill_repeat)
    public TextView repeat;
    @BindView(R.id.bill_next_due)
    public TextView due;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill_details);
        ButterKnife.bind(this);

        loadBillAndListen();

        RelativeLayout adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);

        adView = new AdView(this, "316563612047695_316574695379920", AdSize.BANNER_320_50);
        adViewContainer.addView(adView);
        adView.loadAd();
    }

    private void loadBillAndListen() {
        billUuid = getIntent().getStringExtra("bill_uuid");

        Observable<Bill> billObservable = BillTrackerApplication.getRealm().where(Bill.class).contains("uuid", billUuid).findFirst().asObservable();

        billObservable.filter(new Func1<Bill, Boolean>() {
            @Override
            public Boolean call(Bill bill) {
                return bill.isLoaded();
            }
        }).subscribe(new Action1<Bill>() {
            @Override
            public void call(Bill bill) {
                setUI(bill);

                if (bill.paidDates != null) {
                    if (adapter == null) {
                        adapter = new PaidDateRecyclerViewAdapter(ViewBillDetails.this, bill.getPaidDates());
                        recyclerView.setLayoutManager(new LinearLayoutManager(ViewBillDetails.this));
                        recyclerView.setAdapter(adapter);
                    }
                }
            }
        });
    }

    private void setUI(Bill bill) {
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
