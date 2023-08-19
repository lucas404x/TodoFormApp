package com.noti0ns.todoformapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noti0ns.todoformapp.data.models.Task
import com.noti0ns.todoformapp.data.repositories.RoomTaskRepository
import com.noti0ns.todoformapp.interfaces.TaskRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

typealias TaskState = Pair<Task, Boolean>

class TaskViewModel : ViewModel() {
    companion object {
        private val _taskRepo: TaskRepository

        init {
            _taskRepo = RoomTaskRepository()
        }
    }

    private var _task = Task()

    private val _titleState = MutableLiveData<FieldFormState<String>>(FieldFormState())
    val titleState: LiveData<FieldFormState<String>> = _titleState

    private val _descriptionState = MutableLiveData<FieldFormState<String>>(FieldFormState())
    val descriptionState: LiveData<FieldFormState<String>> = _descriptionState

    private val _dueDateState = MutableLiveData<FieldFormState<LocalDateTime>>(FieldFormState())
    val dueDateState: LiveData<FieldFormState<LocalDateTime>> = _dueDateState

    private val _uiState = MutableLiveData(TaskUiState.INITIAL)
    val uiState: LiveData<TaskUiState> = _uiState

    private var _taskState = MutableLiveData(Pair(_task, false))
    val taskState: LiveData<TaskState> = _taskState


    fun onLoadTask(taskId: Int) {
        _uiState.value = TaskUiState.LOADING
        viewModelScope.launch {
            _taskRepo.get(taskId).also {
                _titleState.value = FieldFormState(it.title)
                _descriptionState.value = FieldFormState(it.description)
                _dueDateState.value = FieldFormState(it.dueDate)
                _task = it
                _taskState.value = TaskState(it, true)
                _uiState.value = TaskUiState.LOADED
            }

        }
    }

    fun onInvokeEvent(event: TaskViewModelEvent) = when (event) {
        is TaskViewModelEvent.TitleChanged -> onTitleChanged(event.title)
        is TaskViewModelEvent.DescriptionChanged -> onDescriptionChanged(event.description)
        is TaskViewModelEvent.DueDateChanged -> onDueDateChanged(event.dueDate)
        TaskViewModelEvent.SubmitTask -> onSubmitTask()
    }

    private fun onTitleChanged(title: String) {
        _task = _task.copy(title = title)
        _titleState.value = _titleState.value?.copy(
            title,
            if (title.isBlank()) "The title is required" else null
        )
    }

    private fun onDescriptionChanged(description: String?) {
        _task = _task.copy(description = description)
        _descriptionState.value = _descriptionState.value?.copy(description)
    }

    private fun onDueDateChanged(dueDate: LocalDateTime?) {
        _task = _task.copy(dueDate = dueDate)
        _dueDateState.value = _dueDateState.value?.copy(dueDate)
    }

    private fun onSubmitTask() {
        viewModelScope.launch {
            _uiState.value = TaskUiState.LOADING
            if (_task.id == 0) {
                _taskRepo.save(_task)
            } else {
                _taskRepo.update(_task)
            }
            _uiState.value = TaskUiState.FINISHED
        }
    }
}

data class FieldFormState<T>(val data: T?, val error: String?) {
    constructor() : this(null, null)
    constructor(data: T?) : this(data, null)
}

sealed class TaskViewModelEvent {
    data class TitleChanged(val title: String) : TaskViewModelEvent()
    data class DescriptionChanged(val description: String) : TaskViewModelEvent()
    data class DueDateChanged(val dueDate: LocalDateTime?) : TaskViewModelEvent()
    object SubmitTask : TaskViewModelEvent()
}

enum class TaskUiState {
    INITIAL,
    LOADING,
    LOADED,
    FINISHED
}