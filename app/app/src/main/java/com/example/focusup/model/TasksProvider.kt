package com.example.focusup.model

import java.time.LocalDate
import java.time.LocalTime

object TasksProvider {
    fun getTasksList(): List<Task> {
        return listOf(
            Task("Tarea 1", "Descripción de la tarea 1", LocalDate.now().plusDays(5), LocalTime.of(14, 0), 3, listOf("Paso 1", "Paso 2"), 1),
            Task("Lectura 1", "Descripción de la lectura 1", LocalDate.now().plusDays(1), LocalTime.of(16, 0), 2, listOf("Leer capítulo 1", "Leer capítulo 2"), 2),
            Task("Tarea 2", "Descripción de la tarea 2", LocalDate.now().plusDays(2), LocalTime.of(10, 0), 4, emptyList(), 3),
            Task("Proyecto", "Descripción del proyecto", LocalDate.now().plusDays(4), LocalTime.of(23, 59), 5, emptyList(), 4),
            Task("Tarea 5", "Descripción de la tarea 5", LocalDate.now().plusDays(14), LocalTime.of(12, 0), 1, emptyList(), 5),
        )
    }
}