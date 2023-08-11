package com.noti0ns.todoformapp.ui

import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.noti0ns.todoformapp.R
import com.noti0ns.todoformapp.data.models.Task
import com.noti0ns.todoformapp.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTaskBinding.inflate(layoutInflater)
        loadTask()
        setContentView(R.layout.activity_task)
    }

    private fun loadTask() {
        val task = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_TASK", Task::class.java)
        } else {
            intent.getParcelableExtra("EXTRA_TASK")
        }
        title = task?.title ?: "Untitled"
    }
}