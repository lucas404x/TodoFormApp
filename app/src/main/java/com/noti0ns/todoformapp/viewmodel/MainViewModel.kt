package com.noti0ns.todoformapp.viewmodel

import androidx.lifecycle.LiveData
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

    private var _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    fun loadTasks() = viewModelScope.launch {
        _tasks.value = taskRepo.getAll()
    }

    fun toggleTaskState(taskPos: Int) = viewModelScope.launch {
        _tasks.value?.get(taskPos)?.let {
            it.isDone = !it.isDone
            taskRepo.update(it)
        }
    }
}