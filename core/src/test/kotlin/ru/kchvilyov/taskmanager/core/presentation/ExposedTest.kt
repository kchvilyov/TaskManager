package ru.kchvilyov.taskmanager.core.presentation

import ru.kchvilyov.taskmanager.core.domain.Task
import java.time.Instant
import kotlin.test.Test

class ExposedTest {
    @Test
    fun testTaskCreation() {
        val task = Task(
            id = 1,
            title = "Test",
            description = "Description",
            isCompleted = false,
            createdAt = Instant.now()
        )
        println(task)
    }
}