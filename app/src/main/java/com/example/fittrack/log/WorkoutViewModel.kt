package com.example.fittrack.log

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class WorkoutViewModel(application: Application, uid: String) : AndroidViewModel(application) {
    val readAllData : LiveData<List<DatabaseTable>>
    private val repository : WorkoutRepository


    init{
        var workoutDAO = WorkoutDB.getDB(application, uid).workoutDAO
        repository = WorkoutRepository(workoutDAO, uid)
        readAllData = repository.readAllData

        viewModelScope.launch(Dispatchers.IO) {
            repository.syncDataFromFirebase()
        }

    }

    fun insertWorkout(databaseTable: DatabaseTable){
        viewModelScope.launch(Dispatchers.IO){
            repository.addWorkout(databaseTable)
        }
    }

    fun deleteWorkout(databaseTable: DatabaseTable){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteWorkout(databaseTable)
        }
    }

    fun updateWorkout(databaseTable: DatabaseTable) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateWorkout(databaseTable)
        }
    }


    fun getWeeklyProgress(goal: Int, goalType: String): LiveData<Int> {
        val progress = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            val totalValue = if (goalType == "Duration") {
                repository.getTotalDurationForWeekValue()
            } else {
                repository.getTotalCaloriesForWeekValue()
            }
            val progressValue = if (goal > 0) (totalValue * 100) / goal else 0
            progress.postValue(progressValue)
        }
        return progress
    }

    fun getMonthlyProgress(goal: Int, goalType: String): LiveData<Int> {
        val progress = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            val totalValue = if (goalType == "Duration") {
                repository.getTotalDurationForMonthValue()
            } else {
                repository.getTotalCaloriesForMonthValue()
            }
            val progressValue = if (goal > 0) (totalValue * 100) / goal else 0
            progress.postValue(progressValue)
        }
        return progress
    }



}