package com.example.focusup

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.Configuration
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import android.util.Patterns

import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale
import java.time.format.DateTimeFormatter
import java.time.LocalTime

import com.example.focusup.model.Task
//import com.example.focusup.model.TasksProvider
import com.example.focusup.ui.theme.SpecialRed
import com.example.focusup.ui.theme.SpecialRed2
import com.example.focusup.ui.theme.SpecialOrange
import com.example.focusup.ui.theme.SpecialOrange2
import com.example.focusup.ui.theme.SpecialYellow
import com.example.focusup.ui.theme.SpecialYellow2
import com.example.focusup.ui.theme.SpecialGreen
import com.example.focusup.ui.theme.SpecialGreen2
import com.example.focusup.ui.theme.SpecialBlue
import com.example.focusup.ui.theme.SpecialBlue2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import java.util.*

// Import para TaskStorage
import com.example.focusup.storage.TaskStorage
import java.time.format.DateTimeFormatter.ofPattern

// Crear canal de notificacion
@RequiresApi(Build.VERSION_CODES.O)
private fun createNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        "TASK_REMINDER_CHANNEL",
        "Recordatorios de tareas",
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = "Canal para recordatorios diarios de FocusUp"
    }
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    manager.createNotificationChannel(channel)
}

// Recordatorio diario a las 9:00 AM
@RequiresApi(Build.VERSION_CODES.O)
fun scheduleDailyReminder(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, ReminderReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    // Configura la hora 9:00 a.m.
    val calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }
    // En caso de que la hora ya haya pasado, programa para el dia siguiente
    if (calendar.timeInMillis < System.currentTimeMillis()) {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY,
        pendingIntent
    )
}

@RequiresApi(Build.VERSION_CODES.O)
val dateFormatter = ofPattern("dd/MM/yyyy")
@RequiresApi(Build.VERSION_CODES.O)
val timeFormatter = ofPattern("HH:mm")

@Composable
fun isPortrait(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }
        createNotificationChannel(this)
        scheduleDailyReminder(this)
        setContent {
            FocusUpTheme {
                CalendarScreen(context = this)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CalendarScreen(context: Context) {
    // Cargar tareas desde el almacenamiento
    val tasksList = remember { 
        mutableStateListOf<Task>().apply { 
            addAll(TaskStorage.loadTasks(context)) 
        } 
    }

    val loggedIn = false // TODO: implementar login

    val today = LocalDate.now()
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysOfMonth: List<LocalDate> = (1..currentMonth.lengthOfMonth()).map { currentMonth.atDay(it) }

    val firstDayOfMonth = currentMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value // 1 = lunes, 7 = domingo (segun java.time)
    val daysInMonth = currentMonth.lengthOfMonth()
    val daysGrid = mutableListOf<LocalDate?>()
    // Aniade nulls hasta el primer día de la semana
    repeat(firstDayOfWeek - 1) { daysGrid.add(null) }
    // Aniade los días reales
    for (day in 1..daysInMonth) {
        daysGrid.add(currentMonth.atDay(day))
    }

    val monthFormatter = ofPattern("MMMM yyyy", Locale("es", "ES"))
    val monthName = currentMonth.format(monthFormatter).replaceFirstChar { 
        if (it.isLowerCase()) it.titlecase(Locale("es", "ES")) else it.toString() 
    }

    val tasks = tasksList

    val tasksByDay: Map<LocalDate, List<Task>> =
    daysOfMonth.associateWith { day ->
        tasks.filter { it.dueDate.year == day.year && it.dueDate.month == day.month && it.dueDate.dayOfMonth == day.dayOfMonth }
    }
    
    var showTaskDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<Task?>(null) }
    var showTaskCompletedDialog by remember { mutableStateOf(false) }

    var showAddTaskDialog by remember { mutableStateOf(false) }
    var showTaskCreatedDialog by remember { mutableStateOf(false) }

    var showAddAccountDialog by remember { mutableStateOf(false) }
    var showAccountCreatedDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FocusUp", fontWeight = FontWeight.Bold) },
                actions = {
                    // Si el usuario esta logueado, mostrar boton de perfil
                    if (loggedIn) {
                        TextButton(onClick = { /* TODO: perfil */ }) {
                            Text("Perfil", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    // Si el usuario no esta logueado, mostrar botones de login y registro
                    else {
                        TextButton(onClick = { /* TODO: login */ }) {
                            Text("Iniciar sesión", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        TextButton(onClick = { showAddAccountDialog = true }) {
                            Text("Registrarse", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("© 2025 FocusUp", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                    text = monthName,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Button(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                    Text(">")
                }
            }
            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom").forEach { dayName ->
                    Text(dayName, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onBackground)
                }
            }
            Spacer(modifier = Modifier.height(15.dp))

            // Grid de dias
            LazyVerticalGrid(
                columns = GridCells.Fixed(7), // 7 columnas para los dias de la semana
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(daysGrid) { date ->
                    if (date != null) {
                        Card(
                            modifier = Modifier.height(100.dp),
                            shape = RoundedCornerShape(15.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                // Numero del dia
                                Text(
                                    date.dayOfMonth.toString(),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
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
                                            color = GetTaskColor(task.difficulty),
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
                                                    color = MaterialTheme.colorScheme.onSecondary,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    task.dueTime.format(timeFormatter),
                                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                                    color = MaterialTheme.colorScheme.onSecondary,
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

                    }
                }
            }

            if (showTaskDialog && selectedTask != null) {
                AlertDialog(
                    onDismissRequest = { showTaskDialog = false },
                    title = { Text(text = selectedTask!!.title, color = MaterialTheme.colorScheme.onSecondary) },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Descripción: ${selectedTask!!.description}", color = MaterialTheme.colorScheme.onSecondary)
                            Text("Fecha: ${selectedTask!!.dueDate.format(dateFormatter)}", color = MaterialTheme.colorScheme.onSecondary)
                            Text("Hora: ${selectedTask!!.dueTime.format(timeFormatter)}", color = MaterialTheme.colorScheme.onSecondary)
                            Text("Dificultad: ${selectedTask!!.difficulty}/5", color = MaterialTheme.colorScheme.onSecondary)
                            if (selectedTask!!.steps.isNotEmpty()) {
                                Text("Pasos:", color = MaterialTheme.colorScheme.onSecondary)
                                selectedTask!!.steps.forEach { step ->
                                    Text("- $step", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondary)
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            showTaskDialog = false
                            selectedTask?.let { task ->
                                tasksList.remove(task)
                                TaskStorage.removeTaskById(context, task.id)
                            }
                            selectedTask = null
                            showTaskCompletedDialog = true
                         },
                         colors = ButtonDefaults.buttonColors(
                            containerColor = GetTaskColorText(selectedTask!!.difficulty),
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )) {
                            Text("Completar tarea")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showTaskDialog = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GetTaskColorText(selectedTask!!.difficulty),
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            )) {
                            Text("Cerrar")
                        }
                    },
                    containerColor = GetTaskColor(selectedTask!!.difficulty)
                )
            }

            if (showTaskCompletedDialog) {
                TaskCompletedDialog(
                    onDismiss = { showTaskCompletedDialog = false }
                )
            }

            if (showAddTaskDialog) {
                AddTaskDialog(
                    onDismiss = { showAddTaskDialog = false
                        showTaskCreatedDialog = true
                    },
                    onAddTask = { newTask ->
                        tasksList.add(newTask)
                        TaskStorage.addTask(context, newTask)
                    },
                    nextId = TaskStorage.getNextId(context)
                )
            }

            if (showTaskCreatedDialog) {
                TaskCreatedDialog(
                    onDismiss = { showTaskCreatedDialog = false }
                )
            }

            if (showAddAccountDialog) {
                AddAccountDialog(
                    onDismiss = { 
                        showAddAccountDialog = false 
                        showAccountCreatedDialog = true
                    }, // TODO: implementar creacion de cuenta
                    //onAddAccount = { newAccount ->
                    //    accountsList.add(newAccount)
                    //    AccountStorage.addAccount(context, newAccount)
                    //}
                )
            }

            if (showAccountCreatedDialog) {
                AccountCreatedDialog(
                    onDismiss = { showAccountCreatedDialog = false }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (Task) -> Unit,
    nextId: Int
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
                var taskDueDate by remember { mutableStateOf(LocalDate.now()) }
                var taskDueTime by remember { mutableStateOf(LocalTime.now()) }
                var selectedDifficulty by remember { mutableStateOf(1) }
                var expanded by remember { mutableStateOf(false) }
                var taskSteps = remember { mutableStateListOf<String>() }

                val context = LocalContext.current

                OutlinedTextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    label = { Text("Nombre de la tarea", color = MaterialTheme.colorScheme.onBackground) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    label = { Text("Descripcion", color = MaterialTheme.colorScheme.onBackground) },
                    modifier = Modifier.fillMaxWidth()
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = taskDueDate.format(dateFormatter),
                        onValueChange = {},
                        label = { Text("Fecha de entrega" ,color = MaterialTheme.colorScheme.onBackground)},
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
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
                    )
                }

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = taskDueTime.format(timeFormatter),
                        onValueChange = {},
                        label = { Text("Hora de entrega", color = MaterialTheme.colorScheme.onBackground) },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
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
                            }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Dificultad:", color = MaterialTheme.colorScheme.onBackground)

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

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Pasos", color = MaterialTheme.colorScheme.onBackground)

                    taskSteps.forEachIndexed { index, step ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = step,
                                onValueChange = { newValue ->
                                    taskSteps[index] = newValue
                                },
                                label = { Text("Paso ${index + 1}", color = MaterialTheme.colorScheme.onBackground) },
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(onClick = { taskSteps.removeAt(index) }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Eliminar paso"
                                )
                            }
                        }
                    }

                    Button(
                        onClick = { taskSteps.add("") },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Agregar paso")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    Button(onClick = onDismiss) {
                        Text("Regresar")
                    }
                    Button(
                        onClick = {
                            val newTask = Task(
                                title = if (taskName == "") "Tarea sin nombre" else taskName,
                                description = if (taskDescription == "") "Descripcion de la tarea" else taskDescription,
                                dueDate = taskDueDate,
                                dueTime = taskDueTime.withSecond(0).withNano(0),
                                difficulty = selectedDifficulty,
                                steps = taskSteps,
                                id = nextId
                            )
                            onAddTask(newTask)
                            onDismiss()
                        }
                    ) {
                        Text("Agregar tarea")
                    }
                }
            }
        }
    }
}

// Funcion para crear una cuenta
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountDialog(
    onDismiss: () -> Unit,
    //onAddAccount: (Account) -> Unit
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
                Text("Crear Cuenta", style = MaterialTheme.typography.titleLarge)

                var accountName by remember { mutableStateOf("") }
                var accountEmail by remember { mutableStateOf("") }
                var accountPassword by remember { mutableStateOf("") }
                var accountConfirmPassword by remember { mutableStateOf("") }
                var accountPremium by remember { mutableStateOf(false) }

                var emailErrorEmpty by remember { mutableStateOf(false) }
                var passwordErrorEmpty by remember { mutableStateOf(false) }
                var confirmPasswordErrorMismatch by remember { mutableStateOf(false) }
                var emailErrorUsed by remember { mutableStateOf(false) }
                var emailErrorInvalid by remember { mutableStateOf(false) }

                OutlinedTextField(
                    value = accountName,
                    onValueChange = { accountName = it },
                    label = { Text("Nombre de la cuenta", color = MaterialTheme.colorScheme.onBackground) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = accountEmail,
                    onValueChange = { accountEmail = it },
                    label = { Text("Email", color = MaterialTheme.colorScheme.onBackground) },
                    modifier = Modifier.fillMaxWidth()
                )
                if (emailErrorEmpty) {
                    Text(
                        text = "El correo no puede estar vacío",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (emailErrorUsed) {
                    Text(
                        text = "El correo ya está en uso",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (emailErrorInvalid) {
                    Text(
                        text = "El correo no es válido",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = accountPassword,
                    onValueChange = { accountPassword = it },
                    label = { Text("Contraseña", color = MaterialTheme.colorScheme.onBackground) },
                    modifier = Modifier.fillMaxWidth()
                )
                if (passwordErrorEmpty) {
                    Text(
                        text = "La contraseña no puede estar vacía",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = accountConfirmPassword,
                    onValueChange = { accountConfirmPassword = it },
                    label = { Text("Confirmar contraseña", color = MaterialTheme.colorScheme.onBackground) },
                    modifier = Modifier.fillMaxWidth()
                )
                if (confirmPasswordErrorMismatch) {
                    Text(
                        text = "Las contraseñas no coinciden",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(
                        checked = accountPremium,
                        onCheckedChange = { accountPremium = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.onBackground,
                            checkmarkColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    Text("Premium", color = MaterialTheme.colorScheme.onBackground)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    Button(onClick = onDismiss) {
                        Text("Regresar")
                    }
                    Button(
                        onClick = {
                            emailErrorEmpty = false
                            passwordErrorEmpty = false
                            confirmPasswordErrorMismatch = false
                            emailErrorUsed = false
                            emailErrorInvalid = false

                            // Revisar si correo vacio
                            if (accountEmail.isEmpty()) {
                                emailErrorEmpty = true
                                return@Button
                            }
                            // Revisar si contrasenia vacia
                            if (accountPassword.isEmpty()) {
                                passwordErrorEmpty = true
                                return@Button
                            }
                            // Revisar si contrasenias coinciden
                            if (accountPassword != accountConfirmPassword) {
                                confirmPasswordErrorMismatch = true
                                return@Button
                            }
                            // Revisar si correo valido
                            if (!Patterns.EMAIL_ADDRESS.matcher(accountEmail).matches()) {
                                emailErrorInvalid = true
                                return@Button
                            }
                            // Revisar si correo en uso
                            if (false) { // TODO: implementar verificacion de correo en uso
                                emailErrorUsed = true
                                return@Button
                            }
                            // TODO: implementar creacion de cuenta
                            onDismiss()
                        }
                    ) {
                        Text("Crear cuenta")
                    }
                }
            }
        }
    }
}

// Funcion para mostrar dialogo de cuenta creada
@Composable
fun AccountCreatedDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cuenta creada", color = MaterialTheme.colorScheme.onBackground) },
        text = { Text("Su cuenta ha sido creada exitosamente.", color = MaterialTheme.colorScheme.onBackground) },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Aceptar")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}

// Funcion para mostrar dialogo de tarea creada
@Composable
fun TaskCreatedDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tarea creada", color = MaterialTheme.colorScheme.onBackground) },
        text = { Text("Su tarea ha sido creada exitosamente.", color = MaterialTheme.colorScheme.onBackground) },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Aceptar")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}

// Funcion para mostrar dialogo de tarea completada
@Composable
fun TaskCompletedDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tarea completada", color = MaterialTheme.colorScheme.onBackground) },
        text = { Text("¡Felicidades! Has completado la tarea.", color = MaterialTheme.colorScheme.onBackground) },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Aceptar")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}

// Funcion de preview
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 900, heightDp = 480)
@Composable
fun CalendarPreview() {
    FocusUpTheme {
        // Para el preview, pasamos un contexto nulo (no se guardarán datos reales)
        CalendarScreen(context = LocalContext.current)
    }
}

// Funcion para obtener un color segun la dificultad
@Composable
fun GetTaskColor(difficulty: Int): Color {
    return when (difficulty) {
        5 -> SpecialRed
        4 -> SpecialOrange
        3 -> SpecialYellow
        2 -> SpecialGreen
        1 -> SpecialBlue
        else -> MaterialTheme.colorScheme.secondary
    }
}

// Funcion para obtener un color para texto segun la dificultad
@Composable
fun GetTaskColorText(difficulty: Int): Color {
    return when (difficulty) {
        5 -> SpecialRed2
        4 -> SpecialOrange2
        3 -> SpecialYellow2
        2 -> SpecialGreen2
        1 -> SpecialBlue2
        else -> MaterialTheme.colorScheme.secondary
    }
}