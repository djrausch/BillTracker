package com.djrausch.billtracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.djrausch.billtracker.models.RepeatingItem;

/**
 * Created by white on 7/25/2016.
 */
public class RepeatingSpinnerAdapter extends ArrayAdapter<RepeatingItem> {
    public RepeatingSpinnerAdapter(Context context, int resource) {
        super(context, resource);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        TextView text;

        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        } else {
            view = convertView;
        }

        text = (TextView) view;

        RepeatingItem item = getItem(position);
        text.setText(item.title);

        return view;
    }

    @Override
    public TextView getDropDownView(int position, View convertView, ViewGroup parent) {
        View view;
        TextView text;

        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        } else {
            view = convertView;
        }

        text = (TextView) view;

        RepeatingItem item = getItem(position);
        text.setText(item.title);

        return text;
    }
}
