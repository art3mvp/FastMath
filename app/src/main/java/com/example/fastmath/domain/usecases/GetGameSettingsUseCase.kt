package com.example.fastmath.domain.usecases

import com.example.fastmath.domain.repository.GameRepository
import com.example.fastmath.domain.entity.GameSettings
import com.example.fastmath.domain.entity.Level

class GetGameSettingsUseCase(private val repository: GameRepository) {
    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}

