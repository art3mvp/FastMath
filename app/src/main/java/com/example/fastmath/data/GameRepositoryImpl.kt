package com.example.fastmath.data

import com.example.fastmath.domain.entity.GameSettings
import com.example.fastmath.domain.entity.Level
import com.example.fastmath.domain.entity.Question
import com.example.fastmath.domain.repository.GameRepository
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {

    private const val MIN_TOTAL_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1


    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            Level.TEST -> GameSettings(
                10,
                2,
                50,
                10
            )

            Level.EASY -> GameSettings(
                15,
                10,
                50,
                30
            )

            Level.MEDIUM -> GameSettings(
                30,
                15,
                70,
                30
            )

            Level.HARD -> GameSettings(
                50,
                15,
                90,
                30
            )
        }
    }

    override fun generateQuestion(maxTotalValue: Int, countOfOptions: Int): Question {

        val options = HashSet<Int>()

        val total = Random.nextInt(MIN_TOTAL_VALUE, maxTotalValue + 1)
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE, total)
        val rightAnswer = total - visibleNumber

        options.add(rightAnswer)

        val from = max(rightAnswer - countOfOptions, MIN_ANSWER_VALUE)
        val to = min(maxTotalValue, rightAnswer + countOfOptions)

        while (options.size < countOfOptions) {
            options.add(Random.nextInt(from, to))
        }

        return Question(total, visibleNumber, options.toList())
    }
}