package ru.kchvilyov.taskmanager.core.domain

interface TaskRepository {
    suspend fun getAllTasks(): List<Task>
    suspend fun getTaskById(id: Long): Task?
    suspend fun insertTask(task: Task): Task
    suspend fun updateTask(task: Task): Boolean
    suspend fun deleteTask(id: Long): Boolean
}