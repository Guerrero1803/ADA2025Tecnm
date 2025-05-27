package org.example

// Clase que implementa el algoritmo de la mochila 0/1 usando programación dinámica
class KnapsackModel {

    // Resuelve el problema de la mochila para una lista de ítems y una capacidad dada
    // Retorna el valor máximo y la lista de ítems seleccionados
    fun solveKnapsack(items: List<Item>, capacity: Int): Pair<Int, List<Item>> {
        val n = items.size
        // Crea una matriz DP para almacenar los valores máximos para subproblemas
        val dp = Array(n + 1) { IntArray(capacity + 1) }

        // Llena la matriz DP
        for (i in 0..n) {
            for (w in 0..capacity) {
                if (i == 0 || w == 0) {
                    dp[i][w] = 0 // Caso base: sin ítems o sin capacidad
                } else if (items[i - 1].weight <= w) {
                    // Decide entre incluir el ítem actual o no
                    dp[i][w] = maxOf(
                        items[i - 1].value + dp[i - 1][w - items[i - 1].weight],
                        dp[i - 1][w]
                    )
                } else {
                    // No se puede incluir el ítem actual por exceso de peso
                    dp[i][w] = dp[i - 1][w]
                }
            }
        }

        // Reconstruye la lista de ítems seleccionados con condición robusta
        val selectedItems = mutableListOf<Item>()
        var w = capacity
        for (i in n downTo 1) {
            if (w >= items[i - 1].weight &&
                dp[i][w] == items[i - 1].value + dp[i - 1][w - items[i - 1].weight]) {
                selectedItems.add(items[i - 1])
                w -= items[i - 1].weight
            }
        }

        // Retorna el valor máximo y la lista de ítems seleccionados
        return Pair(dp[n][capacity], selectedItems.reversed())
    }
}
