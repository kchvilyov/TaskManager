package ru.kchvilyov.taskmanager.ru.kchvilyov.taskmanager.core.di

import ru.kchvilyov.taskmanager.ru.kchvilyov.taskmanager.core.domain.Task
import ru.kchvilyov.taskmanager.ru.kchvilyov.taskmanager.core.presentation.TaskViewModel

class AppContainer {
//    private val dataSource: TaskRepository = LocalTaskDataSource()
//    private val repository: TaskRepository = TaskRepositoryImpl(dataSource)
//    private val viewModelScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val initialTasks = listOf(
        Task(1, "Task 1", "Description 1", false, 1000L),
        Task(2, "Task 2", "Description 2", true, 2000L),
        Task(3, "Task 3", "Description 3", false, 3000L)
    )
    val taskViewModel = TaskViewModel(initialTasks)
}