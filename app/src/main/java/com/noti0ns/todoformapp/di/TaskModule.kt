package com.noti0ns.todoformapp.di

import com.noti0ns.todoformapp.data.repositories.RoomTaskRepository
import com.noti0ns.todoformapp.data.repositories.TaskRepository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class TaskModule {
    @Binds
    abstract fun bindTaskRepository(
        taskRepositoryImpl: RoomTaskRepository
    ) : TaskRepository
}