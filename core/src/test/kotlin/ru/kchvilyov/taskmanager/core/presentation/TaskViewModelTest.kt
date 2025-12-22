package ru.kchvilyov.taskmanager.core.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.kchvilyov.taskmanager.ru.kchvilyov.taskmanager.core.di.AppContainer
import ru.kchvilyov.taskmanager.ru.kchvilyov.taskmanager.core.domain.Task
import ru.kchvilyov.taskmanager.ru.kchvilyov.taskmanager.core.presentation.TaskIntent
import ru.kchvilyov.taskmanager.ru.kchvilyov.taskmanager.core.presentation.TaskState
import ru.kchvilyov.taskmanager.ru.kchvilyov.taskmanager.core.presentation.TaskViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var container: AppContainer
    private lateinit var viewModel: TaskViewModel
    private lateinit var states: MutableList<TaskState>
    private lateinit var collectionJob: kotlinx.coroutines.Job

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        container = AppContainer()
        viewModel = container.taskViewModel
        states = mutableListOf()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        if (::collectionJob.isInitialized && collectionJob.isActive) {
            collectionJob.cancel()
        }
    }

    @Test
    fun `initial state loads tasks correctly`() = runTest {
        // Запускаем сбор состояний ДО любых действий
        collectionJob = launch {
            viewModel.state.toList(states)
        }

        // Даем время на инициализацию
        testDispatcher.scheduler.advanceUntilIdle()

        // Проверяем, что было хотя бы одно излучение
        assertTrue(states.isNotEmpty(), "State should emit at least once")

        val finalState = states.last()
        // Проверяем по последнему состоянию, что загрузка завершена
        assertFalse(finalState.isLoading)
        // Проверяем, что в списке 3 задачи
        assertEquals(3, finalState.tasks.size)
    }

    @Test
    fun `add task increases task count`() = runTest {
        collectionJob = launch {
            viewModel.state.toList(states)
        }
        //Выполняет поставленные в очередь задачи в указанном порядке,
        // продвигая виртуальное время по мере необходимости, пока не останется задач,
        // связанных с диспетчерами, подключенными к этому планировщику.
        testDispatcher.scheduler.advanceUntilIdle()
        // Начальное количество задач
        val initialCount = states.last().tasks.size
        // Производим действие добавления задачи
        val newTask = Task(4, "New Task", "Description", false, System.currentTimeMillis())
        viewModel.processIntent(TaskIntent.AddTask(newTask))
        //Выполняет поставленные в очередь задачи в указанном порядке
        testDispatcher.scheduler.advanceUntilIdle()
        // Проверяем, что количество задач увеличилось на 1
        val finalCount = states.last().tasks.size
        assertEquals(initialCount + 1, finalCount)
        // Проверяем, что добавленная задача появилась в списке
        assertTrue(states.last().tasks.any { it.id == 4 })
    }

    @Test
    fun `toggle task changes completion status`() = runTest {
        collectionJob = launch {
            viewModel.state.toList(states)
        }

        testDispatcher.scheduler.advanceUntilIdle()

        val initialTask = states.last().tasks.find { it.id == 1 }
        // Проверяем, что задача с id 1 существует
        assertNotNull(initialTask)
        val initialStatus = initialTask?.isCompleted

        viewModel.processIntent(TaskIntent.ToggleTask(1))

        testDispatcher.scheduler.advanceUntilIdle()

        val updatedTask = states.last().tasks.find { it.id == 1 }
        assertNotNull(updatedTask)
        // Проверяем, что статус задачи изменился
        initialStatus?.let { assertEquals(!it, updatedTask?.isCompleted) }
    }
}