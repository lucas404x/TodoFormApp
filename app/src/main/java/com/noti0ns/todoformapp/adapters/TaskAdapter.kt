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
import com.noti0ns.todoformapp.extensions.renderShortDate

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
        holder.binding.txtFirstTaskDate.apply {
            if (task.dueDate == null) {
                visibility = View.GONE
            } else {
                text = task.dueDate.toString()
                visibility = View.VISIBLE
            }
        }
        holder.binding.btnIsTaskDone.apply {
            resetTileState(holder)
            isChecked = task.isDone
            var isFirstDateSet = false
            if (task.dueDate != null) {
                holder.binding.txtFirstTaskDate.apply {
                    text = task.dueDate.renderShortDate()
                    visibility = View.VISIBLE
                    isFirstDateSet = true
                }
            }
            if (task.isDone) {
                if (isFirstDateSet) {
                    holder.binding.txtSepBetweenTexts.visibility = View.VISIBLE
                    holder.binding.txtSecondTaskDate.text = task.dateFinished.renderShortDate()
                    holder.binding.txtSecondTaskDate.visibility = View.VISIBLE
                } else {
                    holder.binding.txtSecondTaskDate.text = task.dateFinished.renderShortDate()
                    holder.binding.txtSecondTaskDate.visibility = View.VISIBLE
                }
            }
            setOnClickListener {
                Log.i(
                    "TaskAdapter.onBindViewHolder.Task" + task.id.toString() + ": ",
                    task.isDone.toString()
                )
                events.onCheckboxChanged(position)
            }
        }
    }

    private fun resetTileState(holder: ViewHolder) {
        holder.binding.txtFirstTaskDate.visibility = View.INVISIBLE
        holder.binding.txtSecondTaskDate.visibility = View.INVISIBLE
        holder.binding.txtSepBetweenTexts.visibility = View.INVISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(tasks: MutableList<Task>) {
        Log.i("setList", "SetList called")
        dataset = tasks
        notifyDataSetChanged()
    }

    fun setItemChanged(task: Task, position: Int) {
        dataset[position] = task
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int = dataset.size

    interface TaskClickEvent {
        fun onCheckboxChanged(position: Int)
        fun onClickItem(task: Task)
    }
}