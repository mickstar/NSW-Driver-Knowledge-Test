package com.mickstarify.dktcar.QuestionScreen

import com.mickstarify.dktcar.Database.Question

interface View {
    fun initUI()
    fun displayQuestion(
        questionProgress: String,
        questionTitle: String,
        questionText: String,
        category: String,
        image: String,
        optionA: String,
        optionB: String,
        optionC: String
    )

    fun animateAnswer(correct: Boolean, buttonIndex: Int, onAnimationEnd: () -> Unit)
    fun showSuccessfullyCompletedTest()
    fun showFailedTest()
}

interface Presenter {
    fun submitAnswer(index: Int)
    fun displayQuestion(question: Question, progressText: String)
    fun showSuccessfullyCompletedTest()
    fun showFailedTest()
}

interface Model {
    abstract var correctAnswerIndex: Int
    fun startTest()
    fun didUserAnsweredCorrectly(answeredCorrectly: Boolean)
    fun getProgressString(): String
}