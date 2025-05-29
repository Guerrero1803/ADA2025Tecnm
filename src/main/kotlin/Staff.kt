package org.example

// Clase de datos que representa a un miembro del personal
data class Staff(
    val id: Int,
    val name: String,
    val ranking: String,
    val supervisorId: Int?
)