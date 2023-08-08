package com.noti0ns.todoformapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noti0ns.todoformapp.data.models.Task
import java.time.Instant

class MainViewModel : ViewModel() {
    private val _tasks: List<Task> = listOf(
        Task(1, title = "Task1"),
        Task(2, title = "Task2"),
        Task(3, title = "Task3"),
        Task(4, title = "Task4", dateToFinish = Instant.now())
    )

    val tasks: LiveData<List<Task>> by lazy {
        MutableLiveData(_tasks)
    }

    fun toggleTaskState(taskPos: Int) {
        _tasks[taskPos].isDone = !_tasks[taskPos].isDone
    }
}