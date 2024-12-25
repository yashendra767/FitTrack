package com.example.fittrack.goals

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "duration_goals")
data class DurationGoalsTable(
    var weekly: Int,
    var monthly: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)
