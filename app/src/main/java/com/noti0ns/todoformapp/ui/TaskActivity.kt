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
        setupBindings()
        setupListeners()
        loadTask()
        setContentView(_binding.root)
    }

    private fun setupBindings() {
        _binding.apply {
            inpTitleTaskField.doOnTextChanged { text, _, _, _ ->
                title = text.toString().ifBlank { "Untitled" }
            }
        }
    }

    private fun setupListeners() {
        _viewModel.titleState.observe(this) { error ->
            _binding.inpTitleTaskField.error = error
        }
        _viewModel.descriptionState.observe(this) { error ->
            _binding.inputDescriptionTaskField.error = error
        }
        _viewModel.dueDateState.observe(this) { error ->
            _binding.inpDueDateTaskField.error = error
        }
    }

    private fun loadTask() {
        val task = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_TASK", Task::class.java)
        } else {
            intent.getParcelableExtra("EXTRA_TASK")
        }
        task?.let {
            _viewModel.onLoadTask(it)
            setInitialTaskData(it)
        }
        title = task?.title.orEmpty().ifEmpty { "Untitled" }
    }

    private fun setInitialTaskData(task: Task) {
        _binding.inpTitleTaskField.setText(task.title)
        _binding.inputDescriptionTaskField.setText(task.description)
        _binding.inpDueDateTaskField.setText(task.dueDate.toString())
    }
}