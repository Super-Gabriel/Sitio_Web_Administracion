package com.example.focusup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.time.LocalDate
import com.example.focusup.model.Task
import com.example.focusup.storage.TaskStorage  // Importar TaskStorage
import android.graphics.*

class ReminderReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
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

        val originalLogo = BitmapFactory.decodeResource(context.resources, R.drawable.logo)
        val circularLogo = getCircularBitmap(originalLogo)

        val notification = NotificationCompat.Builder(context, "TASK_REMINDER_CHANNEL")
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(circularLogo)
            .setContentTitle("FocusUp - Recordatorio diario")
            .setContentText(notificationText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notificationText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(1001, notification)
        }
    }

    // Funcion para crear un bitmap circular
    private fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        val size = minOf(bitmap.width, bitmap.height)
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint().apply { isAntiAlias = true }
        val rect = Rect(0, 0, size, size)
        val rectF = RectF(rect)
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, null, rectF, paint)
        return output
    }
}