package com.noti0ns.todoformapp.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.noti0ns.todoformapp.R
import com.noti0ns.todoformapp.data.models.Task
import com.noti0ns.todoformapp.databinding.ActivityMainBinding
import com.noti0ns.todoformapp.ui.task.TaskActivity

class MainActivity : AppCompatActivity(), TaskAdapter.TaskClickEvent {
    companion object {
        const val TASK_ID_KEY = "EXTRA_TASK_ID"
        const val REFRESH_SCREEN_CODE = 0x810
    }

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskAdapter: TaskAdapter

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == REFRESH_SCREEN_CODE) {
                viewModel.loadTasks()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskAdapter = TaskAdapter(this)

        binding.taskList.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.newTaskBtn.setOnClickListener {
            getContent.launch(Intent(this, TaskActivity::class.java))
        }

        viewModel.uiState.observe(this) {
            when (it) {
                is MainViewModel.UIState.Initial -> {}
                is MainViewModel.UIState.Loaded -> {
                    taskAdapter.setList(it.tasks)
                    binding.txtListHasNoItems.visibility = if (it.tasks.isEmpty()) View.VISIBLE else View.GONE
                    binding.taskList.visibility = if (it.tasks.isNotEmpty()) View.VISIBLE else View.GONE
                }
                is MainViewModel.UIState.TaskRemoved -> {
                    taskAdapter.notifyItemRemoved(it.taskPosition)
                }
                is MainViewModel.UIState.TaskUpdated -> {
                    taskAdapter.notifyItemChanged(it.taskPosition)
                }
            }
        }

        viewModel.loadTasks()
    }

    override fun onCheckboxChanged(position: Int) {
        viewModel.toggleTaskState(position)
    }

    override fun onClickItem(task: Task) {
        Intent(this, TaskActivity::class.java).also {
            it.putExtra(TASK_ID_KEY, task.id)
            getContent.launch(it)
        }
    }

    override fun onDeleteItem(position: Int, task: Task) {
        AlertDialog.Builder(this)
            .setTitle("Remove task")
            .setMessage(getString(R.string.txt_remove_task_dialog_body, task.title))
            .setNegativeButton(getString(R.string.txt_no)) { _, _ -> }
            .setPositiveButton(getString(R.string.txt_yes)) { _, _ -> viewModel.deleteTask(position) }
            .create()
            .show()
    }
}