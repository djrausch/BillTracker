package com.djrausch.billtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.djrausch.billtracker.enums.Repeating;
import com.djrausch.billtracker.models.Bill;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddBillActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

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
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddBillActivity.this,
                        selectedDueDate.getYear(),
                        selectedDueDate.getMonthOfYear() - 1,
                        selectedDueDate.getDayOfMonth()
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_bill_menu, menu);
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
            Bill b = new Bill(name.getText().toString(), description.getText().toString(), true, selectedRepeatingIndex, selectedDueDate.toDate());
            BillTrackerApplication.getRealm().beginTransaction();
            BillTrackerApplication.getRealm().copyToRealm(b);
            BillTrackerApplication.getRealm().commitTransaction();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        selectedDueDate = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
        dueDateSelect.setText(selectedDueDate.toString("MMMM d"));
    }
}
