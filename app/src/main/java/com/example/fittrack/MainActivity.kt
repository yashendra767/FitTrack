package com.example.fittrack

import WorkoutEntryFragment
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()
        firebaseAuth = FirebaseAuth.getInstance()
        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginScr::class.java)
                startActivity(intent)
            }
            finish()
        }, 2000)



    }


}

