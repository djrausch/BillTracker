package com.djrausch.billtracker.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class BillPaid : RealmObject {
    @PrimaryKey
    open var uuid: String? = null
    open var date: Date? = null

    constructor() {

    }

    constructor(date: Date) {
        this.uuid = UUID.randomUUID().toString()
        this.date = date
    }

    override fun toString(): String {
        return "BillPaid{uuid='$uuid', date='$date'}"
    }

}
