plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    // Apply Kotlin Serialization plugin from `gradle/libs.versions.toml`.
    alias(libs.plugins.kotlinPluginSerialization)
}

dependencies {
    // Apply the kotlinx bundle of dependencies from the version catalog (`gradle/libs.versions.toml`).
    implementation(libs.bundles.kotlinxEcosystem)
    implementation(libs.protolite.well.known.types)
//    implementation(libs.protolite.well.known.types)
    testImplementation(kotlin("test"))
    implementation(libs.koin.core)
    implementation(libs.hikariCP)
    implementation(libs.postgresql.driver)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
}