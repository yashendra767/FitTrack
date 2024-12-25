package com.example.fittrack.log

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseTable(
    var exName: String,
    var duration: Int,
    var calorie: Int,
    val date: String,
    var uid: String,
    var firebaseId: String? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)