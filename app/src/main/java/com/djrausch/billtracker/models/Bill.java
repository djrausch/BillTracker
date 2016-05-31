package com.djrausch.billtracker.models;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Bill extends RealmObject {
    @PrimaryKey
    public String uuid;
    public String name;
    public String description;
    public int repeatingType;
    public Date dueDate;
    public RealmList<BillNote> notes;

    public Bill() {

    }

    public Bill(String name, String description, int repeatingType, Date dueDate) {
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.repeatingType = repeatingType;
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", repeatingType=" + repeatingType +
                ", dueDate=" + dueDate +
                ", notes=" + notes +
                '}';
    }
}
