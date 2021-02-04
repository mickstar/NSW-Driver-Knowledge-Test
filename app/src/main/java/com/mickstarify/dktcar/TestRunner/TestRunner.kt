package com.mickstarify.dktcar.TestRunner

import com.mickstarify.dktcar.Database.Question

interface TestRunner {
    fun getNextQuestion(): Question
    fun getTestStatus(): TestStatus
    fun setUserAnswerCorrectly(isCorrect: Boolean)
    fun getQuestionProgressString() : String
    fun getQuestion() : Question
}