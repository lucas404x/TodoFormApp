package com.noti0ns.todoformapp.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.noti0ns.todoformapp.MyApp
import com.noti0ns.todoformapp.data.daos.*
import com.noti0ns.todoformapp.data.models.Task

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(): AppDatabase = instance ?: Room.databaseBuilder(
            MyApp.getInstance(),
            AppDatabase::class.java,
            "sample.db"
        ).build().also {
            instance = it
        }
    }
}