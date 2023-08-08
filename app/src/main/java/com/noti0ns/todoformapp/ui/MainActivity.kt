package com.noti0ns.todoformapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.noti0ns.todoformapp.adapters.TaskAdapter
import com.noti0ns.todoformapp.databinding.ActivityMainBinding
import com.noti0ns.todoformapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), TaskAdapter.TaskClickEvent {
    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskAdapter: TaskAdapter

    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.i("MainActivity.getContent", it.resultCode.toString())
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
    }

    override fun onCheckboxChanged(position: Int) {
        viewModel.toggleTaskState(position)
    }

    override fun onClickItem(position: Int) {
        Snackbar.make(binding.root, "Item ${position + 1} clicked!", Snackbar.LENGTH_LONG).apply {
            dismiss()
            show()
        }
    }
}