package com.example.fittrack.log

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class WorkoutRepository(private val workoutDAO : WorkoutDAO) {
    val readAllData: LiveData<List<DatabaseTable>> = workoutDAO.getAll()
    private val firestore = FirebaseFirestore.getInstance()
    private val workoutsCollection = firestore.collection("workouts")

    suspend fun addWorkout(databaseTable: DatabaseTable){
        workoutDAO.insertWorkout(databaseTable)

        saveWorkoutToFirebase(databaseTable)
    }

    suspend fun deleteWorkout(databaseTable: DatabaseTable){
        workoutDAO.deleteWorkout(databaseTable)

        deleteWorkoutFromFirebase(databaseTable)
    }

    fun getTotalDurationForWeek(): LiveData<Int> {
        return workoutDAO.getTotalDurationForWeek()
    }

    fun getTotalDurationForMonth(): LiveData<Int> {
        return workoutDAO.getTotalDurationForMonth()
    }

    private suspend fun saveWorkoutToFirebase(databaseTable: DatabaseTable) {
        try {
            val document = if (databaseTable.firebaseId.isNullOrEmpty()) {
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
            val snapshot = workoutsCollection.get().await()
            for (document in snapshot.documents) {
                val workout = document.toObject(DatabaseTable::class.java)
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