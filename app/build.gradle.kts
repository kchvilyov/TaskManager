plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    // Apply Kotlin Serialization plugin from `gradle/libs.versions.toml`.
    alias(libs.plugins.kotlinPluginSerialization)

    // Apply the Application plugin to add support for building an executable JVM application.
    application
}

dependencies {
    // Project "app" depends on project "core". (Project paths are separated with ":", so ":core" refers to the top-level "core" project.)
    implementation(project(":core"))
    // Koin для Dependency Injection
    implementation(libs.koin.core)

    // Exposed для работы с базой данных
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)

    // Пул соединений и драйвер БД (H2 для тестов/разработки, PostgreSQL для прода)
    implementation(libs.hikariCP)
    runtimeOnly(libs.postgresql) // или implementation(libs.h2) для тестов

//    // Kotlin Coroutines
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${libs.versions.kotlinx.coroutines.get()}")
//
//    // Тестирование
//    testImplementation(libs.koin.test)
//    testImplementation(libs.junit.jupiter)
//    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${libs.versions.kotlinx.coroutines.get()}")
}

application {
    // Define the Fully Qualified Name for the application main class
    // (Note that Kotlin compiles `App.kt` to a class with FQN `com.example.app.AppKt`.)
    mainClass = "ru.kchvilyov.app.AppKt"
}
