package com.djrausch.billtracker.models;

import java.util.Date;

import io.realm.RealmObject;

public class BillNote extends RealmObject {
    public String title;
    public String body;
    public Date date;
}
