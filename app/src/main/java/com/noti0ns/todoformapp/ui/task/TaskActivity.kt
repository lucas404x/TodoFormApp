package com.noti0ns.todoformapp.ui.task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import com.noti0ns.todoformapp.databinding.ActivityTaskBinding
import com.noti0ns.todoformapp.extensions.renderFullDateTime
import com.noti0ns.todoformapp.extensions.reset
import com.noti0ns.todoformapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.util.Calendar

@AndroidEntryPoint
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
                _viewModel.onInvokeEvent(TaskViewModel.TaskViewModelEvent.TitleChanged(text.toString()))
            }
            inputDescriptionTaskField.doOnTextChanged { text, _, _, _ ->
                _viewModel.onInvokeEvent(TaskViewModel.TaskViewModelEvent.DescriptionChanged(text.toString()))
            }
            inpDueDateTaskField.doOnTextChanged { text, _, _, _ ->
                _binding.btnClearDueDate.visibility =
                    if (text.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            }
            inpDueDateTaskField.setOnClickListener {
                showDueDateDatePicker()
            }
            btnClearDueDate.setOnClickListener {
                _viewModel.onInvokeEvent(TaskViewModel.TaskViewModelEvent.DueDateChanged(null))
            }
            btnSaveTaskChanges.setOnClickListener {
                _viewModel.onInvokeEvent(TaskViewModel.TaskViewModelEvent.SubmitTask)
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
        _viewModel.onInvokeEvent(TaskViewModel.TaskViewModelEvent.DueDateChanged(selectedDueDate))
        reset()
    }

    private fun setupListeners() {
        _viewModel.uiState.observe(this) {
            when (it) {
                TaskViewModel.UIState.Initial -> {}
                TaskViewModel.UIState.Loading -> {
                    _binding.progressBarTaskState.visibility = View.VISIBLE
                    _binding.btnSaveTaskChanges.isEnabled = false
                }

                is TaskViewModel.UIState.Loaded -> {
                    _binding.progressBarTaskState.visibility = View.GONE
                    _binding.btnSaveTaskChanges.isEnabled = true
                    _binding.inpTitleTaskField.setText(it.task.title)
                    _binding.inputDescriptionTaskField.setText(it.task.description)
                    _binding.inpDueDateTaskField.setText(it.task.dueDate.renderFullDateTime())
                    title = it.task.title.ifBlank { "Untitled" }
                }

                is TaskViewModel.UIState.SetFieldData<*> -> handleSetFieldData(it)
                is TaskViewModel.UIState.Error -> handleFieldError(it)
                TaskViewModel.UIState.Finished -> {
                    setResult(MainActivity.REFRESH_SCREEN_CODE)
                    finish()
                }
            }
        }
    }

    private fun handleSetFieldData(setFieldData: TaskViewModel.UIState.SetFieldData<*>) {
        when (setFieldData.field) {
            TaskViewModel.TaskField.TITLE -> {
                if (setFieldData.data is String?) {
                    title = setFieldData.data.orEmpty().ifBlank { "Untitled" }
                }
            }
            TaskViewModel.TaskField.DESCRIPTION -> {}
            TaskViewModel.TaskField.DUE_DATE -> {
                if (setFieldData.data is LocalDateTime?) {
                    _binding.inpDueDateTaskField.setText((setFieldData.data.renderFullDateTime()))
                    _binding.inpDueDateTaskField.error = null
                }
            }
        }
    }

    private fun handleFieldError(error: TaskViewModel.UIState.Error) = when (error.field) {
        TaskViewModel.TaskField.TITLE -> _binding.inpTitleTaskField.error = error.message
        TaskViewModel.TaskField.DESCRIPTION -> _binding.inputDescriptionTaskField.error = error.message
        TaskViewModel.TaskField.DUE_DATE -> {
            _binding.inpDueDateTaskField.error = error.message
            Snackbar.make(_binding.root, error.message, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun loadTask() = intent.getIntExtra(MainActivity.TASK_ID_KEY, 0).also {
        if (it > 0) {
            _viewModel.onLoadTask(it)
        }
    }
}