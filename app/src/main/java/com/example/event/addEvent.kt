package com.example.event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_event.*
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.inputmethod.InputMethodManager
import android.view.ViewGroup
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.EditText
import android.view.View
import com.google.firebase.database.FirebaseDatabase


class addEvent : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        setupUI(addEventActivity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        btnSave.setOnClickListener(){
            saveEvent()
        }

        btnGoBack.setOnClickListener(){
            goBack()
        }
    }

    private fun saveEvent(){
        val title = newTitle.text.toString().trim()
        val date = newDate.text.toString().trim()
        val location = newLocation.text.toString().trim()
        val description = newDescription.text.toString().trim()

        if(title.isEmpty() || date.isEmpty() || location.isEmpty() || description.isEmpty()){
            Toast.makeText(this,"Please fill in all fields.",Toast.LENGTH_LONG).show()
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("events")
        val eventId = ref.push().key.toString()
        val event = eventClass(eventId, title, date, location, description)

        newTitle.setText("")
        newDate.setText("")
        newLocation.setText("")
        newDescription.setText("")

        ref.child(eventId).setValue(event).addOnCompleteListener {
            Toast.makeText(applicationContext, "Event added.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun goBack(){
        val title = newTitle.text.toString().trim()
        val date = newDate.text.toString().trim()
        val location = newLocation.text.toString().trim()
        val description = newDescription.text.toString().trim()

        if(title.length>0 || date.length>0 || location.length>0 || description.length>0){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Event haven't save")
            builder.setMessage("Go back without saving event?")
            builder.setPositiveButton("Yes",object :DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    finish()
                }

            })
            builder.setNegativeButton("No",object :DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    return
                }

            })
            builder.show()
        }
        else
            finish()
    }

    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager!!.hideSoftInputFromWindow(
            activity.currentFocus!!.windowToken, 0
        )
    }

    fun setupUI(view: View) {

        if (view !is EditText) {
            view.setOnTouchListener(object : OnTouchListener {
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    hideSoftKeyboard(this@addEvent)
                    return false
                }
            })
        }

        if (view is ViewGroup) {
            for (i in 0 until (view as ViewGroup).childCount) {
                val innerView = (view as ViewGroup).getChildAt(i)
                setupUI(innerView)
            }
        }
    }
}
