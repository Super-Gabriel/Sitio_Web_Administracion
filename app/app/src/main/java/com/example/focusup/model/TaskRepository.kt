package com.example.focusup.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<TaskEntity>> {
        return taskDao.getAllTasks()
    }
    
    suspend fun insertTask(task: TaskEntity) {
        withContext(Dispatchers.IO) {
            taskDao.insertTask(task)
        }
    }
    
    suspend fun deleteTask(task: TaskEntity) {
        withContext(Dispatchers.IO) {
            taskDao.deleteTask(task)
        }
    }
}