package com.example.focusup

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import com.example.focusup.ui.theme.FocusUpTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material3.Surface
import androidx.compose.ui.window.Dialog

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun isPortrait(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FocusUpTheme {
                CalendarMockupScreen()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CalendarMockupScreen() {
    val daysOfMonth = getDaysOfCurrentMonth()

    val tasksByDay = mapOf(
        daysOfMonth[0] to listOf("Tarea 1", "Proyecto"),     // dia 1
        daysOfMonth[1] to listOf("Lectura 1"),               // dia 2
        daysOfMonth[2] to listOf("Tarea 2"),                 // dia 3
        daysOfMonth[4] to listOf("Proyecto"),                // dia 5
        daysOfMonth[14] to listOf("Tarea 5")                 // dia 15
    )
    
    val columns = if (isPortrait()) 4 else 7
    var showTaskDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf("") }

    var showAddTaskDialog by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FocusUp - Calendario", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = { /* TODO: cerrar sesion */ }) {
                        Text("Cerrar sesion")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Boton arriba
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { showAddTaskDialog = true }) {
                    Text("+ Agregar tarea")
                }
            }

            // Grid de dias
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(daysOfMonth) { day ->
                    Card(
                        modifier = Modifier.height(120.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            // Nombre del dia
                            Text(day, fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.height(4.dp))

                            // Mostrar tareas si existen
                            val tasks = tasksByDay[day] ?: emptyList()
                            tasks.forEach { task ->
                                Surface(
                                    tonalElevation = 0.dp,
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFDBEAFE),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp)
                                        .clickable {
                                            selectedTask = task   // guardamos la tarea seleccionada
                                            showTaskDialog = true  // mostramos el diálogo
                                        }
                                ) {
                                    Text(
                                        task,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (showTaskDialog) {
                AlertDialog(
                    onDismissRequest = { showTaskDialog = false },
                    title = { Text(text = selectedTask) },
                    text = { Text("Detalles de la tarea (mockup).") },
                    confirmButton = {
                        Button(onClick = { showTaskDialog = false }) {
                            Text("Completar tarea") // por ahora no hace nada
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showTaskDialog = false }) {
                            Text("Cerrar")
                        }
                    }
                )
            }

            if (showAddTaskDialog) {
                AddTaskDialog(
                    onDismiss = { showAddTaskDialog = false },
                    onAddTask = { 
                        // TODO: aqui mas adelante se guardara la tarea
                        showAddTaskDialog = false
                    }
                )
            }


            Spacer(modifier = Modifier.weight(1f))

            // Footer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("© 2025 FocusUp - Mockup", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f) // casi pantalla completa
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Nueva Tarea", style = MaterialTheme.typography.titleLarge)

                var taskName by remember { mutableStateOf("") }
                var taskDescription by remember { mutableStateOf("") }
                var taskDeadline by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    label = { Text("Nombre de la tarea") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    label = { Text("Descripcion") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = taskDeadline,
                    onValueChange = { taskDeadline = it },
                    label = { Text("Fecha de entrega") },
                    modifier = Modifier.fillMaxWidth()
                )

                var selectedDifficulty by remember { mutableStateOf(1) }
                var expanded by remember { mutableStateOf(false) }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Dificultad:")

                    Box {
                        Button(onClick = { expanded = true }) {
                            Text(selectedDifficulty.toString())
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            (1..5).forEach { level ->
                                DropdownMenuItem(
                                    text = { Text(level.toString()) },
                                    onClick = {
                                        selectedDifficulty = level
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Regresar")
                    }
                    Button(onClick = onAddTask) {
                        Text("Agregar")
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun getDaysOfCurrentMonth(): List<String> {
    val today = LocalDate.now()
    val currentMonth = YearMonth.from(today)
    val daysInMonth = currentMonth.lengthOfMonth()

    // Genera una lista con los dias del mes y el nombre del día de la semana
    return (1..daysInMonth).map { dayNumber ->
        val date = currentMonth.atDay(dayNumber)
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES")) // Lunes, Martes, ...
        "$dayOfWeek $dayNumber"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 900, heightDp = 480)
@Composable
fun CalendarMockupPreview() {
    FocusUpTheme {
        CalendarMockupScreen()
    }
}
