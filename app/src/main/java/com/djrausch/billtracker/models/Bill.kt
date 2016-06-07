package com.djrausch.billtracker.models

import java.util.Date
import java.util.UUID

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Bill : RealmObject {
    @PrimaryKey
    open var uuid: String? = null
    open var name: String? = null
    open var description: String? = null
    open var repeatingType: Int = 0
    open var dueDate: Date? = null
    open var payUrl: String? = null
    open var notes: RealmList<BillNote>? = null
    open var paidDates: RealmList<BillPaid>? = null

    constructor() {

    }

    constructor(name: String, description: String, repeatingType: Int, dueDate: Date, payUrl: String) {
        this.uuid = UUID.randomUUID().toString()
        this.name = name
        this.description = description
        this.repeatingType = repeatingType
        this.dueDate = dueDate
        this.payUrl = payUrl
    }

    override fun toString(): String {
        return "Bill{uuid='$uuid', name='$name', description='$description', repeatingType=$repeatingType, dueDate=$dueDate, payUrl='$payUrl', notes=$notes, paidDate=$paidDates}"
    }
}
