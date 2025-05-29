package org.example

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class OrganigramViewModel {
    private val dbManager = DatabaseManager()

    private val _treeRoot: MutableState<TreeNode?> = mutableStateOf(null)
    val treeRoot: State<TreeNode?> = _treeRoot

    private val _staff: MutableState<List<Staff>> = mutableStateOf(emptyList())
    val staff: State<List<Staff>> = _staff

    private val _errorMessage: MutableState<String?> = mutableStateOf(null)
    val errorMessage: State<String?> = _errorMessage

    init {
        refreshData()
    }

    fun addStaff(name: String, rank: String, supervisorId: Int?) {
        if (name.isBlank()) {
            _errorMessage.value = "El nombre no puede estar vacío"
            return
        }
        if (rank != "Director" && supervisorId == null) {
            _errorMessage.value = "Los no directores deben tener un supervisor"
            return
        }
        if (supervisorId != null && !staff.value.any { it.id == supervisorId }) {
            _errorMessage.value = "Supervisor no válido (ID $supervisorId no existe)"
            return
        }
        dbManager.addStaff(name, rank, supervisorId)?.let {
            refreshData()
            _errorMessage.value = null
        } ?: run {
            _errorMessage.value = "Error al añadir personal"
        }
    }

    fun clearStaff() {
        dbManager.clearStaff()
        refreshData()
        _errorMessage.value = null
    }

    private fun refreshData() {
        _staff.value = dbManager.getAllStaff()
        _treeRoot.value = dbManager.buildTree()
    }
}