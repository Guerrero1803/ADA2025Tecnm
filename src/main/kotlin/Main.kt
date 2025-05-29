package org.example

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.WindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Organigrama Escolar",
        state = WindowState(width = 1000.dp, height = 800.dp)
    ) {
        MaterialTheme {
            OrganigramScreen()
        }
    }
}

@Composable
fun OrganigramScreen() {
    val viewModel = remember { OrganigramViewModel() }
    val treeRoot by viewModel.treeRoot
    val staff by viewModel.staff
    val errorMessage by viewModel.errorMessage

    var nameInput by remember { mutableStateOf("") }
    var rankInput by remember { mutableStateOf("Director") }
    var supervisorIdInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Añadir Personal", style = MaterialTheme.typography.titleLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                label = { Text("Nombre") },
                modifier = Modifier.weight(1f)
            )
            DropdownMenuRank(
                selectedRank = rankInput,
                onRankSelected = { rankInput = it },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = supervisorIdInput,
                onValueChange = { if (it.all { char -> char.isDigit() } || it.isEmpty()) supervisorIdInput = it },
                label = { Text("ID Supervisor${if (rankInput != "Director") " (requerido)" else " (opcional)"}") },
                modifier = Modifier.weight(1f),
                enabled = rankInput != "Director" || supervisorIdInput.isNotEmpty()
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val supervisorId = if (rankInput == "Director" && supervisorIdInput.isEmpty()) null else supervisorIdInput.toIntOrNull()
                    viewModel.addStaff(nameInput, rankInput, supervisorId)
                    nameInput = ""
                    supervisorIdInput = ""
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Añadir Personal")
            }
            Button(
                onClick = { viewModel.clearStaff() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Limpiar")
            }
        }

        errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Text("Organigrama", style = MaterialTheme.typography.titleLarge)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .border(1.dp, Color.Gray)
                .padding(16.dp)
        ) {
            treeRoot?.let { root ->
                OrganigramTree(root)
            } ?: Text("No hay personal registrado")
        }

        Text("Personal Registrado (ID para Supervisor):", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(staff) { person ->
                Text("ID: ${person.id}, Nombre: ${person.name}, Rango: ${person.ranking}, Supervisor ID: ${person.supervisorId ?: "Ninguno"}")
            }
        }
    }
}

@Composable
fun DropdownMenuRank(
    selectedRank: String,
    onRankSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val ranks = listOf("Director", "Subdirector", "Coordinador", "Docente", "Personal de Apoyo")
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedRank,
            onValueChange = {},
            label = { Text("Rango") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Text("▼")
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ranks.forEach { rank ->
                DropdownMenuItem(
                    text = { Text(rank) },
                    onClick = {
                        onRankSelected(rank)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun OrganigramTree(root: TreeNode) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StaffNode(node = root, depth = 0)
    }
}

@Composable
fun StaffNode(node: TreeNode, depth: Int) {
    println("Renderizando nodo: ${node.staff.name}, Hijos: ${node.children.size}")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(if (depth == 0) Color(0xFFE0F7FA) else Color(0xFFFFF3E0))
                .border(1.dp, Color.Black)
                .padding(8.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(node.staff.name, fontWeight = FontWeight.Bold)
                Text(node.staff.ranking, style = MaterialTheme.typography.bodySmall)
            }
        }

        if (node.children.isNotEmpty()) {
            // Línea vertical que conecta con los hijos
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(20.dp)
                    .background(Color.Black)
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                node.children.forEach { child ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)   // Cada hijo toma el mismo ancho
                            .padding(horizontal = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .height(2.dp)
                                .background(Color.Black)
                                .align(Alignment.CenterHorizontally)
                        )
                        StaffNode(node = child, depth = depth + 1)
                    }
                }
            }
        }
    }
}
