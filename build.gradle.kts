plugins {
    kotlin("jvm") version "2.1.20"
    id("org.jetbrains.compose") version "1.6.11"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation("org.jetbrains.compose.runtime:runtime:1.6.11")
    implementation("org.jetbrains.compose.foundation:foundation:1.6.11")
    implementation("org.jetbrains.compose.material:material:1.6.11")
    implementation("org.jetbrains.compose.material3:material3:1.6.11")
    implementation("org.jetbrains.compose.ui:ui:1.6.11")
    implementation("org.jetbrains.compose.desktop:desktop:1.6.11")
    implementation("org.jetbrains.compose.desktop:desktop-jvm-windows-x64:1.6.11") // Añadido para Skiko Windows x64
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.1")
    implementation("mysql:mysql-connector-java:8.0.33")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

compose.desktop {
    application {
        mainClass = "org.example.MainKt"
    }
}