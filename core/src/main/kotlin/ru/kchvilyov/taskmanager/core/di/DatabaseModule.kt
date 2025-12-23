package ru.kchvilyov.taskmanager.core.di

import org.koin.dsl.module
import javax.sql.DataSource
import com.zaxxer.hikari.HikariDataSource

val databaseModule = module {
    single<DataSource> {
        HikariDataSource().apply {
//            jdbcUrl = "jdbc:postgresql://localhost:5432/taskmanager"
            jdbcUrl = "jdbc:h2:mem:taskmanager;DB_CLOSE_DELAY=-1"
            username = "postgres"
            password = "password"
            maximumPoolSize = 10
            minimumIdle = 2
//            driverClassName = "org.postgresql.Driver"
            driverClassName = "org.h2.Driver"        }
    }
}