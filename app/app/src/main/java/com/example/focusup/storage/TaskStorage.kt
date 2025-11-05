package com.example.focusup.storage

import android.content.Context
import android.widget.Toast
import com.example.focusup.model.Task
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.time.LocalDate
import java.time.LocalTime

object TaskStorage {
    private const val FILENAME = "tasks.json"
    private const val MAX_TASKS_FREE = 10
    private const val MAX_TASKS_PREMIUM = 50

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
    fun addTask(context: Context, task: Task, isPremium: Boolean): Boolean {
        val currentTasks = loadTasks(context).toMutableList()

        // Determinar límite según tipo de cuenta
        val maxTasks = if (isPremium) MAX_TASKS_PREMIUM else MAX_TASKS_FREE

        // Validar si alcanzó el límite
        if (currentTasks.size >= maxTasks) {
            val message = "Límite de tareas alcanzado (${currentTasks.size}/$maxTasks)"
            println("⚠️ $message")

            // Mostrar Toast con mensaje al usuario
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            return false
        }

        // Agregar nueva tarea y guardar
        currentTasks.add(task)
        saveTasks(context, currentTasks)
        println("Tarea agregada correctamente. Total: ${currentTasks.size}/$maxTasks")
        return true
    }

    // Eliminar tarea por ID
    fun removeTaskById(context: Context, taskId: Int) {
        val currentTasks = loadTasks(context).toMutableList()
        currentTasks.removeAll { it.id == taskId }
        saveTasks(context, currentTasks)
        println("Tarea eliminada con ID: $taskId")
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
                println("✂️ Paso eliminado de la tarea ${task.id} en posición $stepIndex")
            }
        }
        saveTasks(context, currentTasks)
    }

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
