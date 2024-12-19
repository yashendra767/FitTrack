package com.example.fittrack.goals

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GoalsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goalsTable: GoalsTable)

    @Query("SELECT * FROM goalstable ORDER BY id DESC LIMIT 1")
    suspend fun getLastGoal(): GoalsTable?
}