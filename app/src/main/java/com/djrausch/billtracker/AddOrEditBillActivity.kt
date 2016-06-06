package com.djrausch.billtracker

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import com.djrausch.billtracker.adapters.RepeatingSpinnerAdapter
import com.djrausch.billtracker.models.Bill
import com.djrausch.billtracker.models.RepeatingItem
import com.djrausch.billtracker.ui.AddOrEditUI
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_add_bill.*
import org.jetbrains.anko.setContentView
import org.joda.time.DateTime

class AddOrEditBillActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private var repeatingItem: RepeatingItem? = null
    private var selectedDueDate = DateTime()

    private var editing = false
    private var editBill: Bill? = null
    var repeatingSpinner: Spinner? = null

    var payUrl: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AddOrEditUI().setContentView(this)


        val adapter = RepeatingSpinnerAdapter(this, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        repeatingSpinner?.adapter = adapter

        repeatingItem = RepeatingItem(getString(R.string.repeating_item_monthly), RepeatingItem.CODE_MONTHLY)
        repeatingSpinner?.setSelection(repeatingItem?.toIndex()!!)

        repeatingSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                repeatingItem = adapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        if (intent.extras != null && intent.getBooleanExtra("edit", false)) {
            loadBillForEditing(intent.getStringExtra("bill_uuid"))
        }


        /*setContentView(R.layout.activity_add_bill)


        val adapter = RepeatingSpinnerAdapter(this, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        repeating_spinner.setAdapter(adapter)
        //Default to monthly
        repeating_spinner.setSelection(repeatingItem!!.toIndex())

        repeating_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                repeatingItem = adapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        due_date_select.setOnClickListener({
            val dpd = DatePickerDialog.newInstance(
                    this@AddOrEditBillActivity,
                    selectedDueDate.year,
                    selectedDueDate.monthOfYear - 1,
                    selectedDueDate.dayOfMonth)
            dpd.show(fragmentManager, "Datepickerdialog")
        })

        if (intent.extras != null && intent.getBooleanExtra("edit", false)) {
            loadBillForEditing(intent.getStringExtra("bill_uuid"))
        }*/

    }

    fun showDatePicker() {
        val dpd = DatePickerDialog.newInstance(
                this@AddOrEditBillActivity,
                selectedDueDate.year,
                selectedDueDate.monthOfYear - 1,
                selectedDueDate.dayOfMonth)
        dpd.show(fragmentManager, "Datepickerdialog")
    }


    private fun loadBillForEditing(uuid: String) {
        editing = true
        editBill = BillTrackerApplication.getRealm().where(Bill::class.java).contains("uuid", uuid).findFirst()

        name.setText(editBill!!.name)
        description.setText(editBill!!.description)
        selectedDueDate = DateTime(editBill!!.dueDate)
        setDueDateText(selectedDueDate)
        repeatingItem = RepeatingItem("", editBill!!.repeatingType)
        repeating_spinner.setSelection(repeatingItem!!.toIndex())
        pay_url.setText(editBill!!.payUrl)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (editing) {
            menuInflater.inflate(R.menu.edit_bill_menu, menu)
        } else {
            menuInflater.inflate(R.menu.add_bill_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            if (editing) {
                BillTrackerApplication.getRealm().beginTransaction()
                editBill!!.name = name.getText().toString()
                editBill!!.description = description.getText().toString()
                editBill!!.repeatingType = repeatingItem!!.code
                editBill!!.dueDate = selectedDueDate.toDate()
                editBill!!.payUrl = pay_url.getText().toString()
                BillTrackerApplication.getRealm().commitTransaction()
            } else {
                val b = Bill(name.getText().toString(), description.getText().toString(), repeatingItem!!.code, selectedDueDate.toDate(), pay_url.getText().toString())
                BillTrackerApplication.getRealm().beginTransaction()
                BillTrackerApplication.getRealm().copyToRealm(b)
                BillTrackerApplication.getRealm().commitTransaction()
            }
            finish()
            return true
        } else if (id == R.id.delete_bill) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.confirm_delete))
            builder.setMessage(getString(R.string.deletion_message, editBill!!.name))
            builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                deleteBill(editBill!!)
                dialog.dismiss()
                finish()
            }
            builder.setNegativeButton(getString(R.string.cancel), null)
            builder.show()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun deleteBill(bill: Bill) {
        BillTrackerApplication.getRealm().beginTransaction()
        bill.deleteFromRealm()
        BillTrackerApplication.getRealm().commitTransaction()
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        selectedDueDate = DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0)
        setDueDateText(selectedDueDate)
    }

    private fun setDueDateText(dateText: DateTime) {
        due_date_select.setText(dateText.toString("MMMM d"))
    }
}

