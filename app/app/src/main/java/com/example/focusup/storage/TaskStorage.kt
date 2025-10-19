package com.example.focusup.storage

import android.content.Context
import com.example.focusup.model.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*

object TaskStorage {
    private const val FILENAME = "tasks.json"
    private val gson = Gson()

    fun saveTasks(context: Context, tasks: List<Task>) {
        try {
            val file = File(context.filesDir, FILENAME)
            val jsonString = gson.toJson(tasks)
            file.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadTasks(context: Context): List<Task> {
        return try {
            val file = File(context.filesDir, FILENAME)
            if (!file.exists()) {
                return emptyList()
            }
            val jsonString = file.readText()
            val type = object : TypeToken<List<Task>>() {}.type
            gson.fromJson(jsonString, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Método para agregar una tarea
    fun addTask(context: Context, task: Task) {
        val currentTasks = loadTasks(context).toMutableList()
        currentTasks.add(task)
        saveTasks(context, currentTasks)
    }

    // Método para eliminar una tarea por ID
    fun removeTaskById(context: Context, taskId: Int) {
        val currentTasks = loadTasks(context).toMutableList()
        currentTasks.removeAll { it.id == taskId }
        saveTasks(context, currentTasks)
    }

    // Método para obtener el próximo ID disponible
    fun getNextId(context: Context): Int {
        val currentTasks = loadTasks(context)
        return if (currentTasks.isEmpty()) 1
        else currentTasks.maxOf { it.id } + 1
    }
}