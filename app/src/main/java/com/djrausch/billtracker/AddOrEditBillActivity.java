package com.djrausch.billtracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.djrausch.billtracker.adapters.RepeatingSpinnerAdapter;
import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.models.RepeatingItem;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class AddOrEditBillActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.repeating_spinner)
    public Spinner repeatingSpinner;
    @BindView(R.id.name)
    public EditText name;
    @BindView(R.id.pay_url)
    public EditText payUrl;
    @BindView(R.id.due_date_select)
    public TextView dueDateSelect;


    private RepeatingItem repeatingItem;
    private DateTime selectedDueDate = new DateTime();


    private boolean editing = false;
    private Bill editBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_bill);
        ButterKnife.bind(this);

        dueDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        configureRepeatingSpinner();
        checkIfEditing();

    }

    private void configureRepeatingSpinner() {
        repeatingItem = new RepeatingItem(getString(R.string.repeating_item_monthly), RepeatingItem.CODE_MONTHLY);

        final RepeatingSpinnerAdapter repeatingSpinnerAdapter = new RepeatingSpinnerAdapter(this, android.R.layout.simple_spinner_item);
        repeatingSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        repeatingSpinner.setAdapter(repeatingSpinnerAdapter);
        repeatingSpinner.setSelection(repeatingItem.toIndex());

        repeatingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                repeatingItem = repeatingSpinnerAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void checkIfEditing() {
        if (getIntent().getExtras() != null && getIntent().getBooleanExtra("edit", false)) {
            loadBillForEditing(getIntent().getStringExtra("bill_uuid"));
        }
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, selectedDueDate.getYear(), selectedDueDate.getMonthOfYear() - 1, selectedDueDate.getDayOfMonth());
        datePickerDialog.show(getFragmentManager(), "datePickerDialog");
    }

    private void loadBillForEditing(String billUuid) {
        editing = true;

        Observable<Bill> billObservable = BillTrackerApplication.getRealm().where(Bill.class).contains("uuid", billUuid).findFirst().asObservable();

        billObservable.filter(new Func1<Bill, Boolean>() {
            @Override
            public Boolean call(Bill bill) {
                return bill.isLoaded();
            }
        }).subscribe(new Action1<Bill>() {
            @Override
            public void call(Bill bill) {
                editBill = bill;
                setBillInUI();
            }
        });
    }

    private void setBillInUI() {
        name.setText(editBill.getName());
        selectedDueDate = new DateTime(editBill.getDueDate());
        setDueDateText(selectedDueDate);
        repeatingItem = new RepeatingItem("", editBill.getRepeatingType());
        repeatingSpinner.setSelection(repeatingItem.toIndex());
        payUrl.setText(editBill.getPayUrl());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (editing) {
            getMenuInflater().inflate(R.menu.edit_bill_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.add_bill_menu, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            Realm realm = BillTrackerApplication.getRealm();
            if (editing) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        editBill.setName(name.getText().toString());
                        editBill.setRepeatingType(repeatingItem.code);
                        editBill.setDueDate(selectedDueDate.toDate());
                        editBill.setPayUrl(payUrl.getText().toString());
                    }
                });
            } else {
                final Bill b = new Bill(name.getText().toString(), "", repeatingItem.code, selectedDueDate.toDate(), payUrl.getText().toString());
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(b);
                    }
                });
            }

            Intent intent = new Intent();
            intent.putExtra("delete", false);
            setResult(RESULT_OK, intent);
            finish();
            return true;

        } else if (id == R.id.delete_bill) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.confirm_delete));
            builder.setMessage(getString(R.string.deletion_message, editBill.getName()));
            builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteBill(editBill);
                    dialog.dismiss();

                    Intent intent = new Intent();
                    intent.putExtra("delete", true);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            builder.setNegativeButton(getString(R.string.cancel), null);

            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteBill(final Bill bill) {
        BillTrackerApplication.getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                bill.deleteFromRealm();
            }
        });
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        selectedDueDate = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
        setDueDateText(selectedDueDate);
    }

    private void setDueDateText(DateTime selectedDueDate) {
        dueDateSelect.setText(selectedDueDate.toString("MMMM d"));
    }
}
