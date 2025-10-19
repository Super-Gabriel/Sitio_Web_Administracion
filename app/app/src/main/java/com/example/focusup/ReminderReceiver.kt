package com.example.focusup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.time.LocalDate
import com.example.focusup.model.Task
import com.example.focusup.storage.TaskStorage  // Importar TaskStorage

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // Cargar tareas desde el almacenamiento en lugar de TasksProvider
        val tasks = TaskStorage.loadTasks(context)  // Usar TaskStorage

        // Buscar la tarea mas proxima
        val now = LocalDate.now()
        val upcomingTasks = tasks.filter { it.dueDate >= now }
        val nextTask = upcomingTasks.minWithOrNull(compareBy<Task> { it.dueDate })

        // Buscar la tarea mas dificil
        val hardestTask = tasks.maxWithOrNull(compareBy<Task> { it.difficulty })

        // Texto de la notificacion
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
    }
}