package com.djrausch.billtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.djrausch.billtracker.adapters.MainRecyclerViewAdapter;
import com.djrausch.billtracker.events.BillSwipedEvent;
import com.djrausch.billtracker.itemtouchhelpers.OnStartDragListener;
import com.djrausch.billtracker.itemtouchhelpers.SimpleItemTouchHelperCallback;
import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.presenter.MainPresenter;
import com.djrausch.billtracker.view.MainView;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView, OnStartDragListener {

    @BindView(R.id.main_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    MainRecyclerViewAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, AddBillActivity.class));
                }
            });
        }

        RealmResults<Bill> bills = presenter.loadBills();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MainRecyclerViewAdapter(this, bills);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Subscribe
    public void onBillSwiped(final BillSwipedEvent billSwipedEvent) {
        Snackbar.make(coordinatorLayout, R.string.snackbar_bill_paid, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BillTrackerApplication.getRealm().beginTransaction();
                        billSwipedEvent.bill.dueDate = billSwipedEvent.oldDate;
                        BillTrackerApplication.getRealm().commitTransaction();
                    }
                }).show();
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
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
    public void showLoading(boolean loading) {

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
