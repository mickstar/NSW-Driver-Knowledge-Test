package com.mickstarify.dktcar.QuestionScreen

import android.app.Activity
import android.content.Context
import android.util.Log
import com.mickstarify.dktcar.Database.QuestionDatabase
import com.mickstarify.dktcar.Database.Room.QuestionStatus
import com.mickstarify.dktcar.Database.Room.QuestionStatusDatabase
import com.mickstarify.dktcar.NSWDriverKnowledgeTestApplication
import com.mickstarify.dktcar.PreferenceManager
import com.mickstarify.dktcar.TestRunner.FormalTest
import com.mickstarify.dktcar.TestRunner.SequentialTestRunner
import com.mickstarify.dktcar.TestRunner.TestRunner
import com.mickstarify.dktcar.TestRunner.TestStatus
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
                Log.d("dktcar", "test still in progress")

                presenter.displayQuestion(
                    testRunner.getNextQuestion(),
                    testRunner.getQuestionProgressString()
                )
                answeredQuestion = false
            }
            TestStatus.SUCCESSFULLY_COMPLETED -> {
                Log.d("dktcar", "test finished")
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