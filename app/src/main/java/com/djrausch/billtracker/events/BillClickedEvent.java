package com.djrausch.billtracker.events;

import com.djrausch.billtracker.models.Bill;

public class BillClickedEvent {
    public Bill bill;

    public BillClickedEvent(Bill bill) {
        this.bill = bill;
    }
}
