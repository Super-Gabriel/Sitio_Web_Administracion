package com.focusup.focusupapp.storage

import android.content.Context
import com.focusup.focusupapp.model.Account
import com.google.gson.Gson
import java.io.*

class SessionStorage {

    companion object {
        private const val SESSION_FILE = "current_session.json"

        /**
         * Guarda la sesión actual en un archivo JSON
         */
        fun saveSession(context: Context, account: Account) {
            try {
                val file = File(context.filesDir, SESSION_FILE)
                val gson = Gson()
                val jsonString = gson.toJson(account)
                
                FileOutputStream(file).use { outputStream ->
                    outputStream.write(jsonString.toByteArray())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * Carga la sesión guardada desde el archivo JSON
         */
        fun loadSession(context: Context): Account? {
            return try {
                val file = File(context.filesDir, SESSION_FILE)
                if (!file.exists()) {
                    return null
                }
                
                FileInputStream(file).use { inputStream ->
                    val jsonString = inputStream.bufferedReader().use { it.readText() }
                    val gson = Gson()
                    gson.fromJson(jsonString, Account::class.java)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        /**
         * Elimina la sesión guardada (para logout)
         */
        fun clearSession(context: Context) {
            try {
                val file = File(context.filesDir, SESSION_FILE)
                if (file.exists()) {
                    file.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * Verifica si existe una sesión guardada
         */
        fun hasSavedSession(context: Context): Boolean {
            return try {
                val file = File(context.filesDir, SESSION_FILE)
                file.exists()
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}