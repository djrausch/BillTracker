package com.djrausch.billtracker.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by white on 7/25/2016.
 */
public class Bill extends RealmObject {
    @PrimaryKey
    public String uuid;
    public String name;
    public String description;
    @SerializedName("repeating_type")
    public int repeatingType = 0;
    @SerializedName("due_date")
    public Date dueDate;
    @SerializedName("pay_url")
    public String payUrl;
    public RealmList<BillNote> notes;
    @SerializedName("paid_dates")
    public RealmList<BillPaid> paidDates;
    public boolean deleted = false;

    public Bill() {

    }

    public Bill(String name, String description, int repeatingType, Date dueDate, String payUrl) {
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.repeatingType = repeatingType;
        this.dueDate = dueDate;
        this.payUrl = payUrl;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", repeatingType=" + repeatingType +
                ", dueDate=" + dueDate +
                ", payUrl='" + payUrl + '\'' +
                ", notes=" + notes +
                ", paidDates=" + paidDates +
                ", deleted=" + deleted +
                '}';
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRepeatingType() {
        return repeatingType;
    }

    public void setRepeatingType(int repeatingType) {
        this.repeatingType = repeatingType;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public RealmList<BillNote> getBillNotes() {
        return notes;
    }

    public void setBillNotes(RealmList<BillNote> billNotes) {
        this.notes = billNotes;
    }

    public RealmList<BillPaid> getPaidDates() {
        return paidDates;
    }

    public void setPaidDates(RealmList<BillPaid> paidDates) {
        this.paidDates = paidDates;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
