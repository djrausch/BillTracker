package com.djrausch.billtracker.ui

import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.djrausch.billtracker.AddOrEditBillActivity
import com.djrausch.billtracker.R
import org.jetbrains.anko.*
import org.jetbrains.anko.design.textInputLayout

class AddOrEditUI : AnkoComponent<AddOrEditBillActivity> {
    var TextView.textColorRes: Int
        get() = throw PropertyWithoutGetterException("textColorRes")
        set(@ColorRes v) = setTextColor(ContextCompat.getColor(context, v))

    override fun createView(ui: AnkoContext<AddOrEditBillActivity>) = with(ui) {
        scrollView {
            verticalLayout {
                padding = dip(16)
                textInputLayout {
                    editText {
                        hintResource = R.string.name
                    }
                }

                textView {
                    textResource = R.string.due_date
                    textColorRes = R.color.colorAccent
                    textSize = 12f
                }.lparams(width = matchParent) {
                    leftMargin = dip(2)
                    topMargin = dip(8)
                }

                textView {
                    textResource = R.string.tap_to_select_due_date
                    textSize = 18f
                    isClickable = true
                    onClick {
                        ui.owner.showDatePicker()
                    }
                }.lparams(width = matchParent) {
                    leftMargin = dip(2)
                    topMargin = dip(4)
                }

                textView {
                    textResource = R.string.repeating
                    textColorRes = R.color.colorAccent
                    textSize = 12f
                }.lparams(width = matchParent) {
                    leftMargin = dip(2)
                    topMargin = dip(8)
                }

                ui.owner.repeatingSpinner = spinner {

                }.lparams(width = matchParent) {
                    topMargin = dip(2)
                }

                textInputLayout {
                    ui.owner.payUrl = editText {
                        hintResource = R.string.url_to_pay_bill
                        maxLines = 1
                        singleLine = true
                    }
                }.lparams(width = matchParent) {
                    topMargin = dip(4)
                }
            }
        }
    }

}
