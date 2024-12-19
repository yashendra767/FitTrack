package com.example.fittrack.goals

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.app.Application
import androidx.lifecycle.AndroidViewModel


class GoalsViewModel(application: Application) : AndroidViewModel(application) {
    private val goalDao = GoalsDB.getDB(application).goalsDAO()

    fun saveGoals(weeklyGoal: Int, monthlyGoal: Int) {
        viewModelScope.launch {
            val goal = GoalsTable(weeklyGoal, monthlyGoal)
            goalDao.insertGoal(goal)
        }
    }

    fun getLastGoal(callback: (GoalsTable?) -> Unit) {
        viewModelScope.launch {
            val goal = goalDao.getLastGoal()
            callback(goal)
        }
    }
}
