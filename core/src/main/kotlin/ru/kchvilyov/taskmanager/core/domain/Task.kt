package ru.kchvilyov.taskmanager.core.domain

import java.time.Instant

data class Task(
    val id: Long,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val createdAt: Instant
)