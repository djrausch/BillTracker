package com.djrausch.billtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.djrausch.billtracker.models.Bill;

import java.util.Date;

public class ViewBillActivity extends AppCompatActivity {

    Bill bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        }
        if (getIntent().getExtras().containsKey("bill_uuid")) {
            String uuid = getIntent().getExtras().getString("bill_uuid");
            bill = BillTrackerApplication.getRealm().where(Bill.class).contains("uuid", uuid).findFirst();
            Log.d("bill", bill.toString());
        } else {
            Toast.makeText(this, getString(R.string.view_bill_error_loading), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_bill_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
