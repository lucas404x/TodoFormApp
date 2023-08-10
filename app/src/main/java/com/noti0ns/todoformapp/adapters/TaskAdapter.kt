package com.noti0ns.todoformapp.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.noti0ns.todoformapp.R
import com.noti0ns.todoformapp.data.models.Task
import com.noti0ns.todoformapp.databinding.ItemTaskBinding

class TaskAdapter(private val events: TaskClickEvent) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    private var dataset: MutableList<Task> = mutableListOf()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemTaskBinding

        init {
            binding = ItemTaskBinding.bind(view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = dataset[position]
        holder.binding.constraintLayout.setOnClickListener {
            events.onClickItem(task)
        }
        holder.binding.txtTaskTitle.text = task.title
        holder.binding.txtFinishTaskDate.apply {
            if (task.dateToFinish == null) {
                visibility = View.GONE
            } else {
                text = task.dateToFinish.toString()
                visibility = View.VISIBLE
            }
        }
        holder.binding.btnIsTaskDone.apply {
            isChecked = task.isDone
            setOnClickListener {
                Log.i(
                    "TaskAdapter.onBindViewHolder.Task" + task.id.toString() + ": ",
                    task.isDone.toString()
                )
                events.onCheckboxChanged(position)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(tasks: List<Task>) {
        Log.i("setList", "SetList called")
        dataset = tasks.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dataset.size

    interface TaskClickEvent {
        fun onCheckboxChanged(position: Int)
        fun onClickItem(task: Task)
    }
}