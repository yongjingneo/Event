package com.example.event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_add_event.*
import android.app.Activity
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

        btnSave.setOnClickListener(){
            saveEvent()
        }

        btnGoBack.setOnClickListener(){
            finish()
        }
    }

    private fun saveEvent(){
        val title = editTxtTitle.text.toString().trim()
        val date = editTxtDate.text.toString().trim()
        val description = editTxtDescription.text.toString().trim()

        val ref = FirebaseDatabase.getInstance().getReference("events")

        val eventId = ref.push().key.toString()

        val event = eventClass(eventId, title, date, description)

        ref.child(eventId).setValue(event).addOnCompleteListener{
            Toast.makeText(applicationContext,"Completed",Toast.LENGTH_LONG).show()
        }
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
