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


    private val _titleState = MutableLiveData<String?>(null)
    val titleState: LiveData<String?> = _titleState

    private val _descriptionState = MutableLiveData<String?>(null)
    val descriptionState: LiveData<String?> = _descriptionState

    private val _dueDateState = MutableLiveData<String?>(null)
    val dueDateState: LiveData<String?> = _dueDateState

    private var _task = Task()

    fun onLoadTask(task: Task) = task.apply {
        onTitleChanged(title)
        onDescriptionChanged(description)
        onDueDateChanged(dueDate)
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
        _titleState.value = if (title.isBlank()) "The title is required" else null
    }

    private fun onDescriptionChanged(description: String?) {
        _task = _task.copy(description = description)
    }

    private fun onDueDateChanged(dueDate: Instant?) {
        _task = _task.copy(dueDate = dueDate)
    }

    private fun onSubmitTask() {

    }
}

sealed class TaskChangeEvent {
    data class TitleChanged(val title: String) : TaskChangeEvent()
    data class DescriptionChanged(val description: String) : TaskChangeEvent()
    data class DueDateChanged(val dueDate: Instant?) : TaskChangeEvent()
    object SubmitTask : TaskChangeEvent()
}