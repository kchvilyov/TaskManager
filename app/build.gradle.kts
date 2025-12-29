plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinPluginSerialization)
    application
}

dependencies {
    implementation(project(":core"))
    implementation(libs.koin.core)

    // ✅ Только exposed-kotlin-datetime, НЕ java.time
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)

    implementation(libs.hikariCP)
    runtimeOnly(libs.postgresql.driver)
}

application {
    mainClass = "ru.kchvilyov.app.AppKt"
}