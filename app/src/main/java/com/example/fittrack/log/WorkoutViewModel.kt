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
        val workoutDAO = WorkoutDB.getDB(application, uid).workoutDAO
        repository = WorkoutRepository(workoutDAO)
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

    fun getTotalDurationForWeek(): LiveData<Int> {
        return repository.getTotalDurationForWeek()
    }

    fun getTotalDurationForMonth(): LiveData<Int> {
        return repository.getTotalDurationForMonth()
    }


}