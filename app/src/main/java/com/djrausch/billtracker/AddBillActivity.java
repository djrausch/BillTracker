package com.djrausch.billtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.djrausch.billtracker.models.Bill;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddBillActivity extends AppCompatActivity {

    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.description)
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        ButterKnife.bind(this);
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
            Bill b = new Bill(name.getText().toString(), description.getText().toString(), true, 1, new Date());
            BillTrackerApplication.getRealm().beginTransaction();
            BillTrackerApplication.getRealm().copyToRealm(b);
            BillTrackerApplication.getRealm().commitTransaction();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
