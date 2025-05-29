package org.example

// Nodo del árbol que representa un miembro del personal y sus subordinados
data class TreeNode(
    val staff: Staff,
    val children: MutableList<TreeNode> = mutableListOf()
)