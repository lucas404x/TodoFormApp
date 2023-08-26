package com.noti0ns.todoformapp.ui.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noti0ns.todoformapp.data.models.Task
import com.noti0ns.todoformapp.data.repositories.RoomTaskRepository
import com.noti0ns.todoformapp.data.repositories.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject internal constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    private var _task = Task()

    private val _uiState = MutableLiveData<UIState>(UIState.Initial)
    val uiState: LiveData<UIState> = _uiState

    fun onLoadTask(taskId: Int) = viewModelScope.launch {
        taskRepository.get(taskId).also {
            _uiState.value = UIState.Loading
            _task = it
            _uiState.value = UIState.Loaded(it)
        }
    }

    fun onTitleChanged(title: String) {
        _task = _task.copy(title = title)
        validateTitle(title)
    }

    private fun validateTitle(title: String): Boolean {
        return if (title.isBlank()) {
            _uiState.value = UIState.Error("The title is required", TaskField.TITLE)
            false
        } else {
            _uiState.value = UIState.SetFieldData(title, TaskField.TITLE)
            true
        }
    }

    fun onDescriptionChanged(description: String?) {
        _task = _task.copy(description = description)
        _uiState.value = UIState.SetFieldData(description, TaskField.DESCRIPTION)
    }

    fun onDueDateChanged(dueDate: LocalDateTime?) {
        _task = _task.copy(dueDate = dueDate)
        validateDueDate(dueDate)
    }

    private fun validateDueDate(dueDate: LocalDateTime?): Boolean {
        var errorMsg: String? = null
        dueDate?.let {
            if (it.toLocalDate() <= LocalDate.now()) {
                errorMsg = "The Due Date must be greater than the current date."
            }
        }
        return if (errorMsg == null) {
            _uiState.value = UIState.SetFieldData(dueDate, TaskField.DUE_DATE)
            true
        } else {
            _uiState.value = UIState.Error(errorMsg!!, TaskField.DUE_DATE)
            false
        }
    }

    fun onSubmitTask() = viewModelScope.launch {
        _uiState.value = UIState.Loading
        if (!validateTitle(_task.title) || !validateDueDate(_task.dueDate)) {
            return@launch
        }
        if (_task.id == 0) {
            taskRepository.save(_task)
        } else {
            taskRepository.update(_task)
        }
        _uiState.value = UIState.Finished
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
}
