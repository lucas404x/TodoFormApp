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

    private var _tasks = mutableListOf<Task>()

    private val _uiState = MutableLiveData<UIState>(UIState.Initial)
    val uiState: LiveData<UIState> = _uiState


    fun loadTasks() = viewModelScope.launch {
        _tasks = _taskRepo.getAll().sortedBy { x -> x.isDone }.toMutableList()
        _uiState.value = UIState.Loaded(_tasks)
    }

    fun toggleTaskState(taskPos: Int) = viewModelScope.launch {
        _tasks.getOrNull(taskPos)?.let {
            val isDoneToggled = !it.isDone
            val updatedTask = it.copy(
                isDone = isDoneToggled,
                dateUpdated = LocalDateTime.now(),
                dateFinished = if (isDoneToggled) LocalDateTime.now() else null
            )
            _taskRepo.update(updatedTask)
            _tasks[taskPos] = updatedTask
            _uiState.value = UIState.TaskUpdated(taskPos, updatedTask)
        }
    }

    fun deleteTask(taskPos: Int) = viewModelScope.launch {
        _tasks.getOrNull(taskPos)?.let {
            _taskRepo.delete(it)
            _tasks.removeAt(taskPos)
            _uiState.value = UIState.TaskRemoved(taskPos)
        }
    }

    sealed class UIState {
        object Initial : UIState()
        data class Loaded(val tasks: MutableList<Task>) : UIState()
        data class TaskUpdated(val taskPosition: Int, val updatedTask: Task) : UIState()
        data class TaskRemoved(val taskPosition: Int) : UIState()
    }
}
