package com.mickstarify.nswdriverknowledgetest.TestRunner

import com.mickstarify.nswdriverknowledgetest.Database.Question

interface TestRunner {
    fun getNextQuestion(): Question
    fun getTestStatus(): TestStatus
    fun setUserAnswerCorrectly(isCorrect: Boolean)
    fun getQuestionProgressString() : String
    fun getQuestion() : Question
}