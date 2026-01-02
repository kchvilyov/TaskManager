package ru.kchvilyov.taskmanager.core.di

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.runBlocking
import ru.kchvilyov.taskmanager.core.data.repository.TaskRepositoryImpl
import ru.kchvilyov.taskmanager.core.domain.TaskRepository
import ru.kchvilyov.taskmanager.core.presentation.TaskViewModel
import javax.sql.DataSource

class AppContainer {
    private val dataSource: DataSource = createHikariDataSource()
    val taskRepository: TaskRepository = TaskRepositoryImpl(dataSource)
    val taskViewModel: TaskViewModel = TaskViewModel(taskRepository)

    init {
        // ✅ Используем runBlocking для вызова suspend-функций
        runBlocking {
//            taskRepository.insertTask(
//                Task(1L, "Learn Kotlin", "Study coroutines and flows", false, Instant.now().minusSeconds(1000))
//            )
//            taskRepository.insertTask(
//                Task(2L, "Write Code", "Implement clean architecture", false, Instant.now().minusSeconds(900))
//            )
//            taskRepository.insertTask(
//                Task(3L, "Review PR", "Check team member's code", true, Instant.now().minusSeconds(800))
//            )
        }
    }

    private fun createHikariDataSource(): HikariDataSource {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
            driverClassName = "org.h2.Driver"
            username = "sa"
            password = ""
            maximumPoolSize = 10
        }
        return HikariDataSource(config)
    }
}