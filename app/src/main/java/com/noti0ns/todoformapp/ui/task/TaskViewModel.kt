package com.noti0ns.todoformapp.ui.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noti0ns.todoformapp.data.models.Task
import com.noti0ns.todoformapp.data.repositories.RoomTaskRepository
import com.noti0ns.todoformapp.data.repositories.TaskRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class TaskViewModel : ViewModel() {
    companion object {
        private val _taskRepo: TaskRepository

        init {
            _taskRepo = RoomTaskRepository()
        }
    }

    private var _task = Task()

    private val _uiState = MutableLiveData<UIState>(UIState.Initial)
    val uiState: LiveData<UIState> = _uiState



    fun onLoadTask(taskId: Int) {
        viewModelScope.launch {
            _taskRepo.get(taskId).also {
                _uiState.value = UIState.Loading
                _task = it
                _uiState.value = UIState.Loaded(it)
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
        if (title.isBlank()) {
            _uiState.value = UIState.Error("The title is required", TaskField.TITLE)
        }
        else {
            _uiState.value = UIState.SetFieldData(title, TaskField.TITLE)
        }
    }

    private fun onDescriptionChanged(description: String?) {
        _task = _task.copy(description = description)
        _uiState.value = UIState.SetFieldData(description, TaskField.DESCRIPTION)
    }

    private fun onDueDateChanged(dueDate: LocalDateTime?) {
        _task = _task.copy(dueDate = dueDate)
        var errorMsg: String? = null
        dueDate?.let {
            if (it.toLocalDate() <= LocalDate.now()) {
                errorMsg = "The Due Date must be greater than the current date."
            }
        }
        _uiState.value = if (errorMsg == null) {
            UIState.SetFieldData(dueDate, TaskField.DUE_DATE)
        } else {
            UIState.Error(errorMsg!!, TaskField.DUE_DATE)
        }
    }

    private fun onSubmitTask() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            if (_task.id == 0) {
                _taskRepo.save(_task)
            } else {
                _taskRepo.update(_task)
            }
            _uiState.value = UIState.Finished
        }
    }
}

sealed class TaskViewModelEvent {
    data class TitleChanged(val title: String) : TaskViewModelEvent()
    data class DescriptionChanged(val description: String) : TaskViewModelEvent()
    data class DueDateChanged(val dueDate: LocalDateTime?) : TaskViewModelEvent()
    object SubmitTask : TaskViewModelEvent()
}

sealed class UIState {
    object Initial : UIState()
    object Loading : UIState()
    data class Loaded(val task: Task) : UIState()
    data class SetFieldData<T>(val data: T?, val field: TaskField) : UIState()
    data class Error(val message: String, val field: TaskField) : UIState()
    object Finished : UIState()
}

enum class TaskField { TITLE, DESCRIPTION, DUE_DATE }
