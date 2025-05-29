package org.example

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.WindowState

// Punto de entrada para la aplicación de escritorio
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Job Scheduling",
        state = WindowState(width = 800.dp, height = 600.dp)
    ) {
        MaterialTheme {
            JobSchedulingScreen()
        }
    }
}

// Composable principal que define la interfaz de usuario
@Composable
fun JobSchedulingScreen() {
    // Usar remember para mantener una única instancia del ViewModel
    val viewModel = remember { JobSchedulingViewModel() }

    // Estados locales para los campos de entrada
    var startTimeInput by remember { mutableStateOf("") }
    var endTimeInput by remember { mutableStateOf("") }
    var profitInput by remember { mutableStateOf("") }

    // Observar estados del ViewModel reactivamente
    val jobs by viewModel.jobs
    val result by viewModel.result
    val errorMessage by viewModel.errorMessage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Sección de entrada para tiempo de inicio, fin y beneficio
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = startTimeInput,
                onValueChange = { if (it.all { char -> char.isDigit() || char == '-' }) startTimeInput = it },
                label = { Text("Tiempo de Inicio") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = endTimeInput,
                onValueChange = { if (it.all { char -> char.isDigit() || char == '-' }) endTimeInput = it },
                label = { Text("Tiempo de Fin") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = profitInput,
                onValueChange = { if (it.all { char -> char.isDigit() || char == '-' }) profitInput = it },
                label = { Text("Beneficio") },
                modifier = Modifier.weight(1f)
            )
        }
        // Botón para agregar un trabajo
        Button(
            onClick = {
                val startTime = startTimeInput.toIntOrNull()
                val endTime = endTimeInput.toIntOrNull()
                val profit = profitInput.toIntOrNull()
                if (startTime != null && endTime != null && profit != null) {
                    viewModel.addJob(startTime, endTime, profit)
                    startTimeInput = ""
                    endTimeInput = ""
                    profitInput = ""
                } else {
                    viewModel.setErrorMessage("Ingresa valores numéricos válidos")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Trabajo")
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
        errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Depuración: Mostrar estado de jobs
        Text("Jobs en UI: ${jobs.joinToString()}")

        // Mostrar número de trabajos para depuración
        Text("Número de trabajos: ${jobs.size}")

        // Mostrar lista de trabajos ingresados
        Text("Trabajos:", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            items(jobs) { job ->
                Text("Inicio: ${job.startTime}, Fin: ${job.endTime}, Beneficio: ${job.profit}")
            }
        }

        // Mostrar resultado (beneficio máximo y trabajos seleccionados)
        result?.let { (totalProfit, selectedJobs) ->
            Text(
                "Beneficio Máximo: $totalProfit",
                style = MaterialTheme.typography.titleMedium
            )
            Text("Trabajos Seleccionados:", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(selectedJobs) { job ->
                    Text("Inicio: ${job.startTime}, Fin: ${job.endTime}, Beneficio: ${job.profit}")
                }
            }
        }
    }
}