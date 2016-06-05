package com.djrausch.billtracker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import com.djrausch.billtracker.models.RepeatingItem


class RepeatingSpinnerAdapter(context: Context, resource: Int) : ArrayAdapter<RepeatingItem>(context, resource, RepeatingItem.getItems(context)) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val text: TextView

        if (convertView == null) {
            val inflater = LayoutInflater.from(parent.context)
            view = inflater.inflate(android.R.layout.simple_spinner_item, parent, false)
        } else {
            view = convertView
        }

        text = view as TextView

        val item = getItem(position)
        text.text = item.title

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): TextView {
        val view: View
        val text: TextView

        if (convertView == null) {
            val inflater = LayoutInflater.from(parent.context)
            view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
        } else {
            view = convertView
        }

        text = view as TextView

        val item = getItem(position)
        text.text = item.title

        return text


        /*TextView v = (TextView) super.getView(position, convertView, parent);

        v.setText(getItem(position).title);
        return v;*/
    }

}
