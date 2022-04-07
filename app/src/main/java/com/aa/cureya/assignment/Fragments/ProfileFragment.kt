package com.aa.cureya.assignment.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aa.cureya.assignment.R
import com.aa.cureya.assignment.models.UsersUpload
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        val nametxt = view.findViewById<TextView>(R.id.nametxt)
        val emailtxt = view.findViewById<TextView>(R.id.emailtxt)
        val mobiletxt = view.findViewById<TextView>(R.id.mobiletxt)

        val userid = FirebaseAuth.getInstance().currentUser!!.uid

        val cDataRef = FirebaseDatabase.getInstance().getReference("AllUser").child(userid)
        cDataRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                nametxt.text = "Name : " + dataSnapshot.child("name").getValue(String::class.java)
                emailtxt.text ="Email : " + dataSnapshot.child("email").getValue(String::class.java)
                mobiletxt.text = "Mobile no. : " + dataSnapshot.child("mobile").getValue(String::class.java)

            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, databaseError.message, Toast.LENGTH_SHORT).show()
            }
        })


        return view
    }

}


