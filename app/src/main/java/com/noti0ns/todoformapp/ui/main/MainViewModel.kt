package com.noti0ns.todoformapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noti0ns.todoformapp.data.models.Task
import com.noti0ns.todoformapp.data.repositories.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject internal constructor(
    private val taskRepository: TaskRepository
)  : ViewModel() {
    private var _tasks = mutableListOf<Task>()

    private val _uiState = MutableLiveData<UIState>(UIState.Initial)
    val uiState: LiveData<UIState> = _uiState


    fun loadTasks() = viewModelScope.launch {
        _tasks = taskRepository.getAll().sortedBy { x -> x.isDone }.toMutableList()
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
            taskRepository.update(updatedTask)
            _tasks[taskPos] = updatedTask
            _uiState.value = UIState.TaskUpdated(taskPos, updatedTask)
        }
    }

    fun deleteTask(taskPos: Int) = viewModelScope.launch {
        _tasks.getOrNull(taskPos)?.let {
            taskRepository.delete(it)
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
