package com.djrausch.billtracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
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

import com.bugsnag.android.Bugsnag;
import com.djrausch.billtracker.adapters.MainRecyclerViewAdapter;
import com.djrausch.billtracker.events.BillSwipedEvent;
import com.djrausch.billtracker.itemtouchhelpers.ItemClickSupport;
import com.djrausch.billtracker.itemtouchhelpers.OnStartDragListener;
import com.djrausch.billtracker.itemtouchhelpers.SimpleItemTouchHelperCallback;
import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.util.AlarmUtil;
import com.djrausch.billtracker.util.BillUtil;
import com.djrausch.billtracker.util.NotificationUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements OnStartDragListener {

    @BindView(R.id.main_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.no_bills)
    LinearLayout noBillsView;

    MainRecyclerViewAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bugsnag.init(this);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, AddOrEditBillActivity.class));
                }
            });
        }

        noBillsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddOrEditBillActivity.class));
            }
        });

        configureRecyclerView();
        if (!BillTrackerApplication.isAlarmSet()) {
            AlarmUtil.setDailyAlarm(this);
        }
    }

    private void configureRecyclerView() {

        RealmResults<Bill> bills = BillUtil.loadBills();
        bills.addChangeListener(new RealmChangeListener<RealmResults<Bill>>() {
            @Override
            public void onChange(RealmResults<Bill> element) {
                if (adapter.getItemCount() == 0) {
                    noBillsView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    noBillsView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MainRecyclerViewAdapter(this, bills);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i = new Intent(MainActivity.this, ViewBillActivity.class);
                i.putExtra("bill_uuid", adapter.getItem(position).uuid);
                startActivity(i);

                //NotificationUtil.makeBillNotification(MainActivity.this, adapter.getItem(position));
            }
        });
    }

    @Subscribe
    public void onBillSwiped(final BillSwipedEvent billSwipedEvent) {
        Snackbar.make(coordinatorLayout, getString(R.string.snackbar_bill_paid, billSwipedEvent.bill.name), Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*BillTrackerApplication.getRealm().beginTransaction();
                        billSwipedEvent.bill.dueDate = billSwipedEvent.oldDate;
                        BillTrackerApplication.getRealm().commitTransaction();*/
                        BillUtil.undoMarkBillPaid(billSwipedEvent.oldDate, billSwipedEvent.bill);
                    }
                }).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
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
