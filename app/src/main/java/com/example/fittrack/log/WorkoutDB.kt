package com.example.fittrack.log

import androidx.room.Room
import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlin.jvm.java


@Database(
    entities = [DatabaseTable::class],
    version = 4
)
abstract class WorkoutDB : RoomDatabase() {
    abstract val workoutDAO: WorkoutDAO

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE DatabaseTable ADD COLUMN date TEXT NOT NULL DEFAULT ''")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE DatabaseTable ADD COLUMN firebaseId TEXT")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE DatabaseTable ADD COLUMN uid TEXT NOT NULL DEFAULT ''")
            }
        }

        @Volatile
        private var INSTANCE: WorkoutDB? = null

        fun getDB(context: Context, uid: String): WorkoutDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDB::class.java,
                    "workout_$uid"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
