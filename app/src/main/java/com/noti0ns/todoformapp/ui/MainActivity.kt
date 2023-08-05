package com.noti0ns.todoformapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.noti0ns.todoformapp.R
import com.noti0ns.todoformapp.adapters.TaskAdapter
import com.noti0ns.todoformapp.data.models.Task
import com.noti0ns.todoformapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.i("MainActivity.getContent", it.resultCode.toString())
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.taskList.apply {
            val tasks = listOf(
                Task(1, title = "Task1"),
                Task(2, title = "Task2"),
                Task(3, title = "Task3"),
                Task(4, title = "Task4")
            )
            adapter = TaskAdapter(tasks)
            layoutManager = LinearLayoutManager(context)
        }
        binding.newTaskBtn.setOnClickListener {
            getContent.launch(Intent(this, TaskActivity::class.java))
        }
    }
}