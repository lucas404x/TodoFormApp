package com.noti0ns.todoformapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noti0ns.todoformapp.data.models.Task
import com.noti0ns.todoformapp.data.repositories.RoomTaskRepository
import com.noti0ns.todoformapp.interfaces.TaskRepository

class TaskViewModel : ViewModel() {
    companion object {
        private val taskRepo: TaskRepository

        init {
            taskRepo = RoomTaskRepository()
        }
    }

    private val _task: MutableLiveData<Task> = MutableLiveData(Task())
    val task: LiveData<Task> = _task

    fun loadTask(task: Task) {
        _task.value = task
    }
}