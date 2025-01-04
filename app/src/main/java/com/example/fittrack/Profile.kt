package com.example.fittrack

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val (name, email) = getUserDataFromPreferences()

        val nameTextView: TextView = findViewById(R.id.displayName)
        val emailTextView: TextView = findViewById(R.id.displayEmail)

        nameTextView.text = name
        emailTextView.text = email

        val logout = findViewById<CardView>(R.id.btnOut)
        logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginScr::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun getUserDataFromPreferences(): Pair<String, String> {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val name = sharedPreferences.getString("userName", "Name not available") ?: "Name not available"
        val email = sharedPreferences.getString("userEmail", "Email not available") ?: "Email not available"
        return Pair(name, email)
    }
}