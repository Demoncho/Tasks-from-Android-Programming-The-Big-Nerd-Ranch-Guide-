package com.example.trainingapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders

private const val EXTRA_ANSWER_IS_TRUE = "com.example.trainingapp.answer_is_true"
private const val CHEATS_REMAIN_LAST = "com.example.trainingapp.cheats_remain_last"
const val EXTRA_ANSWER_SHOWN = "com.example.trainingapp.answer_shown"
const val CHEATS_REMAIN = "com.example.trainingapp.cheats_remain"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var apiLvl: TextView
    private lateinit var showAnswerButton: Button

    private var answerIsTrue = false

    private val apiName = "Api Level: " + Build.VERSION.SDK_INT.toString()

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, cheatsRemain: Int): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(CHEATS_REMAIN_LAST, cheatsRemain)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        apiLvl = findViewById(R.id.api_lvl)
        showAnswerButton = findViewById(R.id.show_answer_button)

        apiLvl.text = apiName

        quizViewModel.cheatsRemain = intent.getIntExtra(CHEATS_REMAIN_LAST, 3)

        showAnswerButton.setOnClickListener {
            setAnswerText()
            quizViewModel.isCheater = true
            quizViewModel.cheatsRemain --
            setAnswerShownResult(true)
        }

        setAnswerShownResult(quizViewModel.isCheater)
        if (quizViewModel.isCheater)
            setAnswerText()
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
            putExtra(CHEATS_REMAIN, quizViewModel.cheatsRemain)
        }
        Log.d("AAAAA", quizViewModel.cheatsRemain.toString())
        setResult(Activity.RESULT_OK, data)
    }

    private fun setAnswerText() {
        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        answerTextView.setText(answerText)
    }

}