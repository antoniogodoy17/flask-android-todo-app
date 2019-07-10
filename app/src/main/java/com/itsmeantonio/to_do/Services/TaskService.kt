package com.itsmeantonio.to_do.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.itsmeantonio.to_do.Controller.App
import com.itsmeantonio.to_do.Model.Task
import com.itsmeantonio.to_do.Utilities.URL_CREATE_TASK
import com.itsmeantonio.to_do.Utilities.URL_DELETE_TASK
import com.itsmeantonio.to_do.Utilities.URL_GET_TASKS
import com.itsmeantonio.to_do.Utilities.URL_UPDATE_TASK
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object TaskService {

    val tasks = ArrayList<Task>()

    fun clearTasks() {
        tasks.clear()
    }

    fun getTasks(complete: (Boolean) -> Unit) {

        val getTasksRequest = object : JsonObjectRequest(Method.GET, URL_GET_TASKS, null, Response.Listener { res ->
            clearTasks()
            try {
                if (res.getInt("errorCode") == 0) {
                    val tasks = res.getJSONArray("tasks")

                    for (index in 0 until tasks.length()) {
                        val task = tasks.getJSONObject(index)
                        val id = task.getString("_id")
                        val title = task.getString("title")
                        val content = task.getString("content")
                        val dueDate = task.getString("due_date")
                        val createdDate = task.getString("created_date")

                        val newTask = Task(id, title, content, createdDate, dueDate)
                        this.tasks.add(newTask)
                    }

                    complete(true)
                } else {
                    complete(false)
                }
            } catch(e: JSONException) {
                Log.d("JSON", "EXC: ${e.localizedMessage}")
                complete(false)
            }

        }, Response.ErrorListener { err ->
            Log.d("ERR", "Could not fetch tasks: $err" )
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(getTasksRequest)
    }

    fun createTask(title: String, content: String, dueDate: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("title", title)
        jsonBody.put("content", content)
        jsonBody.put("due_date", dueDate)
        val requestBody = jsonBody.toString()

        val createTaskRequest = object : JsonObjectRequest(Method.POST, URL_CREATE_TASK, null, Response.Listener { res ->

            try {
                if (res.getInt("errorCode") == 0) {
                    complete(true)
                } else {
                    complete(false)
                }
            } catch (e: JSONException) {
                Log.d("JSON","EXC: ${e.localizedMessage}")
                complete(false)
            }
        }, Response.ErrorListener { err ->
            Log.d("Error", "Could not create task: $err")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(createTaskRequest)
    }

    fun updateTask(title: String, content: String, dueDate: String, createdDate: String, id: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("title", title)
        jsonBody.put("content", content)
        jsonBody.put("created_date", createdDate)
        jsonBody.put("due_date", dueDate)
        val requestBody = jsonBody.toString()

        val url = "$URL_UPDATE_TASK$id"

        val updateTaskRequest = object : JsonObjectRequest(Method.PUT, url, null, Response.Listener { res ->

            try {
                if (res.getInt("errorCode") == 0) {
                    complete(true)
                }
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: ${e.localizedMessage}")
                complete(false)
            }

        }, Response.ErrorListener { err ->
            Log.d("ERROR", "Could not update task: $err")
            complete(false)
        }) {
            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }

        App.prefs.requestQueue.add(updateTaskRequest)
    }

    fun deleteTask(id: String, complete: (Boolean) -> Unit) {

        val url = "$URL_DELETE_TASK$id"

        val deleteTaskRequest = object : JsonObjectRequest(Method.DELETE, url, null, Response.Listener { res ->

            try {
                if (res.getInt("errorCode") == 0) {
                    complete(true)
                } else {
                    complete(false)
                }
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: ${e.localizedMessage}")
                complete(false)
            }

        }, Response.ErrorListener { err ->
            Log.d("ERROR", "Could not delete task: $err")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(deleteTaskRequest)
    }
}