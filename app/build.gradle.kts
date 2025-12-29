plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinPluginSerialization)
    application
}

dependencies {
    implementation(project(":core"))
    implementation(libs.koin.core)

    // Exposed — core, dao, jdbc, kotlin-datetime
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime) // ✅ Правильное имя

    // Пул соединений и драйвер БД
    implementation(libs.hikariCP)
    runtimeOnly(libs.postgresql.driver) // ✅ Исправлено: libs.postgresql.driver
}

application {
    mainClass = "ru.kchvilyov.app.AppKt"
}