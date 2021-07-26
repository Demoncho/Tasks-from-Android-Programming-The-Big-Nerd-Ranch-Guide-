package com.example.trainingapp

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
class QuizViewModel : ViewModel() {

    var currentIndex = 0
    var isCheater = false
    var cheatsRemain = 3

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    var enableAnswer = true

    fun moveToNext() {
        isCheater = false
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        isCheater = false
        currentIndex = (currentIndex - 1 + questionBank.size) % questionBank.size
    }
}