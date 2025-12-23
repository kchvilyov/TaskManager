package ru.kchvilyov.taskmanager.core.presentation

import ru.kchvilyov.taskmanager.core.domain.Task

sealed interface TaskIntent {
    data class ToggleTask(val taskId: Int) : TaskIntent
    data class AddTask(val task: Task) : TaskIntent
}