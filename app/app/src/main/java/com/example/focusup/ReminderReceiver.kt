package com.example.focusup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.focusup.model.AppDatabase
import com.example.focusup.model.TaskEntity
import com.example.focusup.model.TaskRepository
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import kotlinx.coroutines.flow.first

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        runBlocking {
            try {
                val database = AppDatabase.getInstance(context)
                val repository = TaskRepository(database.taskDao())
                
                // Obtener todas las tareas
                val tasksFlow = repository.getAllTasks()
                val tasksList = tasksFlow.first() // Obtiene el primer valor del Flow
                
                // Buscar la tarea más próxima
                val now = LocalDate.now()
                val upcomingTasks = tasksList.filter { task -> task.dueDate >= now }
                val nextTask = upcomingTasks.minWithOrNull(compareBy<TaskEntity> { it.dueDate })
                
                // Buscar la tarea más difícil
                val hardestTask = tasksList.maxWithOrNull(compareBy<TaskEntity> { it.difficulty })
                
                // Texto de la notificación
                val notificationText = if (nextTask != null && hardestTask != null) {
                    "Próxima tarea: ${nextTask.title}\n" +
                    "Tarea más difícil: ${hardestTask.title}"
                } else if (nextTask != null) {
                    "Próxima tarea: ${nextTask.title}"
                } else if (hardestTask != null) {
                    "Tarea más difícil: ${hardestTask.title}"
                } else {
                    "No hay tareas pendientes"
                }
                
                val notification = NotificationCompat.Builder(context, "TASK_REMINDER_CHANNEL")
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)
                    .setContentTitle("FocusUp - Recordatorio diario")
                    .setContentText(notificationText)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build()
                
                with(NotificationManagerCompat.from(context)) {
                    notify(1001, notification)
                }
            } catch (e: Exception) {
                // Manejar error silenciosamente
                e.printStackTrace()
            }
        }
    }
}