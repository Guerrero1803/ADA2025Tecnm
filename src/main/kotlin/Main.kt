package org.example

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.WindowState

fun main(): Unit = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Knapsack Problem",
        state = WindowState(width = 800.dp, height = 600.dp)
    ) {
        MaterialTheme {
            KnapsackScreen()
        }
    }
}

@Composable
fun KnapsackScreen(viewModel: KnapsackViewModel = KnapsackViewModel()) {
    val viewModel = remember { KnapsackViewModel() }

    var weightInput by remember { mutableStateOf("") }
    var valueInput by remember { mutableStateOf("") }
    var capacityInput by remember { mutableStateOf("") }

    val items by viewModel.items
    val capacity by viewModel.capacity
    val result by viewModel.result
    val errorMessage by viewModel.errorMessage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Entrada para peso y valor del ítem
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = weightInput,
                onValueChange = { weightInput = it },
                label = { Text("Peso") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = valueInput,
                onValueChange = { valueInput = it },
                label = { Text("Valor") },
                modifier = Modifier.weight(1f)
            )
        }

        Button(
            onClick = {
                val weight = weightInput.toIntOrNull() ?: 0
                val value = valueInput.toIntOrNull() ?: 0
                viewModel.addItem(weight, value)
                weightInput = ""
                valueInput = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Ítem")
        }

        // Entrada para la capacidad
        OutlinedTextField(
            value = capacityInput,
            onValueChange = { capacityInput = it },
            label = { Text("Capacidad de la mochila") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val cap = capacityInput.toIntOrNull() ?: 0
                viewModel.updateCapacity(cap)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Establecer Capacidad")
        }

        // Botones para resolver y limpiar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.solve() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Resolver")
            }
            Button(
                onClick = { viewModel.clear() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Limpiar")
            }
        }

        // Mostrar mensaje de error si existe
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )
        }
        val items by viewModel.items

        // Mostrar lista de ítems
        Text("Ítems:", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(items) { item ->
                Text("Peso: ${item.weight}, Valor: ${item.value}")
            }
        }

        // Mostrar resultado si existe
        result?.let { (maxValue, selectedItems) ->
            Text(
                "Valor Máximo: $maxValue",
                style = MaterialTheme.typography.titleMedium
            )
            Text("Ítems Seleccionados:", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(selectedItems) { item ->
                    Text("Peso: ${item.weight}, Valor: ${item.value}")
                }
            }
        }
    }
}
