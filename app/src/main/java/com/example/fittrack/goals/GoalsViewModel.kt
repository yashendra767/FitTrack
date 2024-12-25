package com.example.fittrack.goals

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.app.Application
import androidx.lifecycle.AndroidViewModel


class GoalsViewModel(application: Application) : AndroidViewModel(application) {
    private val goalDao = GoalsDB.getDB(application).goalsDAO()

    fun saveGoals(weeklyGoal: Int, monthlyGoal: Int, goalType: String) {
        viewModelScope.launch {
            if (goalType == "Duration") {
                val goal = DurationGoalsTable(weeklyGoal, monthlyGoal)
                goalDao.insertDurationGoal(goal)
            } else {
                val goal = CalorieGoalsTable(weeklyGoal, monthlyGoal)
                goalDao.insertCalorieGoal(goal)
            }
        }
    }

    fun getLastGoal(goalType: String, callback: (Any?) -> Unit) {
        viewModelScope.launch {
            val goal = if (goalType == "Duration") {
                goalDao.getLastDurationGoal()
            } else {
                goalDao.getLastCalorieGoal()
            }
            callback(goal)
        }
    }
}
