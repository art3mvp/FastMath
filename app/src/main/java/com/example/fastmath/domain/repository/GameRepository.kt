package com.example.fastmath.domain.repository

import com.example.fastmath.domain.entity.GameSettings
import com.example.fastmath.domain.entity.Level
import com.example.fastmath.domain.entity.Question

interface GameRepository {

    fun getGameSettings(level: Level): GameSettings

    fun generateQuestion(
        maxTotalValue: Int,
        countOfOptions: Int
    ): Question
}