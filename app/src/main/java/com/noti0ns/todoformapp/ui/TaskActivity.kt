package com.noti0ns.todoformapp.ui

import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.noti0ns.todoformapp.R
import com.noti0ns.todoformapp.data.models.Task
import com.noti0ns.todoformapp.databinding.ActivityTaskBinding
import com.noti0ns.todoformapp.viewmodel.TaskViewModel

class TaskActivity : AppCompatActivity() {
    private val _viewModel: TaskViewModel by viewModels()
    private lateinit var _binding: ActivityTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTaskBinding.inflate(layoutInflater)
        loadTask()

        _binding.apply {
            inpTitleTaskField.doOnTextChanged { text, _, _, _ -> onTitleChanged(text.toString()) }
        }

        _viewModel.task.observe(this) {
            onTitleChanged(it.title)
        }

        setContentView(R.layout.activity_task)
    }

    private fun onTitleChanged(title: String) {
        this.title = title.ifBlank { "Untitled" }
    }

    private fun loadTask() {
        val task = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_TASK", Task::class.java)
        } else {
            intent.getParcelableExtra("EXTRA_TASK")
        }
        task?.let { _viewModel.loadTask(it) }
    }
}