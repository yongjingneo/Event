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
        val txtLocation = view.findViewById<TextView>(R.id.txtLocation)
        val txtDescription = view.findViewById<TextView>(R.id.txtDescription)
        val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)
        val btnDelete = view.findViewById<Button>(R.id.btnDelete)

        val event = eventList[position]

        txtTitle.text = event.title
        txtDate.text = event.date
        txtLocation.text = event.location
        txtDescription.text = event.description

        btnUpdate.setOnClickListener(){
            updateEvent(event)
        }

        btnDelete.setOnClickListener(){
            deleteEvent(event)
        }

        return view
    }

    private fun updateEvent(event: eventClass) {
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Update Event")

        val inflater = LayoutInflater.from(mCtx)

        val view = inflater.inflate(R.layout.update_layout, null)

        val updateTitle = view.findViewById<EditText>(R.id.updateTitle)
        val updateDate = view.findViewById<EditText>(R.id.updateDate)
        val updateLocation = view.findViewById<EditText>(R.id.updateLocation)
        val updateDescription = view.findViewById<EditText>(R.id.updateDescription)

        updateTitle.setText(event.title)
        updateDate.setText(event.date)
        updateLocation.setText(event.location)
        updateDescription.setText(event.description)

        builder.setView(view)
        builder.setPositiveButton("Update",object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val dbEvent = FirebaseDatabase.getInstance().getReference("events")

                val title = updateTitle.text.toString().trim()
                val date = updateDate.text.toString().trim()
                val location = updateLocation.text.toString().trim()
                val description = updateDescription.text.toString().trim()

                if(title.isEmpty()){
                    Toast.makeText(mCtx,"Title cannot be empty.",Toast.LENGTH_SHORT).show()
                    return
                }
                if(date.isEmpty()){
                    Toast.makeText(mCtx,"Date cannot be empty.",Toast.LENGTH_SHORT).show()
                    return
                }
                if(location.isEmpty()){
                    Toast.makeText(mCtx,"Location cannot be empty",Toast.LENGTH_SHORT).show()
                    return
                }
                if(description.isEmpty()){
                    Toast.makeText(mCtx,"Description cannot be empty.",Toast.LENGTH_SHORT).show()
                    return
                }

                val updatedEvent =  eventClass(event.id, title, date, location, description)
                dbEvent.child(event.id).setValue(updatedEvent)

                Toast.makeText(mCtx, "Event updated.", Toast.LENGTH_SHORT).show()

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

    private fun deleteEvent(event: eventClass){
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Delete Event")
        builder.setMessage("Are you sure to delete this event?")
        builder.setPositiveButton("Yes", object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

                val dbEvent = FirebaseDatabase.getInstance().getReference("events").child(event.id)

                dbEvent.removeValue()

                Toast.makeText(mCtx,"Event deleted.",Toast.LENGTH_SHORT).show()
            }
        })

        builder.setNegativeButton("No",object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                return
            }

        })

        builder.show()
    }
}