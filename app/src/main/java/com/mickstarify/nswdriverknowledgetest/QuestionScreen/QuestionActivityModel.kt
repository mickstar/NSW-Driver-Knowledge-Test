package com.mickstarify.nswdriverknowledgetest.QuestionScreen

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import com.mickstarify.nswdriverknowledgetest.Database.Question
import com.mickstarify.nswdriverknowledgetest.Database.QuestionDatabase
import com.mickstarify.nswdriverknowledgetest.Database.QuestionDatabaseMode
import com.mickstarify.nswdriverknowledgetest.Database.Room.QuestionStatus
import com.mickstarify.nswdriverknowledgetest.Database.Room.QuestionStatusDatabase
import com.mickstarify.nswdriverknowledgetest.NSWDriverKnowledgeTestApplication
import com.mickstarify.nswdriverknowledgetest.PreferenceManager
import com.mickstarify.nswdriverknowledgetest.TestRunner.FormalTest
import com.mickstarify.nswdriverknowledgetest.TestRunner.SequentialTestRunner
import com.mickstarify.nswdriverknowledgetest.TestRunner.TestRunner
import com.mickstarify.nswdriverknowledgetest.TestRunner.TestStatus
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class QuestionActivityModel(val context: Context, val presenter: Presenter, val questionMode: String) : Model {
    @Inject
    lateinit var preferenceManager: PreferenceManager

    @Inject
    lateinit var questionDatabase: QuestionDatabase

    val questionStatusDatabase = QuestionStatusDatabase(context)

    var testRunner: TestRunner
    override var correctAnswerIndex: Int = -1

    init {
        ((context as Activity).application as NSWDriverKnowledgeTestApplication).component.inject(
            this
        )
        testRunner = when(questionMode){
            QuestionActivityView.MODE_SEQUENTIAL_QUESTIONS -> {
                val questionId = (context as Activity).intent.getIntExtra(QuestionActivityView.QUESTION_ID, 1)
                SequentialTestRunner(questionDatabase, questionId)
            }
            QuestionActivityView.MODE_FORMAL_TEST -> FormalTest(questionDatabase)
            else -> throw Exception("invalid mode provided.")
        }
    }

    override fun getQuestion(): Question {
        return testRunner.getNextQuestion()
    }

    override fun startTest() {
        showNextQuestion()
    }

    var answeredQuestion = false

    override fun didUserAnsweredCorrectly(answeredCorrectly: Boolean) {
        if (answeredQuestion) {
            // user has already incorrectly answered this question.
            if (answeredCorrectly){
                showNextQuestion()
            }
            return
        }

        answeredQuestion = true

        testRunner.setUserAnswerCorrectly(answeredCorrectly)

        val status = when (answeredCorrectly) {
            true -> QuestionStatus.ANSWERED_CORRECT
            false -> QuestionStatus.ANSWERED_INCORRECT
        }
        Log.d("dktcar", "User answer:$answeredCorrectly  status $status")
        val question = testRunner.getQuestion()
        questionStatusDatabase.setQuestionStatus(question.title, questionDatabase.mode, status)
            .subscribeOn(
                Schedulers.io()
            ).subscribe {
            Log.d("dktcar", "Set questionDatabase ${question.title} to status $status")
        }

        // only going to proceed to next question if they got it correct.
        showNextQuestion()
    }

    private fun showNextQuestion() {
        when (testRunner.getTestStatus()){
            TestStatus.INPROGRESS -> {
                presenter.displayQuestion(
                    testRunner.getNextQuestion(),
                    testRunner.getQuestionProgressString()
                )
                answeredQuestion = false
            }
            TestStatus.SUCCESSFULLY_COMPLETED -> {
                presenter.showSuccessfullyCompletedTest()
            }
            TestStatus.FAILED -> {
                presenter.showFailedTest()
            }
        }


    }

    override fun getProgressString(): String {
        return testRunner.getQuestionProgressString()
    }
}