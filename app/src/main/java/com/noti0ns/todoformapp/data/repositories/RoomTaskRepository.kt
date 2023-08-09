package com.noti0ns.todoformapp.data.repositories

import com.noti0ns.todoformapp.data.db.AppDatabase
import com.noti0ns.todoformapp.data.models.Task
import com.noti0ns.todoformapp.interfaces.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomTaskRepository(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) :
    TaskRepository {
    companion object {
        private val taskDao = AppDatabase.getInstance().taskDao()
    }

    override suspend fun get(id: Int): Task {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<Task> = withContext(dispatcher) {
        taskDao.getAll()
    }

    override suspend fun save(task: Task): Task = withContext(dispatcher) {
        taskDao.save(task).let {
            task.id = it.toInt()
            task
        }
    }

    override suspend fun update(task: Task): Task = withContext(dispatcher) {
        taskDao.update(task).let { task }
    }

    override suspend fun delete(id: Int) {
        TODO("Not yet implemented")
    }

}