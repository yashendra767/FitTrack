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
    suspend fun insertDurationGoal(goal: DurationGoalsTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalorieGoal(goal: CalorieGoalsTable)

    @Query("SELECT * FROM duration_goals ORDER BY id DESC LIMIT 1")
    suspend fun getLastDurationGoal(): DurationGoalsTable?

    @Query("SELECT * FROM calorie_goals ORDER BY id DESC LIMIT 1")
    suspend fun getLastCalorieGoal(): CalorieGoalsTable?

    @Query("SELECT * FROM duration_goals ORDER BY id DESC")
    fun getAllDurationGoals(): LiveData<List<DurationGoalsTable>>

    @Query("SELECT * FROM calorie_goals ORDER BY id DESC")
    fun getAllCalorieGoals(): LiveData<List<CalorieGoalsTable>>
}