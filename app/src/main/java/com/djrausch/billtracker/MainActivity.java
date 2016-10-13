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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djrausch.billtracker.adapters.MainRecyclerViewAdapter;
import com.djrausch.billtracker.events.BillSwipedEvent;
import com.djrausch.billtracker.itemtouchhelpers.ItemClickSupport;
import com.djrausch.billtracker.itemtouchhelpers.OnStartDragListener;
import com.djrausch.billtracker.itemtouchhelpers.SimpleItemTouchHelperCallback;
import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.network.controllers.BillApi;
import com.djrausch.billtracker.util.BillUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;

import java.text.NumberFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements OnStartDragListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.no_bills)
    LinearLayout noBillsLayout;
    @BindView(R.id.main_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.total_due_text)
    TextView totalDueTextView;
    @BindView(R.id.bill_peek)
    RelativeLayout billPeek;

    private MainRecyclerViewAdapter adapter;
    private ItemTouchHelper itemTouchHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setClickListeners();
        configureRecyclerView();

    }

    private void configureBillPeek() {
        if (BillTrackerApplication.isBillPeekEnabled()) {
            int daysAhead = BillTrackerApplication.getBillPeekDays();

            RealmResults<Bill> upcomingBills = BillTrackerApplication.getRealm().where(Bill.class).equalTo("deleted", false).between("dueDate", new Date(), new DateTime().plusDays(daysAhead).toDate()).findAllSorted("dueDate");

            int totalDue = 0;

            for (Bill b : upcomingBills) {
                if (b.amountDue > 0) {
                    totalDue += b.amountDue;
                }
            }

            if (totalDue > 0) {
                billPeek.setVisibility(View.VISIBLE);
                totalDueTextView.setText(getString(R.string.bill_peek_upcoming, daysAhead, NumberFormat.getCurrencyInstance().format(totalDue / 100d)));
            } else {
                billPeek.setVisibility(View.GONE);
            }

        } else {
            billPeek.setVisibility(View.GONE);
        }
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
        }).setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                if (event == DISMISS_EVENT_SWIPE || event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_CONSECUTIVE) {
                    //Snackbar has been closed meaning bill has been updated.
                    if (!BillTrackerApplication.getUserToken().equals("")) {
                        BillApi.markBillPaid(billSwipedEvent.bill.uuid, billSwipedEvent.billPaid);
                    }
                }
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
        }/* else if (id == R.id.action_login) {
            startActivity(new Intent(MainActivity.this, GoogleLoginActivity.class));
        }*/

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

    @Override
    public void onResume() {
        super.onResume();
        configureBillPeek();
    }


}
