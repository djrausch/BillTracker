package com.djrausch.billtracker.models;

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
                    .addField("payUrl", String.class, null);
            oldVersion++;
        }
    }
}
