package com.focusup.focusupapp.storage

import androidx.test.core.app.ApplicationProvider
import com.focusup.focusupapp.model.Account
import com.focusup.focusupapp.model.Task
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

// ./gradlew :app:testDebugUnitTest --tests "com.focusup.focusupapp.storage.AccountStorageTest" desde Sitio_Web_Administracion/app

@RunWith(RobolectricTestRunner::class)
class AccountStorageTest {

    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()

    // Cuenta de prueba 1
    private val account1 = Account(
        id = 1,
        name = "Pedrito",
        email = "a@a.com",
        password = "12345",
        isPremium = false,
        points = 0,
        purchasedRewards = mutableListOf(),
        createdAt = "2024-06-15",
        tasks = mutableListOf()
    )

    // Cuenta de prueba 2
    private val account2 = Account(
        id = 2,
        name = "Paquito",
        email = "b@b.com",
        password = "67890",
        isPremium = true,
        points = 100,
        purchasedRewards = mutableListOf(),
        createdAt = "2024-06-16",
        tasks = mutableListOf()
    )

    // Tarea de prueba 1
    private val task1 = Task(
        title = "Examen 1",
        description = "Repasar para el examen",
        dueDate = java.time.LocalDate.of(2024, 6, 20),
        dueTime = java.time.LocalTime.of(15, 0),
        difficulty = 3,
        steps = mutableListOf("Hacer resumen", "Resolver ejercicios"),
        id = 1
    )

    // Tarea de prueba 2
    private val task2 = Task(
        title = "Proyecto Final",
        description = "Completar el proyecto final",
        dueDate = java.time.LocalDate.of(2024, 6, 30),
        dueTime = java.time.LocalTime.of(23, 59),
        difficulty = 5,
        steps = mutableListOf(),
        id = 2
    )

    @Before
    fun cleanStorage() {
        // Elimina el archivo antes de cada test
        val file = File(context.filesDir, "accounts.json")
        if (file.exists()) file.delete()
    }

    // Prueba para crear y cargar una cuenta
    @Test
    fun testCreateAndLoadAccount() {
        AccountStorage.saveAccounts(context, listOf(account1))
        val loaded = AccountStorage.loadAccounts(context)
        println("Probando cuenta creada y cargada...")
        assertEquals(1, loaded.size)
        println("Paso la verificacion de cantidad de cuentas")
        val acc = loaded[0]
        assertEquals(1, acc.id)
        println("Paso la verificacion de id")
        assertEquals("Pedrito", acc.name)
        println("Paso la verificacion de nombre")
        assertEquals("a@a.com", acc.email)
        println("Paso la verificacion de email")
        assertEquals("12345", acc.password)
        println("Paso la verificacion de contrasenia")
        println("Cuenta creada y cargada correctamente")
        println()
    }

    // Prueba para verificar email usado
    @Test
    fun testIsEmailUsed() {
        AccountStorage.saveAccounts(context, listOf(account1))
        println("Probando email ya usado...")
        assertTrue(AccountStorage.isEmailUsed(context, "a@a.com"))
        println("Paso la verificacion de email usado")
        assertFalse(AccountStorage.isEmailUsed(context, "b@b.com"))
        println("Paso la verificacion de email no usado")
        println("Email usado verificado correctamente")
        println()
    }

    // Prueba para getNextId
    @Test
    fun testGetNextId() {
        AccountStorage.saveAccounts(context, listOf(account1))
        println("Probando obtencion de siguiente ID...")
        val nextId = AccountStorage.getNextId(context)
        assertEquals(2, nextId)
        println("Paso la verificacion de siguiente ID")
        println("Siguiente ID obtenido correctamente")
        println()
    }

    // Prueba para validar login
    @Test
    fun testValidateLogin() {
        AccountStorage.saveAccounts(context, listOf(account1))
        println("Probando validacion de login...")
        val validAccount = AccountStorage.validateLogin(context, "a@a.com", "12345")
        assertNotNull(validAccount)
        println("Paso la verificacion de login valido")
        val invalidAccount = AccountStorage.validateLogin(context, "a@a.com", "hola")
        assertNull(invalidAccount)
        println("Paso la verificacion de login invalido")
        println("Validacion de login completada correctamente")
        println()
    }

    // addPointsToAccount

    // purchaseReward

    // addTaskToAccount

    // removeTaskFromAccount

    // removeStepFromTaskInAccount

    // getNextTaskIdForAccount

    // getTasksForAccount

    // updateAccount

    // getAccountById
}