package ru.kchvilyov.taskmanager.ru.kchvilyov.taskmanager.core.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kchvilyov.taskmanager.ru.kchvilyov.taskmanager.core.domain.Task

class TaskViewModel(initialTasks: List<Task>) {
    private val _state = MutableStateFlow(TaskState(isLoading = true, tasks = emptyList()))
    val state: StateFlow<TaskState> = _state.asStateFlow()

    init {
        _state.value = _state.value.copy(tasks = initialTasks, isLoading = false)
    }

    fun processIntent(intent: TaskIntent) {
        when (intent) {
            is TaskIntent.AddTask -> {
                val newTasks = _state.value.tasks + intent.task
                _state.value = _state.value.copy(tasks = newTasks)
            }
            is TaskIntent.ToggleTask -> {
                val newTasks = _state.value.tasks.map { task ->
                    if (task.id == intent.taskId) {
                        task.copy(isCompleted = !task.isCompleted)
                    } else {
                        task
                    }
                }
                _state.value = _state.value.copy(tasks = newTasks)
            }

        }
    }
}