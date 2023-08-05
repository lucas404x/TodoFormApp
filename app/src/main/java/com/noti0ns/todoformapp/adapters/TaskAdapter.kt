package com.noti0ns.todoformapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.noti0ns.todoformapp.data.models.Task

class TaskAdapter(private val dataset: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(android.R.id.text1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i("TaskAdapter.onBindViewHolder", dataset[position].title)
        holder.textView.text = dataset[position].title
    }

    override fun getItemCount(): Int = dataset.size
}