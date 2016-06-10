package com.djrausch.billtracker.models

import android.content.Context
import android.support.annotation.StringRes

import com.djrausch.billtracker.R

import java.util.ArrayList

class RepeatingItem(var title: String, var code: Int) {

    fun toIndex(): Int {
        return convertCodeToIndex(this.code)
    }

    companion object {

        val CODE_NEVER = 100
        val CODE_DAILY = 200
        val CODE_WEEKLY = 300
        val CODE_MONTHLY = 400
        val CODE_BI_YEARLY = 500
        val CODE_YEARLY = 600

        fun convertCodeToIndex(code: Int): Int {
            when (code) {
                CODE_NEVER -> return 0
                CODE_DAILY -> return 1
                CODE_WEEKLY -> return 2
                CODE_MONTHLY -> return 3
                CODE_BI_YEARLY -> return 4
                CODE_YEARLY -> return 5
                else -> return 3
            }
        }

        fun getItems(context: Context): ArrayList<RepeatingItem> {
            val repeatingItems = ArrayList<RepeatingItem>()
            repeatingItems.add(RepeatingItem(context.getString(R.string.repeating_item_never), RepeatingItem.CODE_NEVER))
            repeatingItems.add(RepeatingItem(context.getString(R.string.repeating_item_daily), RepeatingItem.CODE_DAILY))
            repeatingItems.add(RepeatingItem(context.getString(R.string.repeating_item_weekly), RepeatingItem.CODE_WEEKLY))
            repeatingItems.add(RepeatingItem(context.getString(R.string.repeating_item_monthly), RepeatingItem.CODE_MONTHLY))
            repeatingItems.add(RepeatingItem(context.getString(R.string.repeating_item_bi_yearly), RepeatingItem.CODE_BI_YEARLY))
            repeatingItems.add(RepeatingItem(context.getString(R.string.repeating_item_yearly), RepeatingItem.CODE_YEARLY))

            return repeatingItems
        }

        fun convertCodeToString(context: Context, code: Int): String {
            when (code) {
                CODE_NEVER -> return context.getString(R.string.repeating_item_never)
                CODE_DAILY -> return context.getString(R.string.repeating_item_daily)
                CODE_WEEKLY -> return context.getString(R.string.repeating_item_weekly)
                CODE_MONTHLY -> return context.getString(R.string.repeating_item_monthly)
                CODE_BI_YEARLY -> return context.getString(R.string.repeating_item_bi_yearly)
                CODE_YEARLY -> return context.getString(R.string.repeating_item_yearly)
                else -> return context.getString(R.string.repeating_item_monthly)
            }
        }
    }
}
