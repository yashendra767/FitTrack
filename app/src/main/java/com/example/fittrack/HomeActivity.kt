package com.example.fittrack

import WorkoutEntryFragment
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val name = intent.getStringExtra(LoginScr.KEY)
        val welcomeTxt = findViewById<TextView>(R.id.textWelcome)
        val logout = findViewById<CardView>(R.id.btnLogout)
        if(name != null){
            welcomeTxt.text = "Welcome $name"
        }
        else {
            welcomeTxt.text = "Welcome"
        }

        logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginScr::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        val bottomView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        replaceTheFragment(WorkoutEntryFragment())
        bottomView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.item1 -> replaceTheFragment(WorkoutEntryFragment())
                R.id.item2 -> replaceTheFragment(RecyclerWorkout())
                R.id.item3 -> replaceTheFragment(GoalsFragment())
            }
            true
        }

    }
    fun replaceTheFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()

    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
       finishAffinity()
    }
}