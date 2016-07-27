package com.djrausch.billtracker.models;

import android.content.Context;

import com.djrausch.billtracker.R;

import java.util.ArrayList;

/**
 * Created by white on 7/25/2016.
 */
public class RepeatingItem {

    public String title;
    public int code;

    public static final int CODE_NEVER = 100;
    public static final int CODE_DAILY = 200;
    public static final int CODE_WEEKLY = 300;
    public static final int CODE_MONTHLY = 400;
    public static final int CODE_BI_YEARLY = 500;
    public static final int CODE_YEARLY = 600;

    public RepeatingItem(String title, int code) {
        this.title = title;
        this.code = code;
    }

    public int toIndex() {
        return convertCodeToIndex(this.code);
    }

    public static int convertCodeToIndex(int code) {
        switch (code) {
            case CODE_NEVER:
                return 0;
            case CODE_DAILY:
                return 1;
            case CODE_WEEKLY:
                return 2;
            case CODE_MONTHLY:
                return 3;
            case CODE_BI_YEARLY:
                return 4;
            case CODE_YEARLY:
                return 5;
            default:
                return 3;
        }
    }

    public static ArrayList<RepeatingItem> getItems(Context context) {
        ArrayList<RepeatingItem> repeatingItems = new ArrayList<>();
        repeatingItems.add(new RepeatingItem(context.getString(R.string.repeating_item_never), RepeatingItem.CODE_NEVER));
        repeatingItems.add(new RepeatingItem(context.getString(R.string.repeating_item_daily), RepeatingItem.CODE_DAILY));
        repeatingItems.add(new RepeatingItem(context.getString(R.string.repeating_item_weekly), RepeatingItem.CODE_WEEKLY));
        repeatingItems.add(new RepeatingItem(context.getString(R.string.repeating_item_monthly), RepeatingItem.CODE_MONTHLY));
        repeatingItems.add(new RepeatingItem(context.getString(R.string.repeating_item_bi_yearly), RepeatingItem.CODE_BI_YEARLY));
        repeatingItems.add(new RepeatingItem(context.getString(R.string.repeating_item_yearly), RepeatingItem.CODE_YEARLY));

        return repeatingItems;
    }

    public static String convertCodeToString(Context context, int code) {
        switch (code) {
            case CODE_NEVER:
                return context.getString(R.string.repeating_item_never);
            case CODE_DAILY:
                return context.getString(R.string.repeating_item_daily);
            case CODE_WEEKLY:
                return context.getString(R.string.repeating_item_weekly);
            case CODE_MONTHLY:
                return context.getString(R.string.repeating_item_monthly);
            case CODE_BI_YEARLY:
                return context.getString(R.string.repeating_item_bi_yearly);
            case CODE_YEARLY:
                return context.getString(R.string.repeating_item_yearly);
            default:
                return context.getString(R.string.repeating_item_monthly);
        }
    }
}
