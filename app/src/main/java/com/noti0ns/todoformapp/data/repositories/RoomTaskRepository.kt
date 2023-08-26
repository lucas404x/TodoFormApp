package com.noti0ns.todoformapp.data.repositories

import com.noti0ns.todoformapp.data.db.daos.TaskDao
import com.noti0ns.todoformapp.data.models.Task
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomTaskRepository @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
//    companion object {
//        private val taskDao = AppDatabase.getInstance().taskDao()
//    }

    override suspend fun get(id: Int): Task = withContext(Dispatchers.IO) {
        taskDao.get(id)
    }

    override suspend fun getAll(): List<Task> = withContext(Dispatchers.IO) {
        taskDao.getAll()
    }

    override suspend fun save(task: Task): Task = withContext(Dispatchers.IO) {
        taskDao.save(task).let {
            task.copy(id = it.toInt())
        }
    }

    override suspend fun update(task: Task): Task = withContext(Dispatchers.IO) {
        taskDao.update(task).let { task }
    }

    override suspend fun delete(task: Task) = withContext(Dispatchers.IO) {
        taskDao.delete(task)
    }
}