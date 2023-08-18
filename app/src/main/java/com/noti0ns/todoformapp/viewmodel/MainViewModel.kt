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
        private lateinit var _taskRepo: TaskRepository
    }

    init {
        _taskRepo = RoomTaskRepository()
    }

    private var _tasks = MutableLiveData<MutableList<Task>>()
    val tasks: LiveData<MutableList<Task>> = _tasks

    fun loadTasks() = viewModelScope.launch {
        _tasks.value = _taskRepo.getAll().toMutableList()
    }

    fun toggleTaskState(taskPos: Int) = viewModelScope.launch {
        _tasks.value?.getOrNull(taskPos)?.let {
            val task = it.copy(isDone = !it.isDone)
            _taskRepo.update(task)
            _tasks.value?.set(taskPos, task)
            _tasks.value = _tasks.value?.sortedBy { x -> x.isDone }?.toMutableList()
        }
    }
}