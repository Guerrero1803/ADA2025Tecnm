package org.example

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf


// ViewModel que gestiona la lógica de negocio y el estado para la UI
class KnapsackViewModel {

    // Estado para la lista de ítems
    val items: MutableState<List<Item>> = mutableStateOf(emptyList())
    // Estado para la capacidad de la mochila
    val capacity: MutableState<Int> = mutableStateOf(0)
    // Estado para el resultado (valor máximo y ítems seleccionados)
    val result: MutableState<Pair<Int, List<Item>>?> = mutableStateOf(null)
    // Estado para mensajes de error
    val errorMessage: MutableState<String?> = mutableStateOf(null)

    private val knapsackModel = KnapsackModel()

    // Agrega un nuevo ítem a la lista
    fun addItem(weight: Int, value: Int) {
        if (weight > 0 && value > 0) {
            items.value = items.value + Item(weight, value)
            errorMessage.value = null
        } else {
            errorMessage.value = "El peso y el valor deben ser mayores que 0"
        }
    }

    // Actualiza la capacidad de la mochila
    fun updateCapacity(newCapacity: Int) {
        if (newCapacity >= 0) {
            capacity.value = newCapacity
            errorMessage.value = null
        } else {
            errorMessage.value = "La capacidad debe ser no negativa"
        }
    }

    // Resuelve el problema de la mochila
    fun solve() {
        if (items.value.isEmpty()) {
            errorMessage.value = "Agrega al menos un ítem"
            return
        }
        if (capacity.value <= 0) {
            errorMessage.value = "La capacidad debe ser mayor que 0"
            return
        }
        val res = knapsackModel.solveKnapsackGreedy(items.value, capacity.value)
        println("Solve result (Greedy): $res")
        result.value = res
        errorMessage.value = null
    }


    // Limpia todos los datos
    fun clear() {
        items.value = emptyList()
        capacity.value = 0
        result.value = null
        errorMessage.value = null
    }
}