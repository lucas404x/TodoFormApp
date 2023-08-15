package com.noti0ns.todoformapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noti0ns.todoformapp.data.models.Task
import com.noti0ns.todoformapp.data.repositories.RoomTaskRepository
import com.noti0ns.todoformapp.interfaces.TaskRepository
import java.time.Instant

class TaskViewModel : ViewModel() {
    companion object {
        private val taskRepo: TaskRepository

        init {
            taskRepo = RoomTaskRepository()
        }
    }


    private val _titleState = MutableLiveData<FieldFormState<String>>(FieldFormState())
    val titleState: LiveData<FieldFormState<String>> = _titleState

    private val _descriptionState = MutableLiveData<FieldFormState<String>>(FieldFormState())
    val descriptionState: LiveData<FieldFormState<String>> = _descriptionState

    private val _dueDateState = MutableLiveData<FieldFormState<Instant>>(FieldFormState())
    val dueDateState: LiveData<FieldFormState<Instant>> = _dueDateState

    private var _task = Task()

    fun onLoadTask(task: Task) = task.apply {
        _titleState.value = FieldFormState(task.title)
        _descriptionState.value = FieldFormState(task.description)
        _dueDateState.value = FieldFormState(task.dueDate)
    }.also {
        _task = it
    }


    fun onInvokeEvent(event: TaskChangeEvent) = when (event) {
        is TaskChangeEvent.TitleChanged -> onTitleChanged(event.title)
        is TaskChangeEvent.DescriptionChanged -> onDescriptionChanged(event.description)
        is TaskChangeEvent.DueDateChanged -> onDueDateChanged(event.dueDate)
        TaskChangeEvent.SubmitTask -> onSubmitTask()
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

    private fun onDueDateChanged(dueDate: Instant?) {
        _task = _task.copy(dueDate = dueDate)
        _dueDateState.value = _dueDateState.value?.copy(dueDate)
    }

    private fun onSubmitTask() {

    }
}

data class FieldFormState<T>(val data: T?, val error: String?) {
    constructor() : this(null, null)
    constructor(data: T?) : this(data, null)
}

sealed class TaskChangeEvent {
    data class TitleChanged(val title: String) : TaskChangeEvent()
    data class DescriptionChanged(val description: String) : TaskChangeEvent()
    data class DueDateChanged(val dueDate: Instant?) : TaskChangeEvent()
    object SubmitTask : TaskChangeEvent()
}