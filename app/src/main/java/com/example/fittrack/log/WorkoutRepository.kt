package com.example.fittrack.log

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class WorkoutRepository(private val workoutDAO : WorkoutDAO, private var uid : String) {
    var readAllData: LiveData<List<DatabaseTable>> = workoutDAO.getAll(uid)
    private val firestore = FirebaseFirestore.getInstance()
    private val workoutsCollection = firestore.collection("workouts")

    suspend fun addWorkout(databaseTable: DatabaseTable){
        databaseTable.uid = uid
        workoutDAO.insertWorkout(databaseTable)
        saveWorkoutToFirebase(databaseTable)
    }

    suspend fun deleteWorkout(databaseTable: DatabaseTable){
        workoutDAO.deleteWorkout(databaseTable)
        deleteWorkoutFromFirebase(databaseTable)
    }

    suspend fun updateWorkout(databaseTable: DatabaseTable) {
        workoutDAO.updateWorkout(databaseTable)
        saveWorkoutToFirebase(databaseTable)
    }

    suspend fun getTotalDurationForWeekValue(): Int {
        return workoutDAO.getTotalDurationForWeekValue(uid)
    }

    suspend fun getTotalDurationForMonthValue(): Int {
        return workoutDAO.getTotalDurationForMonthValue(uid)
    }

    suspend fun getTotalCaloriesForWeekValue(): Int {
        return workoutDAO.getTotalCaloriesForWeekValue(uid)
    }

    suspend fun getTotalCaloriesForMonthValue(): Int {
        return workoutDAO.getTotalCaloriesForMonthValue(uid)
    }

    private suspend fun saveWorkoutToFirebase(databaseTable: DatabaseTable) {
        try {
            var document = if (databaseTable.firebaseId.isNullOrEmpty()) {
                workoutsCollection.add(databaseTable).await()
            } else {
                workoutsCollection.document(databaseTable.firebaseId!!).set(databaseTable).await()
                workoutsCollection.document(databaseTable.firebaseId!!)
            }
            databaseTable.firebaseId = document.id
            workoutDAO.insertWorkout(databaseTable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun deleteWorkoutFromFirebase(databaseTable: DatabaseTable) {
        try {
            databaseTable.firebaseId?.let {
                workoutsCollection.document(it).delete().await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun syncDataFromFirebase() {
        try {
            var snapshot = workoutsCollection.whereEqualTo("uid", uid).get().await()
            for (document in snapshot.documents) {
                var workout = document.toObject(DatabaseTable::class.java)
                workout?.let {
                    it.firebaseId = document.id
                    workoutDAO.insertWorkout(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}