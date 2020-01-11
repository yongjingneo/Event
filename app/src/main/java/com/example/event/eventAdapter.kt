package com.example.event

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

class eventAdapter(val mCtx: Context, val layoutResId: Int, val eventList: List<eventClass>)
    : ArrayAdapter<eventClass>(mCtx, layoutResId, eventList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view : View = layoutInflater.inflate(layoutResId, null)

        val txtTitle = view.findViewById<TextView>(R.id.txtTitle)
        val txtDate = view.findViewById<TextView>(R.id.txtDate)
        val txtDescription = view.findViewById<TextView>(R.id.txtDescription)
        val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)

        val event = eventList[position]

        txtTitle.text = event.title
        txtDate.text = event.date
        txtDescription.text = event.description

        btnUpdate.setOnClickListener(){
            updateEvent(event)
        }

        return view
    }

    private fun updateEvent(event: eventClass) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Update Event")

        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.update_layout, null)

        val editTitle = view.findViewById<EditText>(R.id.editTitle)
        val editDate = view.findViewById<EditText>(R.id.editDate)
        val editDescription = view.findViewById<EditText>(R.id.editDescription)

        editTitle.setText(event.title)
        editDate.setText(event.date)
        editDescription.setText(event.description)

        builder.setView(view)
        builder.setPositiveButton("Update",object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val dbEvent = FirebaseDatabase.getInstance().getReference("events")

                val title = editTitle.text.toString().trim()
                val date = editDate.text.toString().trim()
                val description = editDescription.text.toString().trim()

                //incomplete
                if(title.isEmpty()){
                    editTitle.error = "Please enter a title."
                    editTitle.requestFocus()
                    return
                }

                val updatedEvent =  eventClass(event.id, title, date, description)

                dbEvent.child(event.id).setValue(updatedEvent)

                Toast.makeText(mCtx, "Event updated successfully.", Toast.LENGTH_LONG).show()

            }

        })

        builder.setNegativeButton("Cancel",object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                return
            }

        })

        val alert = builder.create()
        alert.show()

    }
}