package org.example

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

// ViewModel que gestiona el estado y la lógica de negocio para la UI de escritorio
class JobSchedulingViewModel {

    // Estado reactivo privado para la lista de trabajos
    private val _jobs: MutableState<List<Job>> = mutableStateOf(emptyList())
    // Estado público para la lista de trabajos, observado por la UI
    val jobs: State<List<Job>> = _jobs

    // Estado reactivo privado para el resultado (beneficio máximo y trabajos seleccionados)
    private val _result: MutableState<Pair<Int, List<Job>>?> = mutableStateOf(null)
    // Estado público para el resultado, observado por la UI
    val result: State<Pair<Int, List<Job>>?> = _result

    // Estado reactivo privado para mensajes de error
    private val _errorMessage: MutableState<String?> = mutableStateOf(null)
    // Estado público para mensajes de error, observado por la UI
    val errorMessage: State<String?> = _errorMessage

    private val model = JobSchedulingModel()

    // Agrega un nuevo trabajo con validación de entradas
    // - startTime: Tiempo de inicio (no negativo)
    // - endTime: Tiempo de fin (>= startTime)
    // - profit: Beneficio (no negativo)
    fun addJob(startTime: Int, endTime: Int, profit: Int) {
        println("Intentando añadir: startTime=$startTime, endTime=$endTime, profit=$profit") // Depuración
        if (startTime >= 0 && endTime >= startTime && profit >= 0) {
            _jobs.value = _jobs.value + Job(startTime, endTime, profit)
            println("Trabajo añadido: ${_jobs.value}") // Depuración
            _errorMessage.value = null
        } else {
            _errorMessage.value = "Tiempo de inicio no negativo, fin >= inicio, beneficio no negativo"
            println("Error: Entradas inválidas") // Depuración
        }
    }

    // Establece un mensaje de error
    fun setErrorMessage(message: String?) {
        _errorMessage.value = message
        println("Mensaje de error establecido: $message") // Depuración
    }

    // Resuelve el problema de Job Scheduling usando el modelo
    fun solve() {
        println("Jobs en solve: ${_jobs.value}") // Depuración
        if (_jobs.value.isEmpty()) {
            _errorMessage.value = "Agrega al menos un trabajo"
            println("Error: No hay trabajos para resolver") // Depuración
            return
        }
        _result.value = model.solveJobScheduling(_jobs.value)
        println("Resultado: ${_result.value}") // Depuración
        _errorMessage.value = null
    }

    // Limpia todos los estados (trabajos, resultado, errores)
    fun clear() {
        _jobs.value = emptyList()
        _result.value = null
        _errorMessage.value = null
        println("Estados limpiados") // Depuración
    }
}