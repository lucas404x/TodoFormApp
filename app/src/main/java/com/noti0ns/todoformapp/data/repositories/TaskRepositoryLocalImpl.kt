package com.noti0ns.todoformapp.data.repositories

import com.noti0ns.todoformapp.data.models.Task
import com.noti0ns.todoformapp.interfaces.TaskRepository

class TaskRepositoryLocalImpl : TaskRepository {
    override suspend fun get(id: Int): Task {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<Task> = listOf(
        Task(1, title = "Task1"),
        Task(2, title = "Task2"),
        Task(3, title = "Task3"),
        Task(4, title = "Task4")
    )

    override suspend fun save(task: Task): Task {
        TODO("Not yet implemented")
    }

    override suspend fun update(task: Task): Task {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Int) {
        TODO("Not yet implemented")
    }

}