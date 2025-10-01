package com.example.focusup

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale
import java.time.format.DateTimeFormatter
import java.time.LocalTime

import com.example.focusup.model.Task


val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@RequiresApi(Build.VERSION_CODES.O)
val tasksList = mutableStateListOf<Task>().apply { addAll(getTasksList()) }

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
    val today = LocalDate.now()
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysOfMonth: List<LocalDate> = (1..currentMonth.lengthOfMonth()).map { currentMonth.atDay(it) }

    val firstDayOfMonth = currentMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value // 1 = lunes, 7 = domingo (según java.time)
    val daysInMonth = currentMonth.lengthOfMonth()
    val daysGrid = mutableListOf<LocalDate?>()
    // Aniade nulls hasta el primer día de la semana
    repeat(firstDayOfWeek - 1) { daysGrid.add(null) }
    // Aniade los días reales
    for (day in 1..daysInMonth) {
        daysGrid.add(currentMonth.atDay(day))
    }

    val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "ES"))
    val monthName = currentMonth.format(monthFormatter)

    val tasks = tasksList

    val tasksByDay: Map<LocalDate, List<Task>> =
    daysOfMonth.associateWith { day ->
        tasks.filter { it.dueDate.year == day.year && it.dueDate.month == day.month && it.dueDate.dayOfMonth == day.dayOfMonth }
    }
    
    var showTaskDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<Task?>(null) }


    var showAddTaskDialog by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FocusUp - Calendario", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = { /* TODO: perfil */ }) {
                        Text("Perfil", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                    Text("<")
                }
                Text(
                    text = monthName.capitalize(),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Button(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                    Text(">")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom").forEach { dayName ->
                    Text(dayName, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
            }


            // Grid de dias
            LazyVerticalGrid(
                columns = GridCells.Fixed(7), // 7 columnas para los dias de la semana
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(daysGrid) { date ->
                    if (date != null) {
                        Card(
                            modifier = Modifier.height(120.dp),
                            shape = RoundedCornerShape(10.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                // Numero del dia
                                Text(
                                    date.dayOfMonth.toString(),
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // Mostrar tareas si existen
                                val tasksOfDay = tasksByDay[date] ?: emptyList()
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    tasksOfDay.forEach { task ->
                                        Surface(
                                            tonalElevation = 0.dp,
                                            shape = RoundedCornerShape(6.dp),
                                            color = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 2.dp)
                                                .clickable {
                                                    selectedTask = task
                                                    showTaskDialog = true
                                                }
                                        ) {
                                            Column(modifier = Modifier.padding(4.dp)) {
                                                Text(
                                                    task.title,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.sp,
                                                    color = MaterialTheme.colorScheme.tertiary,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    task.dueTime.format(timeFormatter),
                                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                                    color = MaterialTheme.colorScheme.tertiary,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // Celda vacia para alinear el primer dia del mes
                        Box(modifier = Modifier.height(110.dp)) { }
                    }
                }
            }

            if (showTaskDialog && selectedTask != null) {
                AlertDialog(
                    onDismissRequest = { showTaskDialog = false },
                    title = { Text(text = selectedTask!!.title) },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Descripción: ${selectedTask!!.description}")
                            Text("Fecha: ${selectedTask!!.dueDate}")
                            Text("Hora: ${selectedTask!!.dueTime}")
                            Text("Dificultad: ${selectedTask!!.difficulty}/5")
                            if (selectedTask!!.steps.isNotEmpty()) {
                                Text("Pasos:")
                                selectedTask!!.steps.forEach { step ->
                                    Text("- $step", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            showTaskDialog = false
                            selectedTask?.let { removeTaskById(it.id) }
                            selectedTask = null
                         }) {
                            Text("Completar tarea")
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
                    onAddTask = { newTask ->
                        tasks.add(newTask)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (Task) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 4.dp,
            color = MaterialTheme.colorScheme.background,
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
                var taskDueDate by remember { mutableStateOf(LocalDate.now()) } // fecha como LocalDate
                var taskDueTime by remember { mutableStateOf(LocalTime.now()) } // hora como LocalTime
                //var taskDueDate by remember { mutableStateOf("") } // fecha como String
                //var taskDueTime by remember { mutableStateOf("") } // hora como String
                var selectedDifficulty by remember { mutableStateOf(1) }
                var expanded by remember { mutableStateOf(false) }
                var taskSteps by remember { mutableStateOf(listOf<String>()) }

                val context = LocalContext.current

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
                    value = taskDueDate.format(dateFormatter),
                    onValueChange = {},
                    label = { Text("Fecha de entrega") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val picker = DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    taskDueDate = LocalDate.of(year, month + 1, dayOfMonth)
                                },
                                taskDueDate.year,
                                taskDueDate.monthValue - 1,
                                taskDueDate.dayOfMonth
                            )
                            picker.show()
                        },
                    readOnly = true
                )

                OutlinedTextField(
                    value = taskDueTime.format(timeFormatter),
                    onValueChange = {},
                    label = { Text("Hora de entrega") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val picker = TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    taskDueTime = LocalTime.of(hour, minute)
                                },
                                taskDueTime.hour,
                                taskDueTime.minute,
                                true
                            )
                            picker.show()
                        },
                    readOnly = true
                )

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

                OutlinedTextField(
                    value = "", // pasos no implementados aun
                    onValueChange = { /* TODO: manejar pasos */ },
                    label = { Text("Pasos (no implementado)") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Regresar")
                    }
                    Button(
                        onClick = {
                            val newId = (tasksList.maxOfOrNull { it.id } ?: 0) + 1
                            val newTask = Task(
                                title = if (taskName == "") "Tarea sin nombre" else taskName,
                                description = if (taskDescription == "") "Descripcion de la tarea" else taskDescription,
                                dueDate = taskDueDate,
                                dueTime = taskDueTime.withSecond(0).withNano(0),
                                difficulty = selectedDifficulty,
                                steps = taskSteps,
                                id = newId
                            )
                            onAddTask(newTask)
                            onDismiss()
                        }
                    ) {
                        Text("Agregar")
                    }
                }
            }
        }
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

fun getTasksList(): List<Task> {
    return listOf(
        Task("Tarea 1", "Descripcion de la tarea 1", LocalDate.now().plusDays(5), java.time.LocalTime.of(14, 0), 3, listOf("Paso 1", "Paso 2"), 1),
        Task("Lectura 1", "Descripcion de la lectura 1", LocalDate.now().plusDays(1), java.time.LocalTime.of(16, 0), 2, listOf("Leer capitulo 1", "Leer capitulo 2"), 2),
        Task("Tarea 2", "Descripcion de la tarea 2", LocalDate.now().plusDays(2), java.time.LocalTime.of(10, 0), 4, emptyList(), 3),
        Task("Proyecto", "Descripcion del proyecto", LocalDate.now().plusDays(4), java.time.LocalTime.of(23, 59), 5, emptyList(), 4),
        Task("Tarea 5", "Descripcion de la tarea 5", LocalDate.now().plusDays(14), java.time.LocalTime.of(12, 0), 1, emptyList(), 5),
    )
}

fun removeTaskById(taskId: Int) {
    tasksList.removeAll { it.id == taskId }
}