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

    // Guardar lista completa de tareas
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

    // Cargar lista completa de tareas
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

    // Agregar nueva tarea con límite según tipo de usuario
    fun addTask(context: Context, task: Task, isPremium: Boolean) {
        val currentTasks = loadTasks(context).toMutableList()

        // Límite de tareas según suscripción
        val maxTasks = if (isPremium) 50 else 10

        if (currentTasks.size >= maxTasks) {
            println("Límite de tareas alcanzado (${currentTasks.size}/$maxTasks)")
            // Podrías mostrar un Toast o Snackbar si lo llamas desde un Composable o Activity
            return
        }

        currentTasks.add(task)
        saveTasks(context, currentTasks)
        println("Tarea agregada correctamente. Total: ${currentTasks.size}/$maxTasks")
    }

    // Eliminar tarea por ID
    fun removeTaskById(context: Context, taskId: Int) {
        val currentTasks = loadTasks(context).toMutableList()
        currentTasks.removeAll { it.id == taskId }
        saveTasks(context, currentTasks)
    }

    // Obtener siguiente ID disponible
    fun getNextId(context: Context): Int {
        val currentTasks = loadTasks(context)
        return if (currentTasks.isEmpty()) 1
        else currentTasks.maxOf { it.id } + 1
    }

    // Quitar un paso de una tarea (si tiene steps)
    fun removeStepFromTask(context: Context, taskId: Int, stepIndex: Int) {
        val currentTasks = loadTasks(context).toMutableList()
        val task = currentTasks.find { it.id == taskId }
        task?.let {
            if (stepIndex in it.steps.indices) {
                it.steps.removeAt(stepIndex)
            }
        }
        saveTasks(context, currentTasks)
    }

    // ------------------- NUEVA FUNCIÓN -------------------
    // Actualizar tarea existente
    fun updateTask(context: Context, task: Task) {
        val currentTasks = loadTasks(context).toMutableList()
        val index = currentTasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            currentTasks[index] = task
            saveTasks(context, currentTasks)
            println("Tarea actualizada: ${task.id}")
        } else {
            println("Tarea no encontrada para actualizar: ${task.id}")
        }
    }
}

