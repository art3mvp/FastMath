package com.example.fastmath.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fastmath.R
import com.example.fastmath.data.GameRepositoryImpl
import com.example.fastmath.domain.entity.GameResult
import com.example.fastmath.domain.entity.GameSettings
import com.example.fastmath.domain.entity.Level
import com.example.fastmath.domain.entity.Question
import com.example.fastmath.domain.usecases.GenerateQuestionUseCase
import com.example.fastmath.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application) : AndroidViewModel(application) {


    private val context = application
    private lateinit var gameSettings: GameSettings
    private lateinit var level: Level

    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var timer: CountDownTimer? = null

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private var countOfCorrectAnswers = 0
    private var countOfQuestions = 0

    private val _percentOfCorrectAnswers = MutableLiveData<Int>()
    val percentOfCorrectAnswers:LiveData<Int>
        get() = _percentOfCorrectAnswers

    private val _progressOfCorrectAnswers = MutableLiveData<String>()
    val progressOfCorrectAnswers:LiveData<String>
        get() = _progressOfCorrectAnswers

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent:LiveData<Boolean>
        get() = _enoughPercent

    private val _enoughCorrectAnswers = MutableLiveData<Boolean>()
    val enoughCorrectAnswers:LiveData<Boolean>
        get() = _enoughCorrectAnswers

    private val _minPercent = MutableLiveData<Int>()
    val minPercent:LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult:LiveData<GameResult>
        get() = _gameResult


    private fun generateQuestion() {
        _question.value = generateQuestionUseCase.invoke(gameSettings.maxSumValue)
    }

    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        generateQuestion()
        updateProgress()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentOfCorrectAnswers.value = percent
        _progressOfCorrectAnswers.value = String.format(
            context.resources.getString(R.string.progress_answers),
            countOfCorrectAnswers.toString(),
            gameSettings.minCountOfRightAnswers.toString()
        )
        _enoughCorrectAnswers.value = countOfCorrectAnswers >= gameSettings.minCountOfRightAnswers
        _enoughPercent.value = percent >= gameSettings.minPercentOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int {
        if (countOfQuestions == 0) {
            return 0
        }
        return ((countOfCorrectAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    fun chooseAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfCorrectAnswers++
        }
        countOfQuestions++

        updateProgress()
        generateQuestion()
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingsUseCase.invoke(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }

    private fun startTimer() {
         timer = object :
            CountDownTimer(
                gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS,
                MILLIS_IN_SECONDS
            ) {
            override fun onTick(millis: Long) {
                _formattedTime.value = formatTime(millis)
            }

            override fun onFinish() {
               finishGame()
            }
        }
        timer?.start()
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughPercent.value == true && enoughCorrectAnswers.value == true,
            countOfCorrectAnswers,
            countOfQuestions,
            gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    private fun formatTime(millis: Long): String {
        val seconds = millis / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTE
        val remainingSeconds = seconds % SECONDS_IN_MINUTE
        return String.format(TIME_TEMPLATE, minutes, remainingSeconds)
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val TIME_TEMPLATE = "%02d:%02d"
        private const val SECONDS_IN_MINUTE = 60
    }


}