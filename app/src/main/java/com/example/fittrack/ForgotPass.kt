  package com.example.fittrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

  class ForgotPass : AppCompatActivity() {

    private var firebaseAuth : FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_pass)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        firebaseAuth = FirebaseAuth.getInstance()
        val btnReset = findViewById<CardView>(R.id.btnReset)
        btnReset.setOnClickListener {
            reset()
        }
    }


    fun reset(){
        val emailR = findViewById<TextInputEditText>(R.id.emailReset)
       val email = emailR.text.toString()
       if(email.isEmpty()){
           Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show()
       }
        firebaseAuth?.sendPasswordResetEmail(email)?.addOnCompleteListener{ task ->
            if (task.isSuccessful){
                Toast.makeText(this,"Reset Email Sent Successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginScr::class.java))
            }
            else{
                Toast.makeText(this,"It's us! Reset email not sent", Toast.LENGTH_SHORT).show()
            }
        }
    }
}