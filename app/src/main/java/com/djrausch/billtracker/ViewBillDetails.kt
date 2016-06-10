package com.djrausch.billtracker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.djrausch.billtracker.adapters.PaidDateRecyclerViewAdapter
import com.djrausch.billtracker.models.Bill
import com.djrausch.billtracker.models.RepeatingItem
import kotlinx.android.synthetic.main.activity_view_bill_details.*
import org.joda.time.DateTime

class ViewBillDetails : AppCompatActivity() {

    lateinit var bill: Bill
    lateinit var billUuid: String

    lateinit var adapter: PaidDateRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_bill_details)

        loadBillAndListen()

    }

    private fun loadBillAndListen() {
        billUuid = intent.getStringExtra("bill_uuid")

        bill = BillTrackerApplication.getRealm().where(Bill::class.java).contains("uuid", billUuid).findFirstAsync()
        bill.addChangeListener<Bill> {
            if (bill.paidDates != null) {
                adapter = PaidDateRecyclerViewAdapter(this, bill.paidDates!!)
                bill_paid_recycler_view.layoutManager = LinearLayoutManager(this)
                bill_paid_recycler_view.adapter = adapter
            }
            setUI()
        }
    }

    private fun setUI() {
        bill_name.text = bill.name
        bill_repeat.text = getString(R.string.repeats_view_bill, RepeatingItem.convertCodeToString(this, bill.repeatingType))
        bill_next_due.text = getString(R.string.next_due_date_view_bill, DateTime(bill.dueDate).toString("MMMM d"))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.view_bill_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            val i: Intent = Intent(this@ViewBillDetails, AddOrEditBillActivity::class.java)
            i.putExtra("edit", true)
            i.putExtra("bill_uuid", billUuid)
            startActivity(Intent(i))
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
