package com.noti0ns.todoformapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noti0ns.todoformapp.data.models.Task
import com.noti0ns.todoformapp.data.repositories.RoomTaskRepository
import com.noti0ns.todoformapp.interfaces.TaskRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    companion object {
        private lateinit var taskRepo: TaskRepository
    }

    init {
        taskRepo = RoomTaskRepository()
    }

    private var _tasks = listOf<Task>()

    val tasks: MutableLiveData<List<Task>> by lazy {
        MutableLiveData(_tasks)
    }

    fun loadTasks() {
        viewModelScope.launch {
            _tasks = taskRepo.getAll()
            tasks.value = _tasks
        }
    }

    fun toggleTaskState(taskPos: Int) {
        viewModelScope.launch {
            _tasks[taskPos].isDone = !_tasks[taskPos].isDone
            taskRepo.update(_tasks[taskPos])
        }
    }
}