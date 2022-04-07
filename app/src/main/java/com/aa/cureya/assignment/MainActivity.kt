package com.aa.cureya.assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {


    private var mAuth: FirebaseAuth? = null
    private var authId: String? = null

    private val RC_SIGN_IN = 243

    lateinit var sendBtn : Button
    lateinit var varifyBtn : Button

    lateinit var otp1 : EditText
    lateinit var otp2 : EditText
    lateinit var otp3 : EditText
    lateinit var otp4 : EditText
    lateinit var otp5 : EditText
    lateinit var otp6 : EditText

    lateinit var progressBar : ProgressBar
    lateinit var progressBar2 : ProgressBar

    lateinit var otptxt : TextView
    lateinit var varifytxt : TextView

    lateinit var editname : EditText
    lateinit var editemail : EditText
    lateinit var editnumber : EditText

    lateinit var linearviewname : LinearLayout
    lateinit var linearviewemail : LinearLayout
    lateinit var linearviewno : LinearLayout
    lateinit var linearviewotp : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()

        sendBtn = findViewById<Button>(R.id.sendBtn)
        varifyBtn = findViewById<Button>(R.id.verifyBtn)

        editname = findViewById<EditText>(R.id.editname)
        editemail = findViewById<EditText>(R.id.editemail)
        editnumber = findViewById<EditText>(R.id.editnumber)

        progressBar = findViewById(R.id.progressBar)


        sendBtn.setOnClickListener(View.OnClickListener {
            if (editname.getText().toString().isEmpty() ||
                editemail.getText().toString().trim().isEmpty() ||
                editnumber.getText().toString().trim().isEmpty())
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            else if (editnumber.getText().toString().trim().length !== 10)
                Toast.makeText(this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show()
            else
                sendOtpToNumber(editnumber.getText().toString())
        })


        varifyBtn.setOnClickListener(View.OnClickListener {
            if (otp1.getText().toString().trim().isEmpty() ||
                otp2.getText().toString().trim().isEmpty() ||
                otp3.getText().toString().trim().isEmpty() ||
                otp4.getText().toString().trim().isEmpty() ||
                otp5.getText().toString().trim().isEmpty() ||
                otp6.getText().toString().trim().isEmpty())
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
            else
                varifyOtp()
        })

    }

    override fun onStart() {
        super.onStart()
        if (mAuth!!.currentUser != null) {
            startActivity(Intent(this, NavActivity::class.java))
            finish()
        }
    }

    private fun sendOtpToNumber(number: String) {
        Log.e("Main...", number)

        otptxt  = findViewById(R.id.otptxt)
        varifytxt  = findViewById(R.id.verifytxt)

        linearviewname = findViewById(R.id.linearviewname)
        linearviewemail = findViewById(R.id.linearviewemail)
        linearviewno = findViewById(R.id.linearviewno)
        linearviewotp = findViewById(R.id.linearviewotp)

        otp1 = findViewById(R.id.otp1)
        otp2 = findViewById(R.id.otp2)
        otp3 = findViewById(R.id.otp3)
        otp4 = findViewById(R.id.otp4)
        otp5 = findViewById(R.id.otp5)
        otp6 = findViewById(R.id.otp6)

        sendBtn.setVisibility(View.INVISIBLE)
        progressBar.setVisibility(View.VISIBLE)
        val mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {}
            override fun onVerificationFailed(e: FirebaseException) {
                progressBar.setVisibility(View.GONE)
                sendBtn.setVisibility(View.VISIBLE)
                Toast.makeText(this@MainActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                authId = verificationId
                otptxt.setText("Enter OTP below, Sent to  +91 $number")
                varifytxt.setVisibility(View.VISIBLE)
                linearviewname.setVisibility(View.INVISIBLE)
                linearviewemail.setVisibility(View.INVISIBLE)
                linearviewno.setVisibility(View.INVISIBLE)
                linearviewotp.setVisibility(View.VISIBLE)
                progressBar.setVisibility(View.INVISIBLE)
                varifyBtn.setVisibility(View.VISIBLE)
//                ortxt.setVisibility(View.INVISIBLE)
//                googleSignBtn.setVisibility(View.INVISIBLE)
                otp1.requestFocus()
                auto_otp_enter()
            }
        }
        val options = PhoneAuthOptions.newBuilder(mAuth!!)
            .setPhoneNumber("+91$number")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun varifyOtp() {

        varifyBtn.setVisibility(View.INVISIBLE)
        progressBar.setVisibility(View.VISIBLE)
        val code: String = otp1.getText().toString().trim() + otp2.getText().toString().trim() + otp3.getText().toString().trim() +
                otp4.getText().toString().trim() + otp5.getText().toString().trim() + otp6.getText().toString().trim()
        val credential = PhoneAuthProvider.getCredential(authId!!, code)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e("main", "Task Success........")
                    val dr_user = FirebaseDatabase.getInstance().getReference("AllUser").child(mAuth!!.currentUser!!.uid)
                    dr_user.child("name").setValue(editname.getText().toString())
                    dr_user.child("email").setValue(editemail.getText().toString())
                    dr_user.child("mobile").setValue("+91 "+editnumber.getText().toString())

                        startActivity(Intent(this, NavActivity::class.java))
                        finish()
//                        getFragmentManager()?.beginTransaction()?.add(R.id.flFragment, AccountFragment())?.remove(this)?.commit()
//                    (activity as MainActivity).bind.bottomNav.selectedItemId = R.id.account

                } else {
                    Log.e("main", "Not Success........")
                    progressBar.setVisibility(View.INVISIBLE)
                    varifyBtn.setVisibility(View.VISIBLE)
                    Toast.makeText(this, "Wrong OTP Enter", Toast.LENGTH_SHORT).show()
                }
            }
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                progressBar2.visibility = View.VISIBLE
//                googleSignBtn.visibility = View.INVISIBLE
//                val account = task.getResult(ApiException::class.java)
//                firebaseAuthWithGoogle(account)
//            } catch (e: ApiException) {
//                Log.e("main", e.message!!)
//            }
//        }
//    }

    private fun auto_otp_enter() {
        otp1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count == 1) otp2.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        otp2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count == 1) otp3.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        otp3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count == 1) otp4.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        otp4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count == 1) otp5.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        otp5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count == 1) otp6.requestFocus()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }


}