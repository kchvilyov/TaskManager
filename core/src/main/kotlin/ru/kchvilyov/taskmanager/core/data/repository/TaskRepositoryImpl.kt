package ru.kchvilyov.taskmanager.core.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.kchvilyov.taskmanager.core.data.source.local.TasksTable
import ru.kchvilyov.taskmanager.core.domain.Task
import ru.kchvilyov.taskmanager.core.domain.TaskRepository
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.sql.DataSource

class TaskRepositoryImpl(dataSource: DataSource) : TaskRepository {

    init {
        // Подключаем Exposed к DataSource
        Database.connect(dataSource)
        // Создаём таблицу при инициализации
        transaction {
            SchemaUtils.create(TasksTable)
        }
    }

    override suspend fun getAllTasks(): List<Task> = withContext(Dispatchers.IO) {
        transaction {
            TasksTable.selectAll().map { rowToTask(it) }
        }
    }

    override suspend fun getTaskById(id: Long): Task? = withContext(Dispatchers.IO) {
        transaction {
            TasksTable
                .select { TasksTable.id eq id }
                .singleOrNull()
                ?.let { rowToTask(it) }
        }
    }

    override suspend fun insertTask(task: Task): Task = withContext(Dispatchers.IO) {
        transaction {
            val id = TasksTable.insertAndGetId {
                it[TasksTable.title] = task.title
                it[TasksTable.description] = task.description
                it[TasksTable.isCompleted] = task.isCompleted
            }
            task.copy(id = id.value)
        }
    }

    override suspend fun updateTask(task: Task): Boolean = withContext(Dispatchers.IO) {
        transaction {
            TasksTable.update({ TasksTable.id eq task.id }) {
                it[TasksTable.title] = task.title
                it[TasksTable.description] = task.description
                it[TasksTable.isCompleted] = task.isCompleted
            } > 0
        }
    }

    override suspend fun deleteTask(id: Long): Boolean = withContext(Dispatchers.IO) {
        transaction {
            TasksTable.deleteWhere { TasksTable.id eq id } > 0
        }
    }

    private fun rowToTask(row: ResultRow): Task = Task(
        id = row[TasksTable.id].value,
        title = row[TasksTable.title],
        description = row[TasksTable.description] ?: "",
        isCompleted = row[TasksTable.isCompleted],
        createdAt = (row[TasksTable.createdAt] as? LocalDateTime)
            ?.atZone(ZoneOffset.UTC)
            ?.toInstant()
            ?: Instant.now()
    )

    // ✅ Реализация метода для тестов
    override suspend fun clearTasksForTesting() {
        transaction {
            TasksTable.deleteWhere { TasksTable.id greaterEq 0 }
        }
    }
}