package com.example.fastmath.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fastmath.R
import com.example.fastmath.data.GameRepositoryImpl
import com.example.fastmath.domain.entity.GameResult
import com.example.fastmath.domain.entity.GameSettings
import com.example.fastmath.domain.entity.Level
import com.example.fastmath.domain.entity.Question
import com.example.fastmath.domain.usecases.GenerateQuestionUseCase
import com.example.fastmath.domain.usecases.GetGameSettingsUseCase

class GameViewModel(
    private val application: Application,
    private val level: Level,
) : ViewModel() {


    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private lateinit var gameSettings: GameSettings

    private val _getQuestion = MutableLiveData<Question>()
    val getQuestion: LiveData<Question>
        get() = _getQuestion

    private val _enoughScore = MutableLiveData<Boolean>()
    val enoughScore: LiveData<Boolean>
        get() = _enoughScore

    private val _enoughPercentage = MutableLiveData<Boolean>()
    val enoughPercentage: LiveData<Boolean>
        get() = _enoughPercentage

    private val _userPercentage = MutableLiveData<Int>()
    val userPercentage: LiveData<Int>
        get() = _userPercentage


    private val _currentProgress = MutableLiveData<String>()
    val currentProgress: LiveData<String>
        get() = _currentProgress

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private var timer: CountDownTimer? = null

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent



    init {
        startGame()
        startTimer()
    }

    private var countOfCorrectAnswers = 0
    private var countOfQuestions = 0

    private fun startGame() {
        getGameSettings()
        generateQuestion()
        updateProgress()
    }

    private fun getGameSettings() {
        gameSettings = getGameSettingsUseCase.invoke(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }

    private fun generateQuestion() {
        _getQuestion.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    fun checkAnswer(answer: Int) {
        if (answer == _getQuestion.value?.rightAnswer) {
            countOfCorrectAnswers++
        }
        countOfQuestions++
        updateProgress()
        generateQuestion()
    }

    private fun updateProgress() {
        val percent = getPercentage()
        _userPercentage.value = percent
        _currentProgress.value = getCurrentProgress()
        _enoughScore.value = countOfCorrectAnswers >= gameSettings.minCountOfRightAnswers
        _enoughPercentage.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun getPercentage(): Int {
        return (countOfCorrectAnswers / countOfQuestions.toDouble() * 100).toInt()
    }

    private fun getCurrentProgress(): String {
        val template = ContextCompat.getString(application, R.string.progress_answers)
        return String.format(template, countOfCorrectAnswers, gameSettings.minCountOfRightAnswers)
    }

    private fun startTimer() {
        timer = object : CountDownTimer(secondsToMillis(), SECONDS_IN_MILLIS) {
            override fun onTick(millis: Long) {
                _formattedTime.value = getFormattedTime(millis)
            }

            override fun onFinish() {
                gameFinish()
            }
        }
        timer?.start()
    }

    private fun getFormattedTime(millis: Long): String {
        val seconds = millis / SECONDS_IN_MILLIS
        val minutes = seconds / SECONDS_IN_MINUTE
        val restSeconds = seconds % SECONDS_IN_MINUTE
        return String.format(TEMPLATE_TIME, minutes, restSeconds)
    }

    private fun gameFinish() {
        _gameResult.value = GameResult(
            enoughPercentage.value == true && enoughScore.value == true,
            countOfCorrectAnswers,
            countOfQuestions,
            gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    private fun secondsToMillis(): Long {
        return gameSettings.gameTimeInSeconds * SECONDS_IN_MILLIS
    }

    companion object {
        private const val SECONDS_IN_MILLIS = 1000L
        private const val TEMPLATE_TIME = "%02d:%02d"
        private const val SECONDS_IN_MINUTE = 60
    }
}