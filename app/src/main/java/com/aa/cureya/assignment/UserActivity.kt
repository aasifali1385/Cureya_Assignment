package com.aa.cureya.assignment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)


        getSupportActionBar()!!.setDisplayShowHomeEnabled(true);
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);


        val nametxt = findViewById<TextView>(R.id.nametxt)
        val emailtxt = findViewById<TextView>(R.id.emailtxt)
        val mobiletxt = findViewById<TextView>(R.id.mobiletxt)

        val cDataRef = FirebaseDatabase.getInstance().getReference("AllUser").child(
            intent.getStringExtra(
                "key"
            )!!
        )
        cDataRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                nametxt.text = "Name : " + dataSnapshot.child("name").getValue(String::class.java)
                emailtxt.text =
                    "Email : " + dataSnapshot.child("email").getValue(String::class.java)
                mobiletxt.text =
                    "Mobile no. : " + dataSnapshot.child("mobile").getValue(String::class.java)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@UserActivity, databaseError.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}