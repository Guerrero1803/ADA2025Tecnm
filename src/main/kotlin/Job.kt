package org.example

// Clase de datos que representa un trabajo con tiempo de inicio, fin y beneficio
data class Job(
    val startTime: Int,  // Tiempo de inicio del trabajo
    val endTime: Int,    // Tiempo de finalización del trabajo
    val profit: Int      // Beneficio asociado al trabajo
)