package com.noti0ns.todoformapp.interfaces

import com.noti0ns.todoformapp.data.models.Task

interface TaskRepository {
    suspend fun get(id: Int): Task
    suspend fun getAll(): List<Task>
    suspend fun save(task: Task): Task
    suspend fun update(task: Task): Task
    suspend fun delete(task: Task)
}