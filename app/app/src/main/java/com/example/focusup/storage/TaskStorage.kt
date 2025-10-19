package com.example.focusup.storage

import android.content.Context
import com.example.focusup.model.Task
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.*
import java.time.LocalDate
import java.time.LocalTime

object TaskStorage {
    private const val FILENAME = "tasks.json"
    
    // Creando Gson con los adaptadores personalizados
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
        .create()

    fun saveTasks(context: Context, tasks: List<Task>) {
        try {
            val file = File(context.filesDir, FILENAME)
            val jsonString = gson.toJson(tasks)
            file.writeText(jsonString)
            println("Tareas guardadas: ${tasks.size} tareas")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error al guardar tareas: ${e.message}")
        }
    }

    fun loadTasks(context: Context): List<Task> {
        return try {
            val file = File(context.filesDir, FILENAME)
            if (!file.exists()) {
                println("Archivo de tareas no existe")
                return emptyList()
            }
            val jsonString = file.readText()
            val type = object : TypeToken<List<Task>>() {}.type
            val tasks = gson.fromJson<List<Task>>(jsonString, type) ?: emptyList()
            println("Tareas cargadas: ${tasks.size} tareas")
            tasks
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error al cargar tareas: ${e.message}")
            emptyList()
        }
    }

    fun addTask(context: Context, task: Task) {
        val currentTasks = loadTasks(context).toMutableList()
        currentTasks.add(task)
        saveTasks(context, currentTasks)
    }

    fun removeTaskById(context: Context, taskId: Int) {
        val currentTasks = loadTasks(context).toMutableList()
        currentTasks.removeAll { it.id == taskId }
        saveTasks(context, currentTasks)
    }

    fun getNextId(context: Context): Int {
        val currentTasks = loadTasks(context)
        return if (currentTasks.isEmpty()) 1
        else currentTasks.maxOf { it.id } + 1
    }
}