package org.example

// Clase que implementa el algoritmo de la mochila 0/1 usando programación dinámica
class KnapsackModel {

    // Algoritmo original con programación dinámica (0/1 Knapsack)
    fun solveKnapsack(items: List<Item>, capacity: Int): Pair<Int, List<Item>> {
        val n = items.size
        val dp = Array(n + 1) { IntArray(capacity + 1) }

        for (i in 0..n) {
            for (w in 0..capacity) {
                if (i == 0 || w == 0) {
                    dp[i][w] = 0
                } else if (items[i - 1].weight <= w) {
                    dp[i][w] = maxOf(
                        items[i - 1].value + dp[i - 1][w - items[i - 1].weight],
                        dp[i - 1][w]
                    )
                } else {
                    dp[i][w] = dp[i - 1][w]
                }
            }
        }

        val selectedItems = mutableListOf<Item>()
        var w = capacity
        for (i in n downTo 1) {
            if (w >= items[i - 1].weight &&
                dp[i][w] == items[i - 1].value + dp[i - 1][w - items[i - 1].weight]) {
                selectedItems.add(items[i - 1])
                w -= items[i - 1].weight
            }
        }

        return Pair(dp[n][capacity], selectedItems.reversed())
    }

    // Nuevo algoritmo greedy basado en valor/peso
    fun solveKnapsackGreedy(items: List<Item>, capacity: Int): Pair<Int, List<Item>> {
        val sortedItems = items.sortedByDescending { it.value.toDouble() / it.weight }
        val selectedItems = mutableListOf<Item>()
        var remainingCapacity = capacity
        var totalValue = 0

        for (item in sortedItems) {
            if (item.weight <= remainingCapacity) {
                selectedItems.add(item)
                remainingCapacity -= item.weight
                totalValue += item.value
            }
        }

        return Pair(totalValue, selectedItems)
    }
}
