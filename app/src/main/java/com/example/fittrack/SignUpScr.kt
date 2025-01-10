package com.example.fittrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpScr : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_scr)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        val nameUser = findViewById<TextInputEditText>(R.id.name)
        val emailUser = findViewById<TextInputEditText>(R.id.email)
        val passUser = findViewById<TextInputEditText>(R.id.pass)
        val signButton = findViewById<CardView>(R.id.btnCreate)
        signButton.setOnClickListener {
            val name = nameUser.text.toString()
            val email = emailUser.text.toString()
            val pass = passUser.text.toString()
            if (!email.contains("@gmail.com")) {
                Toast.makeText(this, "Enter email in the correct format (e.g., example@gmail.com)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pass.length < 6) {
                Toast.makeText(this, "Password should be at least 6 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val specialCharacterPattern = "[!@#\$%^&*(),.?\":{}|<>]"
            if (!pass.matches(Regex(".*$specialCharacterPattern.*"))) {
                Toast.makeText(this, "Password must contain at least one special character", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userId = user?.uid ?: ""

                        val profileUpdates = userProfileChangeRequest {
                            displayName = name
                        }
                        user?.updateProfile(profileUpdates)?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                val userObject = User(name, email, pass)
                                database = FirebaseDatabase.getInstance().getReference("Users")
                                database.child(userId).setValue(userObject).addOnSuccessListener {
                                    Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, LoginScr::class.java)
                                    startActivity(intent)
                                    finish()
                                }.addOnFailureListener {
                                    Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this, "Failed to update profile: ${updateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

        }

        val existingUser = findViewById<TextView>(R.id.textView3)
        existingUser.setOnClickListener {
            val intentExist = Intent(this, LoginScr::class.java)
            startActivity(intentExist)
        }
    }
}
