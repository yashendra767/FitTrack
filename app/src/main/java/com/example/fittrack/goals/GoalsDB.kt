package com.example.fittrack.goals

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [GoalsTable::class],
    version = 1
)
abstract class GoalsDB: RoomDatabase() {
    abstract fun goalsDAO(): GoalsDAO

    companion object {
        @Volatile
        private var INSTANCE: GoalsDB?= null
        fun getDB(context: Context): GoalsDB{
            val tempInstance =INSTANCE
            if (tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GoalsDB::class.java,
                    "goals_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}