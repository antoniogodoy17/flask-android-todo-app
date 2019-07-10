package com.itsmeantonio.to_do.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itsmeantonio.to_do.Model.Task
import com.itsmeantonio.to_do.R
import com.itsmeantonio.to_do.Services.TaskService

class TaskAdapter(val context: Context, val tasks: ArrayList<Task>, val itemClick: (Task, action: String) -> Unit) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false)

        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return tasks.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTask(context, tasks[position])
    }

    inner class ViewHolder(itemView: View, val itemClick: (Task, action: String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.txtTaskItemTitle)
        val content = itemView.findViewById<TextView>(R.id.txtTaskItemContent)
        val dueDate = itemView.findViewById<TextView>(R.id.txtTaskItemDueDate)
        val btnDelete = itemView.findViewById<ImageButton>(R.id.btnItemDelete)
        val btnEdit = itemView.findViewById<ImageButton>(R.id.btnItemEdit)

        fun bindTask(context: Context, task: Task) {
            title.text = task.title
            content.text = task.content
            dueDate.text = task.dueDate

            btnDelete.setOnClickListener { itemClick(task, "delete") }
            btnEdit.setOnClickListener { itemClick(task, "edit") }
        }

    }
}