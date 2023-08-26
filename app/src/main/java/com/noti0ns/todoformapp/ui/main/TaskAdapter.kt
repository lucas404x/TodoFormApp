package com.noti0ns.todoformapp.ui.main

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
import java.time.LocalDate

class TaskAdapter(private val events: TaskClickEvent) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    private var dataset: MutableList<Task> = mutableListOf()

    private var _textViewDefaultColorCached: Int? = null

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
        resetTileState(holder)
        val task = dataset[position]
        holder.binding.constraintLayout.apply {
            setOnClickListener { events.onClickItem(task.id) }
            setOnLongClickListener {
                events.onDeleteItem(position, task)
                true
            }
        }
        holder.binding.txtTaskTitle.text = task.title
        holder.binding.btnIsTaskDone.apply {
            setOnClickListener {
                Log.d(
                    "TaskAdapter.onBindViewHolder.Task" + task.id.toString() + ": ",
                    task.isDone.toString()
                )
                events.onCheckboxChanged(position)
            }
            isChecked = task.isDone
            var isFirstDateSet = false
            task.dueDate?.let {
                holder.binding.txtFirstTaskDate.apply {
                    isFirstDateSet = true
                    text = it.renderShortDate()
                    visibility = View.VISIBLE
                    if (it.toLocalDate() <= LocalDate.now() && !task.isDone)
                        setTextColor(
                            resources.getColor(
                                com.google.android.material.R.color.design_default_color_error,
                                context.theme
                            )
                        )
                }
            }
            if (task.isDone) {
                if (isFirstDateSet) {
                    holder.binding.txtSepBetweenTexts.visibility = View.VISIBLE
                    holder.binding.txtSecondTaskDate.text = task.dateFinished.renderShortDate()
                    holder.binding.txtSecondTaskDate.visibility = View.VISIBLE
                } else {
                    holder.binding.txtFirstTaskDate.text = task.dateFinished.renderShortDate()
                    holder.binding.txtFirstTaskDate.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun resetTileState(holder: ViewHolder) {
        holder.binding.txtFirstTaskDate.apply {
            _textViewDefaultColorCached = _textViewDefaultColorCached ?: textColors.defaultColor
            setTextColor(_textViewDefaultColorCached!!)
            text = null
            visibility = View.VISIBLE
        }
        holder.binding.txtSepBetweenTexts.visibility = View.INVISIBLE
        holder.binding.txtSecondTaskDate.apply {
            text = null
            visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(tasks: MutableList<Task>) {
        Log.d("setList", "SetList called")
        dataset = tasks
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dataset.size

    interface TaskClickEvent {
        fun onCheckboxChanged(position: Int)
        fun onClickItem(taskId: Int)
        fun onDeleteItem(position: Int, task: Task)
    }
}