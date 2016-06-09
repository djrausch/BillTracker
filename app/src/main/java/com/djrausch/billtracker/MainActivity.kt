package com.djrausch.billtracker

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.djrausch.billtracker.adapters.MainRecyclerViewAdapter
import com.djrausch.billtracker.events.BillSwipedEvent
import com.djrausch.billtracker.itemtouchhelpers.ItemClickSupport
import com.djrausch.billtracker.itemtouchhelpers.OnStartDragListener
import com.djrausch.billtracker.itemtouchhelpers.SimpleItemTouchHelperCallback
import com.djrausch.billtracker.util.BillUtil
import com.djrausch.billtracker.util.BillNotificationManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity(), OnStartDragListener {

    lateinit private var adapter: MainRecyclerViewAdapter
    lateinit private var mItemTouchHelper: ItemTouchHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        setSupportActionBar(toolbar)

        setClickListeners()
        configureRecyclerView()
    }

    private fun setClickListeners() {
        fab.setOnClickListener { startActivity(Intent(this@MainActivity, AddOrEditBillActivity::class.java)) }

        no_bills.setOnClickListener({ startActivity(Intent(this@MainActivity, AddOrEditBillActivity::class.java)) })
    }

    private fun configureRecyclerView() {

        val bills = BillUtil.loadBills()
        bills.addChangeListener {
            if (adapter.itemCount == 0) {
                no_bills.visibility = View.VISIBLE
                main_recyclerview.visibility = View.GONE
            } else {
                no_bills.visibility = View.GONE
                main_recyclerview.visibility = View.VISIBLE
            }
        }

        //Set layout manager of recycler view
        main_recyclerview.layoutManager = LinearLayoutManager(this)

        //Create and set adapter
        adapter = MainRecyclerViewAdapter(this, bills)
        main_recyclerview.adapter = adapter

        //Add swipe support
        val callback = SimpleItemTouchHelperCallback(adapter)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper.attachToRecyclerView(main_recyclerview)

        //Item click listner
        ItemClickSupport.addTo(main_recyclerview).setOnItemClickListener { recyclerView, position, v ->
            val i = Intent(this@MainActivity, ViewBillDetails::class.java)
            i.putExtra("edit", true)
            i.putExtra("bill_uuid", adapter.getItem(position).uuid)
            startActivity(i)

            //BillNotificationManager(this@MainActivity).makeBillNotification(adapter.getItem(position))

        }
    }

    @Subscribe
    fun onBillSwiped(billSwipedEvent: BillSwipedEvent) {
        Snackbar.make(coordinator, getString(R.string.snackbar_bill_paid, billSwipedEvent.bill.name), Snackbar.LENGTH_LONG).setAction(R.string.undo) {
            /*BillTrackerApplication.getRealm().beginTransaction();
                        billSwipedEvent.bill.dueDate = billSwipedEvent.oldDate;
                        BillTrackerApplication.getRealm().commitTransaction();*/
            BillUtil.undoMarkBillPaid(billSwipedEvent.oldDate, billSwipedEvent.bill)
        }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        mItemTouchHelper.startDrag(viewHolder)
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
}
