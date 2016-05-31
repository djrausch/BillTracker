package com.djrausch.billtracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.djrausch.billtracker.models.Bill;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddOrEditBillActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.repeating_spinner)
    Spinner repeatingSpinner;
    @BindView(R.id.due_date_select)
    TextView dueDateSelect;

    private int selectedRepeatingIndex = 3;
    private DateTime selectedDueDate = new DateTime();

    private boolean editing = false;
    private Bill editBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        ButterKnife.bind(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.repeating_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatingSpinner.setAdapter(adapter);
        //Default to monthly
        repeatingSpinner.setSelection(3);

        repeatingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRepeatingIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dueDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddOrEditBillActivity.this,
                        selectedDueDate.getYear(),
                        selectedDueDate.getMonthOfYear() - 1,
                        selectedDueDate.getDayOfMonth()
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("edit") && getIntent().getExtras().getBoolean("edit")) {
            loadBillForEditing(getIntent().getExtras().getString("bill_uuid"));
        }
    }

    private void loadBillForEditing(String uuid) {
        editing = true;
        editBill = BillTrackerApplication.getRealm().where(Bill.class).contains("uuid", uuid).findFirst();

        name.setText(editBill.name);
        description.setText(editBill.description);
        selectedDueDate = new DateTime(editBill.dueDate);
        setDueDateText(selectedDueDate);
        repeatingSpinner.setSelection(editBill.repeatingType);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (editing) {
            getMenuInflater().inflate(R.menu.edit_bill_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.add_bill_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            if (editing) {
                BillTrackerApplication.getRealm().beginTransaction();
                editBill.name = name.getText().toString();
                editBill.description = description.getText().toString();
                editBill.repeatingType = selectedRepeatingIndex;
                editBill.dueDate = selectedDueDate.toDate();
                BillTrackerApplication.getRealm().commitTransaction();
            } else {
                Bill b = new Bill(name.getText().toString(), description.getText().toString(), selectedRepeatingIndex, selectedDueDate.toDate());
                BillTrackerApplication.getRealm().beginTransaction();
                BillTrackerApplication.getRealm().copyToRealm(b);
                BillTrackerApplication.getRealm().commitTransaction();
            }
            finish();
            return true;
        } else if (id == R.id.delete_bill) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.confirm_delete));
            builder.setMessage(getString(R.string.deletion_message, editBill.name));
            builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteBill(editBill);
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setNegativeButton(getString(R.string.cancel), null);
            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteBill(Bill bill) {
        BillTrackerApplication.getRealm().beginTransaction();
        bill.deleteFromRealm();
        BillTrackerApplication.getRealm().commitTransaction();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        selectedDueDate = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
        setDueDateText(selectedDueDate);
    }

    private void setDueDateText(DateTime dateText) {
        dueDateSelect.setText(dateText.toString("MMMM d"));
    }
}
