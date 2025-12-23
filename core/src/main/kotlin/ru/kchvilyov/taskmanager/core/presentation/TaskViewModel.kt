package ru.kchvilyov.taskmanager.core.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.kchvilyov.taskmanager.core.domain.TaskRepository

class TaskViewModel(private val taskRepository: TaskRepository) {
    private val _state = MutableStateFlow(TaskState())
    val state: StateFlow<TaskState> = _state

    suspend fun processIntent(intent: TaskIntent) {
        when (intent) {
            is TaskIntent.AddTask -> {
                taskRepository.insertTask(intent.task)
                refreshTasks()
            }
            is TaskIntent.ToggleTask -> {
                val currentTask = taskRepository.getTaskById(intent.taskId)
                if (currentTask != null) {
                    taskRepository.updateTask(currentTask.copy(isCompleted = !currentTask.isCompleted))
                    refreshTasks()
                }
            }
        }
    }

    private suspend fun refreshTasks() {
        _state.value = _state.value.copy(
            isLoading = false,
            tasks = taskRepository.getAllTasks()
        )
    }
}