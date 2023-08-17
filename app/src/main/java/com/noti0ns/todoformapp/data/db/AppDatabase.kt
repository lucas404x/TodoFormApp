package com.noti0ns.todoformapp.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.noti0ns.todoformapp.MyApp
import com.noti0ns.todoformapp.constants.Directories
import com.noti0ns.todoformapp.data.db.converters.Converters
import com.noti0ns.todoformapp.data.db.daos.*
import com.noti0ns.todoformapp.data.models.Task

@Database(
    entities = [Task::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(): AppDatabase = instance ?: Room.databaseBuilder(
            MyApp.getInstance(),
            AppDatabase::class.java,
            "sample.db"
        ).createFromAsset(
            Directories.database
        ).build().also {
            instance = it
        }
    }
}