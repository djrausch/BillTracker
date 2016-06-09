package com.djrausch.billtracker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.djrausch.billtracker.models.Bill

class ViewBillDetails : AppCompatActivity() {

    lateinit var bill: Bill
    lateinit var billUuid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_bill_details)

        loadBillAndListen()

    }

    private fun loadBillAndListen() {
        billUuid = intent.getStringExtra("bill_uuid")

        bill = BillTrackerApplication.getRealm().where(Bill::class.java).contains("uuid", billUuid).findFirstAsync()
        bill.addChangeListener<Bill> {
            title = bill.name
        }
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
