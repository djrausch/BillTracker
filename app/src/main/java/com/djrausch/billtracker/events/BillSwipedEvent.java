package com.djrausch.billtracker.events;

import com.djrausch.billtracker.models.Bill;

import java.util.Date;

public class BillSwipedEvent {
    public Date oldDate;
    public Bill bill;

    public BillSwipedEvent(Date oldDate, Bill bill) {
        this.oldDate = oldDate;
        this.bill = bill;
    }
}
