package com.example.fittrack

import WorkoutEntryFragment
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

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
        if(name != null){
            welcomeTxt.text = "Welcome $name"
        }
        else {
            welcomeTxt.text = "Welcome"
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
}