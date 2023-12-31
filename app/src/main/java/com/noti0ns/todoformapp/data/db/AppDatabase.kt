package com.noti0ns.todoformapp.data.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.noti0ns.todoformapp.MyApp
import com.noti0ns.todoformapp.data.db.converters.Converters
import com.noti0ns.todoformapp.data.db.daos.*
import com.noti0ns.todoformapp.data.models.Task

@Database(
    entities = [Task::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = instance ?: Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "sample.db"
        ).build().also {
            instance = it
        }
    }
}