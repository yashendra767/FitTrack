package com.example.fittrack.goals

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [DurationGoalsTable::class, CalorieGoalsTable::class],
    version = 2
)
abstract class GoalsDB: RoomDatabase() {
    abstract fun goalsDAO(): GoalsDAO

    companion object {

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // SQL command to create the new table `CalorieGoalsTable`
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `calorie_goals` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`weekly` INTEGER NOT NULL, " +
                            "`monthly` INTEGER NOT NULL)"
                )

                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `duration_goals` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `monthly` INTEGER NOT NULL,
                `weekly` INTEGER NOT NULL
            )
            """
                )
            }
        }

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
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}