package com.example.fittrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WelcomeScr : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome_scr)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnlogin = findViewById<CardView>(R.id.btnlogin)
        val btnsignup = findViewById<CardView>(R.id.btnsignup)
        btnlogin.setOnClickListener{
            val intent1 = Intent(this, LoginScr::class.java)
            startActivity(intent1)
        }
        btnsignup.setOnClickListener{
            val intent2 = Intent(this, SignUpScr::class.java)
            startActivity(intent2)
        }
    }
}