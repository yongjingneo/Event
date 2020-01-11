package com.example.event

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class eventAdapter(val mCtx: Context, val layoutResId: Int, val eventList: List<eventClass>)
    : ArrayAdapter<eventClass>(mCtx, layoutResId, eventList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view : View = layoutInflater.inflate(layoutResId, null)

        val txtTitle = view.findViewById<TextView>(R.id.txtTitle)
        val txtDate = view.findViewById<TextView>(R.id.txtDate)
        val txtDescription = view.findViewById<TextView>(R.id.txtDescription)

        val event = eventList[position]

        txtTitle.text = event.title
        txtDate.text = event.date
        txtDescription.text = event.description

        return view
    }
}