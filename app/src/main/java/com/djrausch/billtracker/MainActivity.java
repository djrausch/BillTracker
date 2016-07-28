package com.djrausch.billtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.djrausch.billtracker.adapters.MainRecyclerViewAdapter;
import com.djrausch.billtracker.events.BillSwipedEvent;
import com.djrausch.billtracker.itemtouchhelpers.ItemClickSupport;
import com.djrausch.billtracker.itemtouchhelpers.OnStartDragListener;
import com.djrausch.billtracker.itemtouchhelpers.SimpleItemTouchHelperCallback;
import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.util.AlarmUtil;
import com.djrausch.billtracker.util.BillUtil;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements OnStartDragListener {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.no_bills)
    LinearLayout noBillsLayout;
    @BindView(R.id.main_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    private MainRecyclerViewAdapter adapter;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jmain);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-1464692828868203~1013938599");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("D68C46B15DA6D66612423C3526F0F16C").build();
        mAdView.loadAd(adRequest);


        setClickListeners();
        configureRecyclerView();
    }

    private void setClickListeners() {
        final Intent i = new Intent(MainActivity.this, AddOrEditBillActivity.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });

        noBillsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
    }

    private void configureRecyclerView() {
        RealmResults<Bill> bills = BillUtil.loadBills();

        bills.addChangeListener(new RealmChangeListener<RealmResults<Bill>>() {
            @Override
            public void onChange(RealmResults<Bill> element) {
                if (adapter.getItemCount() == 0) {
                    noBillsLayout.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    noBillsLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new MainRecyclerViewAdapter(this, bills);
        recyclerView.setAdapter(adapter);

        SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i = new Intent(MainActivity.this, ViewBillDetails.class);
                i.putExtra("edit", true);
                i.putExtra("bill_uuid", adapter.getItem(position).getUuid());
                startActivity(i);
            }
        });
    }

    @Subscribe
    public void onBillSwiped(final BillSwipedEvent billSwipedEvent) {
        Snackbar.make(coordinatorLayout, getString(R.string.snackbar_bill_paid, billSwipedEvent.bill.getName()), Snackbar.LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillUtil.undoMarkBillPaid(billSwipedEvent.oldDate, billSwipedEvent.bill);
            }
        }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


}
