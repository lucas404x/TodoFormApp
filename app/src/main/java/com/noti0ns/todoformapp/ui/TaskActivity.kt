package com.noti0ns.todoformapp.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.noti0ns.todoformapp.data.models.Task
import com.noti0ns.todoformapp.databinding.ActivityTaskBinding
import com.noti0ns.todoformapp.viewmodel.TaskChangeEvent
import com.noti0ns.todoformapp.viewmodel.TaskViewModel
import java.util.Calendar

class TaskActivity : AppCompatActivity() {
    private val _viewModel: TaskViewModel by viewModels()
    private lateinit var _binding: ActivityTaskBinding

    private val _calendar: Calendar = Calendar.getInstance()

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
                _viewModel.onInvokeEvent(TaskChangeEvent.TitleChanged(text.toString()))
            }
            inputDescriptionTaskField.doOnTextChanged { text, _, _, _ ->
                _viewModel.onInvokeEvent(TaskChangeEvent.DescriptionChanged(text.toString()))
            }
            inpDueDateTaskField.setOnClickListener {
                showDueDateDatePicker()
            }
        }
    }

    private fun showDueDateDatePicker() {
        val datePickerListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            _calendar.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }.also {
                showDueDateTimerPicker()
            }
        }
        DatePickerDialog(
            this@TaskActivity,
            datePickerListener,
            _calendar.get(Calendar.YEAR),
            _calendar.get(Calendar.MONTH),
            _calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showDueDateTimerPicker() {
        val timePickerListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            _calendar.apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
            setDueDate()
        }
        TimePickerDialog(
            this@TaskActivity,
            timePickerListener,
            _calendar.get(Calendar.HOUR_OF_DAY),
            _calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun setDueDate() = _calendar.apply {
        _viewModel.onInvokeEvent(TaskChangeEvent.DueDateChanged(toInstant()))
        clear()
    }

    private fun setupListeners() {
        _viewModel.titleState.observe(this) {
            title = it.data.orEmpty().ifBlank { "Untitled" }
            if (it.error.orEmpty().isNotBlank()) {
                _binding.inpTitleTaskField.error = it.error
            }
        }
        _viewModel.descriptionState.observe(this) {
            if (it.error.orEmpty().isNotBlank()) {
                _binding.inputDescriptionTaskField.error = it.error
            }
        }
        _viewModel.dueDateState.observe(this) {
            _binding.inpDueDateTaskField.apply {
                setText(if (it.data == null) "" else it.data.toString())
                if (it.error.orEmpty().isNotBlank()) {
                    error = it.error
                }
            }
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
    }

    private fun setInitialTaskData(task: Task) {
        _binding.inpTitleTaskField.setText(task.title)
        _binding.inputDescriptionTaskField.setText(task.description)
        _binding.inpDueDateTaskField.setText(task.dueDate.toString())
    }
}