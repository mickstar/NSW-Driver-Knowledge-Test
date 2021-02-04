package com.mickstarify.dktcar.QuestionScreen

import android.content.Context
import android.util.Log
import com.mickstarify.dktcar.Database.Question

class QuestionActivityPresenter(val view: View, val questionMode: String) : Presenter {
    val model: Model

    init {
        model = QuestionActivityModel(view as Context, this, questionMode)
        view.initUI()
        model.startTest()
    }

    override fun submitAnswer(index: Int) {
        val answeredCorrectly = index == model.correctAnswerIndex
        Log.d("dktcar", "User Answered Correctly $answeredCorrectly")
        view.animateAnswer(answeredCorrectly, index) {
            model.didUserAnsweredCorrectly(answeredCorrectly)
        }
    }

    override fun displayQuestion(question: Question, progressText: String) {
        val answers = listOf(question.optionA, question.optionB, question.optionC).shuffled()
        model.correctAnswerIndex = answers.indexOf(question.optionA)

        view.displayQuestion(
            model.getProgressString(),
            question.title,
            question.question,
            question.category,
            question.picture,
            answers[0], answers[1], answers[2]
        )
    }

    override fun showSuccessfullyCompletedTest() {
        view.showSuccessfullyCompletedTest()
    }

    override fun showFailedTest() {
        view.showFailedTest()
    }
}