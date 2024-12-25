package com.example.fittrack.log

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkoutDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(databaseTable: DatabaseTable)

    @Delete
    suspend fun deleteWorkout(databaseTable: DatabaseTable)

    @Update
    suspend fun updateWorkout(databaseTable: DatabaseTable)

    @Query("SELECT * FROM DatabaseTable WHERE uid = :uid")
    fun getAll(uid: String): LiveData<List<DatabaseTable>>

    @Query("SELECT SUM(duration) FROM DatabaseTable WHERE uid = :uid AND date >= DATE('now', '-7 days')")
    suspend fun getTotalDurationForWeekValue(uid: String): Int

    @Query("SELECT SUM(duration) FROM DatabaseTable WHERE uid = :uid AND date >= DATE('now', 'start of month')")
    suspend fun getTotalDurationForMonthValue(uid: String): Int

    @Query("SELECT SUM(calorie) FROM DatabaseTable WHERE uid = :uid AND date >= DATE('now', '-7 days')")
    suspend fun getTotalCaloriesForWeekValue(uid: String): Int

    @Query("SELECT SUM(calorie) FROM DatabaseTable WHERE uid = :uid AND date >= DATE('now', 'start of month')")
    suspend fun getTotalCaloriesForMonthValue(uid: String): Int
}