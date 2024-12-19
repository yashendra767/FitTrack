package com.example.fittrack.log

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WorkoutDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(databaseTable: DatabaseTable)

    @Delete
    suspend fun deleteWorkout(databaseTable: DatabaseTable)

    @Query("Select * from 'DatabaseTable'")
    fun getAll(): LiveData<List<DatabaseTable>>

    @Query("SELECT SUM(duration) FROM DatabaseTable WHERE date >= DATE('now', '-7 days')")
    fun getTotalDurationForWeek(): LiveData<Int>

    @Query("SELECT SUM(duration) FROM DatabaseTable WHERE date >= DATE('now', 'start of month')")
    fun getTotalDurationForMonth(): LiveData<Int>
}