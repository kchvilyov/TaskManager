package ru.kchvilyov.taskmanager.core.data.source.local

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime

object TasksTable : LongIdTable("tasks") {
    val title = varchar("title", 255)
    val description = text("description").nullable()
    val isCompleted = bool("is_completed").default(false)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
}