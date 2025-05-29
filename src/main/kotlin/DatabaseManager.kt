package org.example

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DatabaseManager {
    private val url = "jdbc:mysql://localhost:3306/school_organigram"
    private val user = "root"
    private val password = "" // Reemplaza con tu contraseña de MySQL

    private fun getConnection(): Connection {
        return DriverManager.getConnection(url, user, password)
    }

    fun addStaff(name: String, ranking: String, supervisorId: Int?): Staff? {
        val sql = "INSERT INTO staff (name, ranking, supervisor_id) VALUES (?, ?, ?)"
        try {
            getConnection().use { conn ->
                conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS).use { stmt ->
                    stmt.setString(1, name)
                    stmt.setString(2, ranking)
                    if (supervisorId != null) {
                        stmt.setInt(3, supervisorId)
                    } else {
                        stmt.setNull(3, java.sql.Types.INTEGER)
                    }
                    stmt.executeUpdate()
                    val rs = stmt.generatedKeys
                    if (rs.next()) {
                        val newStaff = Staff(rs.getInt(1), name, ranking, supervisorId)
                        println("Añadido: $newStaff")
                        return newStaff
                    }
                }
            }
        } catch (e: SQLException) {
            println("Error al añadir personal: ${e.message}")
        }
        return null
    }

    fun getAllStaff(): List<Staff> {
        val staffList = mutableListOf<Staff>()
        val sql = "SELECT id, name, ranking, supervisor_id FROM staff"
        try {
            getConnection().use { conn ->
                conn.createStatement().use { stmt ->
                    val rs = stmt.executeQuery(sql)
                    while (rs.next()) {
                        val staff = Staff(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("ranking"),
                            rs.getInt("supervisor_id").takeIf { !rs.wasNull() }
                        )
                        staffList.add(staff)
                    }
                    println("Personal obtenido: $staffList")
                }
            }
        } catch (e: SQLException) {
            println("Error al obtener personal: ${e.message}")
        }
        return staffList
    }

    fun buildTree(): TreeNode? {
        val staffList = getAllStaff()
        if (staffList.isEmpty()) {
            println("No hay personal para construir el árbol")
            return null
        }

        val nodeMap = staffList.associate { it.id to TreeNode(it) }
        var root: TreeNode? = null
        for (staff in staffList) {
            val node = nodeMap[staff.id]!!
            if (staff.supervisorId == null && staff.ranking == "Director") {
                root = node
                println("Raíz encontrada: ${staff.name} (ID: ${staff.id})")
            } else if (staff.supervisorId != null) {
                nodeMap[staff.supervisorId]?.children?.add(node)
                    ?: println("Supervisor ID ${staff.supervisorId} no encontrado para ${staff.name}")
            }
        }
        println("Árbol construido con raíz: ${root?.staff?.name}")
        return root
    }

    fun clearStaff() {
        val sql = "DELETE FROM staff"
        try {
            getConnection().use { conn ->
                conn.createStatement().use { stmt ->
                    stmt.executeUpdate(sql)
                    println("Personal eliminado")
                }
            }
        } catch (e: SQLException) {
            println("Error al limpiar personal: ${e.message}")
        }
    }
}