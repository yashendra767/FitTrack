package com.example.fittrack

import WorkoutViewModelFactory
import android.app.Application
import android.graphics.Color
import android.os.Bundle
import com.example.fittrack.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fittrack.goals.CalorieGoalsTable
import com.example.fittrack.goals.DurationGoalsTable
import com.example.fittrack.goals.GoalsTable
import com.example.fittrack.goals.GoalsViewModel
import com.example.fittrack.log.WorkoutViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class GoalsFragment : Fragment() {
    private lateinit var viewModel: GoalsViewModel
    private lateinit var workoutViewModel: WorkoutViewModel
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    private var goalType : String = "Duration"

    private var currentWeeklyProgress: Int = 0
    private var currentMonthlyProgress: Int = 0
    private var weeklyGoal: Int = 0
    private var monthlyGoal: Int = 0

    private lateinit var progressBarWeekly: ProgressBar
    private lateinit var progressTextWeekly: TextView
    private lateinit var progressBarMonthly: ProgressBar
    private lateinit var progressTextMonthly: TextView

    private lateinit var progressBarCalorieWeekly: ProgressBar
    private lateinit var progressTextCalorieWeekly: TextView
    private lateinit var progressBarCalorieMonthly: ProgressBar
    private lateinit var progressTextCalorieMonthly: TextView

    private lateinit var cardViewDurationWeekly: CardView
    private lateinit var cardViewDurationMonthly: CardView
    private lateinit var cardViewCalorieMonthly: CardView
    private lateinit var cardViewCalorieWeekly: CardView




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
        val setGoalsButton = view.findViewById<CardView>(R.id.btnSetGoals)

        //Duration

        progressBarWeekly = view.findViewById<ProgressBar>(R.id.progressBarWeekly)
        progressTextWeekly = view.findViewById<TextView>(R.id.progressTextWeekly)
        progressBarMonthly = view.findViewById<ProgressBar>(R.id.progressBarMonthly)
        progressTextMonthly = view.findViewById<TextView>(R.id.progressTextMonthly)

        //Calorie

        progressBarCalorieWeekly = view.findViewById<ProgressBar>(R.id.progressBarCalorieWeekly)
        progressTextCalorieWeekly = view.findViewById<TextView>(R.id.progressTextCalorieWeekly)
        progressBarCalorieMonthly = view.findViewById<ProgressBar>(R.id.progressBarCalorieMonthly)
        progressTextCalorieMonthly = view.findViewById<TextView>(R.id.progressTextCalorieMonthly)


        cardViewDurationMonthly=view.findViewById<CardView>(R.id.cardViewDurationMonth)
        cardViewDurationWeekly=view.findViewById<CardView>(R.id.cardViewDurationWeek)
        cardViewCalorieWeekly=view.findViewById<CardView>(R.id.calorieCardViewWeek)
        cardViewCalorieMonthly=view.findViewById<CardView>(R.id.calorieCardViewMonth)

        val goalTypeSpinner = view.findViewById<Spinner>(R.id.goalTypeSpinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.goal_type_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            goalTypeSpinner.adapter = adapter
        }

        goalTypeSpinner.setSelection(0, false)


        goalTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                goalType = parent.getItemAtPosition(position).toString()
                updateProgressBars()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Kuchhh na
            }
        }


        viewModel.getLastGoal(goalType) { goal ->
            goal?.let {
                if (goalType == "Duration") {
                    val durationGoal = it as DurationGoalsTable
                    weeklyGoal = durationGoal.weekly
                    monthlyGoal = durationGoal.monthly
                } else {
                    val calorieGoal = it as CalorieGoalsTable
                    weeklyGoal = calorieGoal.weekly
                    monthlyGoal = calorieGoal.monthly
                }
                fetchAndUpdateProgress(weeklyGoal, monthlyGoal, goalType)
            }
        }

        setGoalsButton.setOnClickListener {
            val weeklyGoalInput = weeklyGoalEditText.text.toString()
            val monthlyGoalInput = monthlyGoalEditText.text.toString()

            if (weeklyGoalInput.isEmpty() || monthlyGoalInput.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter all the goals", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            weeklyGoal = weeklyGoalInput.toInt()
            monthlyGoal = monthlyGoalInput.toInt()
            viewModel.saveGoals(weeklyGoal, monthlyGoal, goalType)
            fetchAndUpdateProgress(weeklyGoal, monthlyGoal, goalType)
        }


        return view
    }
    fun updateProgressBars() {
        if (goalType == "Duration") {
            progressBarWeekly.visibility = View.VISIBLE
            progressTextWeekly.visibility = View.VISIBLE
            progressBarMonthly.visibility = View.VISIBLE
            progressTextMonthly.visibility = View.VISIBLE

            progressBarCalorieWeekly.visibility = View.GONE
            progressTextCalorieWeekly.visibility = View.GONE
            progressBarCalorieMonthly.visibility = View.GONE
            progressTextCalorieMonthly.visibility = View.GONE

            cardViewCalorieWeekly.visibility = View.GONE
            cardViewCalorieMonthly.visibility = View.GONE
            cardViewDurationWeekly.visibility = View.VISIBLE
            cardViewDurationMonthly.visibility = View.VISIBLE



            progressBarWeekly.progress = currentWeeklyProgress
            progressTextWeekly.text = "$currentWeeklyProgress%"
            progressBarMonthly.progress = currentMonthlyProgress
            progressTextMonthly.text = "$currentMonthlyProgress%"
        }else if (goalType == "Calories") {
            progressBarCalorieWeekly.visibility = View.VISIBLE
            progressTextCalorieWeekly.visibility = View.VISIBLE
            progressBarCalorieMonthly.visibility = View.VISIBLE
            progressTextCalorieMonthly.visibility = View.VISIBLE
            
            progressBarWeekly.visibility = View.GONE
            progressTextWeekly.visibility = View.GONE
            progressBarMonthly.visibility = View.GONE
            progressTextMonthly.visibility = View.GONE


            cardViewCalorieWeekly.visibility = View.VISIBLE
            cardViewCalorieMonthly.visibility = View.VISIBLE
            cardViewDurationWeekly.visibility = View.GONE
            cardViewDurationMonthly.visibility = View.GONE

            progressBarCalorieWeekly.progress = currentWeeklyProgress
            progressTextCalorieWeekly.text = "$currentWeeklyProgress%"
            progressBarCalorieMonthly.progress = currentMonthlyProgress
            progressTextCalorieMonthly.text = "$currentMonthlyProgress%"

        }
    }


    private fun fetchAndUpdateProgress(weeklyGoal: Int, monthlyGoal: Int, goalType: String) {
        if (goalType == "Duration") {
            workoutViewModel.getWeeklyProgress(weeklyGoal, goalType).observe(viewLifecycleOwner, Observer { weeklyProgress ->
                currentWeeklyProgress = weeklyProgress
                updateProgressBars()
            })
            workoutViewModel.getMonthlyProgress(monthlyGoal, goalType).observe(viewLifecycleOwner, Observer { monthlyProgress ->
                currentMonthlyProgress = monthlyProgress
                updateProgressBars()
            })
        } else {
            workoutViewModel.getWeeklyProgress(weeklyGoal, goalType).observe(viewLifecycleOwner, Observer { weeklyProgress ->
                currentWeeklyProgress = weeklyProgress
                updateProgressBars()
            })
            workoutViewModel.getMonthlyProgress(monthlyGoal, goalType).observe(viewLifecycleOwner, Observer { monthlyProgress ->
                currentMonthlyProgress = monthlyProgress
                updateProgressBars()
            })
        }
    }
}
