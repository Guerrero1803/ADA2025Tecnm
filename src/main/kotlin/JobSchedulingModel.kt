package org.example

// Clase que implementa el algoritmo greedy para el problema de Job Scheduling
class JobSchedulingModel {

    // Devuelve el índice del último trabajo que no se solapa con el trabajo actual
    private fun findLastNonConflicting(jobs: List<Job>, index: Int): Int {
        for (i in index - 1 downTo 0) {
            if (jobs[i].endTime <= jobs[index].startTime) {
                return i
            }
        }
        return -1
    }

    fun solveJobScheduling(jobs: List<Job>): Pair<Int, List<Job>> {
        if (jobs.isEmpty()) return Pair(0, emptyList())

        // 1. Ordenar por tiempo de fin
        val sortedJobs = jobs.sortedBy { it.endTime }
        val n = sortedJobs.size

        // 2. dp[i] almacenará el beneficio máximo hasta el trabajo i
        val dp = IntArray(n)
        val selectedIndices = IntArray(n) { -1 }

        dp[0] = sortedJobs[0].profit

        for (i in 1 until n) {
            val inclProfit = sortedJobs[i].profit
            val lastIndex = findLastNonConflicting(sortedJobs, i)
            val profitWith = inclProfit + if (lastIndex != -1) dp[lastIndex] else 0
            val profitWithout = dp[i - 1]

            if (profitWith > profitWithout) {
                dp[i] = profitWith
                selectedIndices[i] = lastIndex
            } else {
                dp[i] = profitWithout
                selectedIndices[i] = -2 // indicamos que no se eligió este
            }
        }

        // 3. Reconstruir los trabajos seleccionados
        val selectedJobs = mutableListOf<Job>()
        var i = n - 1
        while (i >= 0) {
            if (selectedIndices[i] == -2) {
                i--
            } else {
                selectedJobs.add(sortedJobs[i])
                i = selectedIndices[i]
            }
        }

        selectedJobs.reverse()
        return Pair(dp[n - 1], selectedJobs)
    }
}
