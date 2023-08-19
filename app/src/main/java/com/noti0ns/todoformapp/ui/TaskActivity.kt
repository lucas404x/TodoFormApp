package com.noti0ns.todoformapp.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.noti0ns.todoformapp.databinding.ActivityTaskBinding
import com.noti0ns.todoformapp.extensions.renderFullDateTime
import com.noti0ns.todoformapp.extensions.reset
import com.noti0ns.todoformapp.viewmodel.TaskUiState
import com.noti0ns.todoformapp.viewmodel.TaskViewModel
import com.noti0ns.todoformapp.viewmodel.TaskViewModelEvent
import java.time.LocalDateTime
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
                _viewModel.onInvokeEvent(TaskViewModelEvent.TitleChanged(text.toString()))
            }
            inputDescriptionTaskField.doOnTextChanged { text, _, _, _ ->
                _viewModel.onInvokeEvent(TaskViewModelEvent.DescriptionChanged(text.toString()))
            }
            inpDueDateTaskField.doOnTextChanged { text, _, _, _ ->
                _binding.btnClearDueDate.visibility =
                    if (text.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            }
            inpDueDateTaskField.setOnClickListener {
                showDueDateDatePicker()
            }
            btnClearDueDate.setOnClickListener {
                _viewModel.onInvokeEvent(TaskViewModelEvent.DueDateChanged(null))
            }
            btnSaveTaskChanges.setOnClickListener {
                _viewModel.onInvokeEvent(TaskViewModelEvent.SubmitTask)
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
            _calendar.get(Calendar.DAY_OF_MONTH),
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
        val selectedDueDate = LocalDateTime.ofInstant(
            toInstant(),
            timeZone.toZoneId()
        )
        _viewModel.onInvokeEvent(TaskViewModelEvent.DueDateChanged(selectedDueDate))
        reset()
    }

    private fun setupListeners() {
        _viewModel.uiState.observe(this) {
            when (it) {
                TaskUiState.INITIAL -> {}
                TaskUiState.LOADING -> {
                    _binding.progressBarTaskState.visibility = View.VISIBLE
                    _binding.btnSaveTaskChanges.isEnabled = false
                }

                TaskUiState.LOADED -> {
                    _binding.progressBarTaskState.visibility = View.GONE
                    _binding.btnSaveTaskChanges.isEnabled = true
                }

                TaskUiState.FINISHED -> {
                    setResult(MainActivity.REFRESH_SCREEN_CODE)
                    finish()
                }
            }
        }
        _viewModel.taskState.observe(this) {
            if (!it.second) return@observe;
            val task = it.first
            _binding.inpTitleTaskField.setText(task.title)
            _binding.inputDescriptionTaskField.setText(task.description)
            _binding.inpDueDateTaskField.setText(task.dueDate.renderFullDateTime())
        }
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
                setText(it.data.renderFullDateTime())
                if (it.error.orEmpty().isNotBlank()) {
                    error = it.error
                }
            }
        }
    }

    private fun loadTask() = intent.getIntExtra(MainActivity.TASK_ID_KEY, 0).also {
        if (it > 0) {
            _viewModel.onLoadTask(it)
        }
    }
}