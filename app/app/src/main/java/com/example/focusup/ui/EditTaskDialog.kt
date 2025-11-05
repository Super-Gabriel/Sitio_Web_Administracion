package com.example.focusup.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.focusup.model.Task
import com.example.focusup.storage.TaskStorage
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun EditTaskDialog(
    context: Context,
    task: Task,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var date by remember { mutableStateOf(task.dueDate) }
    var time by remember { mutableStateOf(task.dueTime) }

    val localContext = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Título
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Descripción
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Fecha
                TextButton(onClick = {
                    DatePickerDialog(
                        localContext,
                        { _, year, month, dayOfMonth ->
                            date = LocalDate.of(year, month + 1, dayOfMonth)
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }) {
                    Text("Fecha: ${date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}")
                }

                // Hora
                TextButton(onClick = {
                    TimePickerDialog(
                        localContext,
                        { _, hourOfDay, minute ->
                            time = LocalTime.of(hourOfDay, minute)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    ).show()
                }) {
                    Text("Hora: ${time.format(DateTimeFormatter.ofPattern("HH:mm"))}")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botones Cancelar / Guardar
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { onDismiss() }) { Text("Cancelar") }
                    TextButton(onClick = {
                        val updatedTask = task.copy(
                            title = title,
                            description = description,
                            dueDate = date,
                            dueTime = time
                        )
                        TaskStorage.updateTask(context, updatedTask)
                        onSave(updatedTask)
                    }) { Text("Guardar") }
                }
            }
        }
    }
}

