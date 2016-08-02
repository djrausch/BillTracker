package com.djrausch.billtracker.events;

import com.djrausch.billtracker.models.Bill;
import com.djrausch.billtracker.models.BillPaid;

import java.util.Date;

public class BillSwipedEvent {
    public Date oldDate;
    public Bill bill;
    public BillPaid billPaid;

    public BillSwipedEvent(Date oldDate, Bill bill, BillPaid billPaid) {
        this.oldDate = oldDate;
        this.bill = bill;
        this.billPaid = billPaid;
    }
}
