package ru.kchvilyov.taskmanager.core.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.kchvilyov.taskmanager.core.di.AppContainer
import ru.kchvilyov.taskmanager.core.domain.Task
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var container: AppContainer
    private lateinit var viewModel: TaskViewModel

    @BeforeEach
    fun setUp() = runTest {
        Dispatchers.setMain(testDispatcher)
        container = AppContainer()
        viewModel = container.taskViewModel

        // Очищаем таблицу перед каждым тестом
        container.taskRepository.clearTasksForTesting()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state loads tasks correctly`() = runTest {
        val states = mutableListOf<TaskState>()

        // Собираем только первые 2 эмита (или сколько нужно)
        val job = launch {
            viewModel.state.take(2).collect { states.add(it) }
        }

        testDispatcher.scheduler.advanceUntilIdle()

        // Проверяем, что было хотя бы одно состояние
        assertTrue(states.isNotEmpty(), "State should emit at least once")
        val finalState = states.last()
        assertFalse(finalState.isLoading)
        assertEquals(0, finalState.tasks.size) // ✅ ожидаем 0

        job.cancel() // ⚠️ Обязательно отменяем!
    }

    @Test
    fun `add task increases task count`() = runTest {
        val states = mutableListOf<TaskState>()
        val job = launch {
            viewModel.state.take(3).collect { states.add(it) }
        }

        testDispatcher.scheduler.advanceUntilIdle()
        val initialCount = states.last().tasks.size

        val newTask = Task(
            id = 0L, // ❌ Не важно — будет перезаписан
            title = "New Task",
            description = "Description",
            isCompleted = false,
            createdAt = Instant.now()
        )
        viewModel.processIntent(TaskIntent.AddTask(newTask))

        testDispatcher.scheduler.advanceUntilIdle()

        val finalCount = states.last().tasks.size
        assertEquals(initialCount + 1, finalCount)
        assertTrue(states.last().tasks.any { it.title == "New Task" }) // ✅ Проверяем по названию

        job.cancel()
    }

    @Test
    fun `toggle task changes completion status`() = runTest {
        val states = mutableListOf<TaskState>()
        val job = launch {
            viewModel.state.take(3).collect { states.add(it) }
        }

        testDispatcher.scheduler.advanceUntilIdle()

        val testTask = Task(
            id = 1L,
            title = "Test Task",
            description = "",
            isCompleted = false,
            createdAt = Instant.now()
        )
        viewModel.processIntent(TaskIntent.AddTask(testTask))

        testDispatcher.scheduler.advanceUntilIdle()

        val initialTask = states.last().tasks.find { it.id == 1L }
        assertNotNull(initialTask)
        // Присваиваем в отдельную переменную после проверки
        assertFalse(initialTask!!.isCompleted)

        viewModel.processIntent(TaskIntent.ToggleTask(1L))

        testDispatcher.scheduler.advanceUntilIdle()

        val updatedTask = states.last().tasks.find { it.id == 1L }
        assertNotNull(updatedTask)
        // Здесь тоже
        assertTrue(updatedTask!!.isCompleted)

        job.cancel()
    }
}