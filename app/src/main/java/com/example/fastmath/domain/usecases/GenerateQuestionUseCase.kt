package com.example.fastmath.domain.usecases

import com.example.fastmath.domain.repository.GameRepository
import com.example.fastmath.domain.entity.Question

class GenerateQuestionUseCase(private val repository: GameRepository) {

    operator fun invoke(maxTotalValue: Int): Question {
        return repository.generateQuestion(maxTotalValue, COUNT_OF_OPTIONS)
    }

    companion object {
        private const val COUNT_OF_OPTIONS = 6
    }
}