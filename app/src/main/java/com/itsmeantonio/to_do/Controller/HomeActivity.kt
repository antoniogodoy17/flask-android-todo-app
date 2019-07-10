package com.itsmeantonio.to_do.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.itsmeantonio.to_do.Adapter.TaskAdapter
import com.itsmeantonio.to_do.Model.Task
import com.itsmeantonio.to_do.R
import com.itsmeantonio.to_do.Services.AuthService
import com.itsmeantonio.to_do.Services.TaskService
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    var currentTask : Task? = null
    lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (!App.prefs.isLoggedIn) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }

        setupAdapter()
        getTasks()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bar_menu_layout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()

        if (id == R.id.action_logout) {
            AuthService.logout()
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    fun setupAdapter() {
        taskAdapter = TaskAdapter(this, TaskService.tasks) { selectedTask, action ->
            when (action) {
                "delete" -> onDeleteTask(selectedTask)
                "edit" -> onEditTask(selectedTask)
            }
        }
        tasksRecyclerView.adapter = taskAdapter
        val layoutManager = LinearLayoutManager(this)
        tasksRecyclerView.layoutManager = layoutManager
    }

    fun onSubmitTask(view: View) {
        val title = txtTaskTitle.text.toString()
        val content = txtTaskContent.text.toString()
        val dueDate = txtTaskDate.text.toString()
        if (currentTask == null) {
            // Create task
            TaskService.createTask(title, content, dueDate) { success ->
                if (success) {
                    clearForm()
                    getTasks()
                }
            }
        } else {
            // Update task
            val createdDate = currentTask!!.createdDate
            val id = currentTask!!.id

            TaskService.updateTask(title, content, dueDate, createdDate, id) { success ->
                if (success) {
                    clearForm()
                    getTasks()
                }
            }
        }
    }

    fun onEditTask(task: Task) {
        currentTask = task
        txtTaskTitle.setText(task.title)
        txtTaskContent.setText(task.content)
        txtTaskDate.setText(task.dueDate)
        btnSubmit.text = "Update"
    }

    fun onDeleteTask(task: Task) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.delete_task_dialog, null)

        builder.setView(dialogView)
            .setPositiveButton("Yes") { _, _ ->
                TaskService.deleteTask(task.id) { success ->
                    if (success) {
                        getTasks()
                    }
                }
            }
            .setNegativeButton("No") { _, _ -> }
            .show()
    }

    fun getTasks() {
        TaskService.getTasks { success ->
            if (success) {
                taskAdapter.notifyDataSetChanged()
            }
        }
    }

    fun clearForm() {
        txtTaskTitle.text.clear()
        txtTaskContent.text.clear()
        txtTaskDate.text.clear()
        currentTask = null
        btnSubmit.text = "Create"
    }
}
