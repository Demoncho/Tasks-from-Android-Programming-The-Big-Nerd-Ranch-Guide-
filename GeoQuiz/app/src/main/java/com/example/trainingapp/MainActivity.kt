package com.example.trainingapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val KEY_AVAILABILITY = "availability"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var cheatButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var cheatsRemainTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        quizViewModel.currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.enableAnswer = savedInstanceState?.getBoolean(KEY_AVAILABILITY, true) ?: true

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        cheatButton = findViewById(R.id.cheat_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)
        cheatsRemainTextView = findViewById(R.id.cheats_remain)

        changeButtonsEnability()

        val countCheats = getString(R.string.cheats_remain) + ' ' + quizViewModel.cheatsRemain
        cheatsRemainTextView.text = countCheats
        if (quizViewModel.cheatsRemain == 0)
            cheatButton.isEnabled = false

        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        cheatButton.setOnClickListener { view ->
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val cheatsRemain = quizViewModel.cheatsRemain
            Log.d(TAG, cheatsRemain.toString())
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue, cheatsRemain)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val options = ActivityOptions
                    .makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
            } else {
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }
        }

        nextButton.setOnClickListener {
            nextQuestion()
        }

        prevButton.setOnClickListener {
            previousQuestion()
        }

        questionTextView.setOnClickListener {
            nextQuestion()
        }

        updateQuestion()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult called")
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            quizViewModel.cheatsRemain =
                data?.getIntExtra(CHEATS_REMAIN, 3) ?: 3
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
        val countCheats = getString(R.string.cheats_remain) + ' ' + quizViewModel.cheatsRemain
        cheatsRemainTextView.text = countCheats
        if (quizViewModel.cheatsRemain == 0)
            cheatButton.isEnabled = false
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        savedInstanceState.putBoolean(KEY_AVAILABILITY, quizViewModel.enableAnswer)
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }


    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        changeButtonsEnability()
    }

    private fun nextQuestion() {
        quizViewModel.moveToNext()
        quizViewModel.enableAnswer = true
        updateQuestion()
    }

    private fun previousQuestion() {
        quizViewModel.moveToPrev()
        quizViewModel.enableAnswer = true
        updateQuestion()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        quizViewModel.enableAnswer = false
        changeButtonsEnability()
    }

    private fun changeButtonsEnability() {
        val availability = quizViewModel.enableAnswer
        trueButton.isEnabled = availability
        falseButton.isEnabled = availability
    }

}