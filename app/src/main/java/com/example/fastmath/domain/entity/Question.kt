package com.example.fastmath.domain.entity

data class Question(
    val total: Int,
    val visibleNumber: Int,
    val options: List<Int>
)