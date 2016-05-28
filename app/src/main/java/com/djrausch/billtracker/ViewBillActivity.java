package com.djrausch.billtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ViewBillActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);
        if (getIntent().getExtras().containsKey("bill_uuid")) {
            Toast.makeText(this, "Bill: " + getIntent().getExtras().getString("bill_uuid"), Toast.LENGTH_LONG).show();
        }
    }
}
