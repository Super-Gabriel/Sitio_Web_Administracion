package com.focusup.focusupapp.storage

import android.content.Context
import com.focusup.focusupapp.model.Account
import com.google.gson.Gson
import java.io.File

object AccountStorage {
    private const val ACCOUNTS_FILE = "accounts.json"
    private val gson = Gson()

    private fun getAccountsFile(context: Context): File {
        return File(context.filesDir, ACCOUNTS_FILE)
    }

    fun loadAccounts(context: Context): List<Account> {
        val file = getAccountsFile(context)
        if (!file.exists()) {
            return emptyList()
        }

        return try {
            val json = file.readText()
            val type = object : com.google.gson.reflect.TypeToken<List<Account>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveAccounts(context: Context, accounts: List<Account>) {
        val file = getAccountsFile(context)
        val json = gson.toJson(accounts)
        file.writeText(json)
    }

    fun addAccount(context: Context, account: Account) {
        val accounts = loadAccounts(context).toMutableList()
        accounts.add(account)
        saveAccounts(context, accounts)
    }

    fun isEmailUsed(context: Context, email: String): Boolean {
        val accounts = loadAccounts(context)
        return accounts.any { it.email == email }
    }

    fun getNextId(context: Context): Int {
        val accounts = loadAccounts(context)
        return if (accounts.isEmpty()) 1 else accounts.maxOf { it.id } + 1
    }

    fun validateLogin(context: Context, email: String, password: String): Account? {
    val accounts = loadAccounts(context)
    return accounts.find { it.email == email && it.password == password }
    }

    // Funcion para dar puntos a una cuenta
    fun addPointsToAccount(context: Context, accountId: Int, points: Int) {
        val accounts = loadAccounts(context)
        val account = accounts.find { it.id == accountId } ?: return
        account.points += points
        saveAccounts(context, accounts)
    }
}