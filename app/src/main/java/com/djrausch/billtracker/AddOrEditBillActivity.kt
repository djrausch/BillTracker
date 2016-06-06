package com.djrausch.billtracker

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.djrausch.billtracker.models.Bill
import com.djrausch.billtracker.models.RepeatingItem
import com.djrausch.billtracker.ui.AddOrEditUI
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import io.realm.Realm
import org.jetbrains.anko.setContentView
import org.joda.time.DateTime

class AddOrEditBillActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    lateinit var repeatingItem: RepeatingItem
    private var selectedDueDate = DateTime()

    private var editing = false
    private var editBill: Bill? = null

    //UI
    var name: EditText? = null
    var dueDateSelect: TextView? = null
    var repeatingSpinner: Spinner? = null
    var payUrl: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Set UI
        AddOrEditUI().setContentView(this)
        //Configure Spinner
        configureRepeatingSpinner()
        //Check if editing a previous bill
        checkIfEditing()
    }

    /**
     * Used to check intent for editing prop. If it is in edit mode, load the bill and set the ui.
     */
    private fun checkIfEditing() {
        if (intent.extras != null && intent.getBooleanExtra("edit", false)) {
            loadBillForEditing(intent.getStringExtra("bill_uuid"))
        }
    }

    /**
     * Set the repeating spinner to monthly by defualt
     */
    private fun configureRepeatingSpinner() {
        repeatingItem = RepeatingItem(getString(R.string.repeating_item_monthly), RepeatingItem.CODE_MONTHLY)
        repeatingSpinner?.setSelection(repeatingItem.toIndex())
    }

    /**
     * Show the date picker
     */
    fun showDatePicker() {
        val dpd = DatePickerDialog.newInstance(
                this@AddOrEditBillActivity,
                selectedDueDate.year,
                selectedDueDate.monthOfYear - 1,
                selectedDueDate.dayOfMonth)
        dpd.show(fragmentManager, "Datepickerdialog")
    }


    /**
     * Load the bill for editing
     * @param uuid The uuid of the bill to load
     */
    private fun loadBillForEditing(uuid: String) {
        editing = true
        editBill = BillTrackerApplication.getRealm().where(Bill::class.java).contains("uuid", uuid).findFirst()

        name?.setText(editBill!!.name)
        //description.setText(editBill!!.description)
        selectedDueDate = DateTime(editBill!!.dueDate)
        setDueDateText(selectedDueDate)
        repeatingItem = RepeatingItem("", editBill!!.repeatingType)
        repeatingSpinner?.setSelection(repeatingItem.toIndex())
        payUrl?.setText(editBill!!.payUrl)
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
            val realm: Realm = BillTrackerApplication.getRealm()
            if (editing) {
                realm.executeTransaction {
                    editBill?.name = name?.text.toString()
                    //editBill!!.description = description.getText().toString()
                    editBill?.repeatingType = repeatingItem.code
                    editBill?.dueDate = selectedDueDate.toDate()
                    editBill?.payUrl = payUrl?.text.toString()
                }

            } else {
                val b = Bill(name?.text.toString(), "", repeatingItem.code, selectedDueDate.toDate(), payUrl?.text.toString())
                realm.executeTransaction { realm.copyToRealm(b) }
            }
            finish()
            return true
        } else if (id == R.id.delete_bill) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.confirm_delete))
            builder.setMessage(getString(R.string.deletion_message, editBill!!.name))
            builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
                deleteBill(editBill)
                dialog.dismiss()
                finish()
            }
            builder.setNegativeButton(getString(R.string.cancel), null)
            builder.show()
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * Delete bill
     * @param bill The bill to delete
     */
    private fun deleteBill(bill: Bill?) {
        val realm: Realm = BillTrackerApplication.getRealm()
        realm.executeTransaction { bill?.deleteFromRealm() }
    }

    /**
     * Callback for date picker
     */
    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        selectedDueDate = DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0)
        setDueDateText(selectedDueDate)
    }

    /**
     * Set the due date in the textview
     * @param dateText The DateTime representing the user selected date.
     */
    private fun setDueDateText(dateText: DateTime) {
        dueDateSelect?.text = dateText.toString("MMMM d")
    }
}

