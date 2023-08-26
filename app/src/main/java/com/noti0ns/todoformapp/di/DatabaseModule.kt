package com.noti0ns.todoformapp.di

import android.content.Context
import com.noti0ns.todoformapp.data.db.AppDatabase
import com.noti0ns.todoformapp.data.db.daos.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ) : AppDatabase = AppDatabase.getInstance(context)

    @Provides
    fun provideTaskDao(appDatabase: AppDatabase) : TaskDao = appDatabase.taskDao()
}