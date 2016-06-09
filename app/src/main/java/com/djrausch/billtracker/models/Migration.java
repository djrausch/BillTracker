package com.djrausch.billtracker.models;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class Migration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            //Add pay url to bill
            schema.get("Bill")
                    .addField("payUrl", String.class);
            oldVersion++;
        }

        if (oldVersion == 1) {
            schema.create("BillPaid")
                    .addField("uuid", String.class)
                    .addPrimaryKey("uuid")
                    .addField("date", Date.class);
            schema.get("Bill")
                    .addRealmListField("paidDates", schema.get("BillPaid"));
            oldVersion++;
        }
    }
}
