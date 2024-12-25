package com.example.fittrack

import WorkoutViewModelFactory
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.example.fittrack.log.DatabaseTable
import com.example.fittrack.log.WorkoutViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import kotlin.time.Duration

class UpdateWorkout : AppCompatActivity() {

    private lateinit var upExerciseName: TextInputEditText
    private lateinit var workoutViewModel: WorkoutViewModel
    private lateinit var upDuration: TextInputEditText
    private lateinit var upCalories: TextInputEditText
    private lateinit var btnUpdate: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_workout)

        upExerciseName = findViewById(R.id.updateExerciseName)
        upDuration = findViewById(R.id.updateDuration)
        upCalories = findViewById(R.id.updateCalories)
        btnUpdate = findViewById(R.id.btnUpdate)

        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val factory = WorkoutViewModelFactory(application, uid)
        workoutViewModel = ViewModelProvider(this, factory).get(WorkoutViewModel::class.java)
        val existingWorkoutId = intent.getIntExtra("EXTRA_WORKOUT_ID", -1)

        btnUpdate.setOnClickListener{
            val newExName = upExerciseName.text.toString()
            val newDuration = upDuration.text.toString().toInt()
            val newCalories = upCalories.text.toString().toInt()
            val existingDate = intent.getStringExtra("EXTRA_DATE")

            val newWorkout = DatabaseTable(newExName,newDuration,newCalories,existingDate!!,uid, id = existingWorkoutId)
            workoutViewModel.insertWorkout(newWorkout)
            finish()
        }
    }
}