package com.example.fittrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpScr : AppCompatActivity() {
    lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_scr)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val nameUser = findViewById<TextInputEditText>(R.id.name)
        val emailUser = findViewById<TextInputEditText>(R.id.email)
        val passUser = findViewById<TextInputEditText>(R.id.pass)
        val signButton = findViewById<Button>(R.id.btnCreate)
        signButton.setOnClickListener{
            val name = nameUser.text.toString()
            val email = emailUser.text.toString().replace(".", ",")
            val pass = passUser.text.toString()
            val user = User(name,email,pass)
            database = FirebaseDatabase.getInstance().getReference("Users")
            database.child(email).setValue(user).addOnSuccessListener(){
                Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this,"Account Creation Failed",Toast.LENGTH_SHORT).show()
            }
        }
        val existingUser = findViewById<TextView>(R.id.textView3)
        existingUser.setOnClickListener{
            val intentExist = Intent(this, LoginScr::class.java)
            startActivity(intentExist)
        }
    }
}