package com.noti0ns.todoformapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noti0ns.todoformapp.data.models.Task
import com.noti0ns.todoformapp.data.repositories.RoomTaskRepository
import com.noti0ns.todoformapp.data.repositories.TaskRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MainViewModel : ViewModel() {
    companion object {
        private lateinit var _taskRepo: TaskRepository
    }

    init {
        _taskRepo = RoomTaskRepository()
    }

    private val _tasks = MutableLiveData<MutableList<Task>>()
    val tasks: LiveData<MutableList<Task>> = _tasks

    private val _taskUpdated = MutableLiveData<Pair<Int, Task>?>()
    val taskUpdated: LiveData<Pair<Int, Task>?> = _taskUpdated

    fun loadTasks() = viewModelScope.launch {
        _tasks.value = _taskRepo.getAll().sortedBy { x -> x.isDone }.toMutableList()
    }

    fun toggleTaskState(taskPos: Int) = viewModelScope.launch {
        _tasks.value?.getOrNull(taskPos)?.let {
            val isDoneToggled = !it.isDone
            val updatedTask = it.copy(
                isDone = isDoneToggled,
                dateUpdated = LocalDateTime.now(),
                dateFinished = if (isDoneToggled) LocalDateTime.now() else null
            )
            _taskRepo.update(updatedTask)
            _tasks.value?.set(taskPos, updatedTask)
            _taskUpdated.value = Pair(taskPos, updatedTask)
        }
    }
}