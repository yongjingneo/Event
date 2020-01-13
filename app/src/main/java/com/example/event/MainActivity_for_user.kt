package com.example.event

import android.app.usage.UsageEvents
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_event.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity_for_user : AppCompatActivity() {
    lateinit var ref: DatabaseReference
    lateinit var eventList: MutableList<eventClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_for_user)

        eventList = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("events")

        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()){
                    eventList.clear()
                    for(u in p0.children){
                        val event = u.getValue(eventClass::class.java)
                        eventList.add(event!!)
                    }

                    val adapter = eventAdapter_for_user(this@MainActivity_for_user, R.layout.event_layout_for_user, eventList)
                    listView.adapter = adapter
                }
            }

        })
    }

}
