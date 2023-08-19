package com.noti0ns.todoformapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.noti0ns.todoformapp.adapters.TaskAdapter
import com.noti0ns.todoformapp.data.models.Task
import com.noti0ns.todoformapp.databinding.ActivityMainBinding
import com.noti0ns.todoformapp.viewmodel.MainViewModel

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

        viewModel.tasks.observe(this) {
            taskAdapter.setList(it)
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
}