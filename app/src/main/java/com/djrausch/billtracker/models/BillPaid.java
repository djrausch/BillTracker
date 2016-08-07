package com.djrausch.billtracker.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by white on 7/25/2016.
 */
public class BillPaid extends RealmObject {
    @PrimaryKey
    public String uuid;
    @SerializedName("paid_date")
    public Date date;

    public BillPaid() {

    }

    public BillPaid(Date date) {
        this.uuid = UUID.randomUUID().toString();
        this.date = date;
    }

    @Override
    public String toString() {
        return "BillPaid{" +
                "uuid='" + uuid + '\'' +
                ", date=" + date +
                '}';
    }
}
