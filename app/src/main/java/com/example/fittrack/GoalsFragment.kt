package com.example.fittrack

import WorkoutViewModelFactory
import android.app.Application
import android.os.Bundle
import com.example.fittrack.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fittrack.goals.GoalsTable
import com.example.fittrack.goals.GoalsViewModel
import com.example.fittrack.log.WorkoutViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class GoalsFragment : Fragment() {
    private lateinit var viewModel: GoalsViewModel
    private lateinit var workoutViewModel: WorkoutViewModel
    val uid = FirebaseAuth.getInstance().currentUser?.uid


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_goals, container, false)
        viewModel = ViewModelProvider(this).get(GoalsViewModel::class.java)
        val factory = WorkoutViewModelFactory(requireActivity().application, uid.toString())
        workoutViewModel = ViewModelProvider(this, factory).get(WorkoutViewModel::class.java)

        val weeklyGoalEditText = view.findViewById<TextInputEditText>(R.id.weeklyGoalEditText)
        val monthlyGoalEditText = view.findViewById<TextInputEditText>(R.id.monthlyGoalEditText)
        val weekTv = view.findViewById<TextView>(R.id.weekTv)
        val monthTv = view.findViewById<TextView>(R.id.monthTv)
        val setGoalsButton = view.findViewById<CardView>(R.id.btnSetGoals)
        val weekAchievement = view.findViewById<TextView>(R.id.weekAchievement)
        val monthAchievement = view.findViewById<TextView>(R.id.monthAchievement)

        setGoalsButton.setOnClickListener {
            val weeklyGoal = weeklyGoalEditText.text.toString().toIntOrNull() ?: 0
            val monthlyGoal = monthlyGoalEditText.text.toString().toIntOrNull() ?: 0
            viewModel.saveGoals(weeklyGoal, monthlyGoal)
            viewModel.getLastGoal { goal ->
                goal?.let {
                    weekTv.text = it.weekly.toString()
                    monthTv.text = it.monthly.toString()
                }
            }
        }

        workoutViewModel.getTotalDurationForWeek().observe(viewLifecycleOwner, Observer { totalDurationForWeek ->
            weekAchievement.text = "You have completed $totalDurationForWeek out of ${weekTv.text} in this week."
        })

        workoutViewModel.getTotalDurationForMonth().observe(viewLifecycleOwner, Observer { totalDurationForMonth ->
            monthAchievement.text = "You have completed $totalDurationForMonth out of ${monthTv.text} in this month."
        })



        return view
    }
}
